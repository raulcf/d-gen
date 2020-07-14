package dgen.utils;


import dgen.utils.schemas.ColumnSchema;
import dgen.utils.schemas.DefColumnSchema;
import dgen.utils.schemas.relationships.GraphSchema;
import dgen.utils.schemas.relationships.dependencyFunctions.DependencyFunction;
import dgen.utils.schemas.relationships.DefTableRelationshipSchema;
import dgen.utils.schemas.relationships.GenTableRelationshipSchema;
import dgen.utils.schemas.relationships.TableRelationshipSchema;

import java.util.*;

public class TableRelationshipParser {
    private Map<Integer, Set<Integer>> relationshipMap = new HashMap<>();
    private int numRelationships = 0;
    private Map<Integer, ColumnSchema> columnMap;

    public TableRelationshipParser(Map<Integer, ColumnSchema> columnMap) {
        this.columnMap = columnMap;
    }

    public DefTableRelationshipSchema parse(TableRelationshipSchema relationshipSchema) {
        switch (relationshipSchema.relationshipType()) {
            case DEFTABLE:
                DefTableRelationshipSchema defTableRelationship = (DefTableRelationshipSchema) relationshipSchema;
                return parseTableRelationship(defTableRelationship);
            case GENTABLE:
                GenTableRelationshipSchema genTableRelationship = (GenTableRelationshipSchema) relationshipSchema;
                return parseGenTableRelationship(genTableRelationship);
            default:
                throw new SpecificationException("Type " + relationshipSchema.relationshipType() +
                        " not parsable by TableRelationshipParser");
        }
    }

    public DefTableRelationshipSchema parseTableRelationship(DefTableRelationshipSchema tableRelationship) {
        Map<Integer, Set<Integer>> dependencyMap = tableRelationship.getDependencyMap();

        for (Integer start: dependencyMap.keySet()) {
            for (Integer end: dependencyMap.get(start)) {
                if (start.equals(end)) {
                    throw new SpecificationException("Cannot create relationship between the same column");
                }
                if (!(columnMap.containsKey(start))) {
                    throw new SpecificationException("Column with columnID " + start + " not found");
                }
                if (!(columnMap.containsKey(end))) {
                    throw new SpecificationException("Column with columnID " + end + " not found");
                }
                if (relationshipMap.containsKey(start) && relationshipMap.get(start).contains(end)) {
                    throw new SpecificationException("Relationship between columnID " + start + " and " + end +
                            " already exists");
                }

                DependencyFunction dependencyFunction = tableRelationship.getDependencyFunction();
                DefColumnSchema startColumn = (DefColumnSchema) columnMap.get(start);
                DefColumnSchema endColumn = (DefColumnSchema) columnMap.get(end);
                dependencyFunction.validate(startColumn, endColumn);

                if (relationshipMap.containsKey(start)) {
                    Set<Integer> ends = relationshipMap.get(start);
                    ends.add(end);
                    relationshipMap.replace(start, ends);
                } else {
                    Set<Integer> ends = new HashSet<>();
                    ends.add(end);
                    relationshipMap.put(start, ends);
                }

                numRelationships += 1;
            }
        }
        return tableRelationship;
    }

    public DefTableRelationshipSchema parseGenTableRelationship(GenTableRelationshipSchema genTableRelationship) {
        if (genTableRelationship.getNumRelationships() + numRelationships > columnMap.size() * (columnMap.size() - 1)) {
            throw new SpecificationException("Too many relationships in one table");
        }

        DefTableRelationshipSchema tableRelationship = new DefTableRelationshipSchema();
        tableRelationship.setDependencyFunction(genTableRelationship.getDependencyFunction());
        GraphSchema graphSchema = genTableRelationship.getGraphSchema();
        List<Integer> columnIDs = new ArrayList<>(columnMap.keySet());
        tableRelationship.setDependencyMap(graphSchema.generateTableGraph(columnIDs,
                genTableRelationship.getNumRelationships(), relationshipMap));
        return parseTableRelationship(tableRelationship);
    }
}
