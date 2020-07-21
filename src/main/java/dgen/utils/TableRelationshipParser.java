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
    /* Mapping of all relationships in a table */
    private Map<Integer, Set<Integer>> relationshipMap = new HashMap<>();
    private int numRelationships = 0;
    private final Map<Integer, ColumnSchema> columnMap;

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

    /**
     * Parses a DefTableRelationshipSchema object and preforms very basic checks to ensure that the relationship
     * is valid.
     * @param tableRelationship DefTableRelationshipSchema object to parse.
     * @return A parsed and validated DefTableRelationshipSchema object.
     */
    public DefTableRelationshipSchema parseTableRelationship(DefTableRelationshipSchema tableRelationship) {
        Map<Integer, Set<Integer>> dependencyMap = tableRelationship.getDependencyMap();

        for (Integer start: dependencyMap.keySet()) {
            for (Integer end: dependencyMap.get(start)) {
                if (start.equals(end)) {
                    throw new SpecificationException("Columns can't have relationships with themselves");
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
                ColumnSchema startColumn = columnMap.get(start);
                ColumnSchema endColumn = columnMap.get(end);
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

    /**
     * Parses a GenTableRelationshipSchema object into a DefTableRelationshipSchema object with a dependency map.
     * @param genTableRelationship GenTableRelationshipSchema object to parse.
     * @return Parsed DefTableRelationshipSchema object.
     */
    public DefTableRelationshipSchema parseGenTableRelationship(GenTableRelationshipSchema genTableRelationship) {
        if (genTableRelationship.getNumRelationships() + numRelationships > columnMap.size() * (columnMap.size() - 1)) {
            throw new SpecificationException("Too many relationships in one table");
        }

        DefTableRelationshipSchema tableRelationship = new DefTableRelationshipSchema();
        List<Integer> columnIDs = new ArrayList<>(columnMap.keySet());

        tableRelationship.setDependencyFunction(genTableRelationship.getDependencyFunction());
        GraphSchema graphSchema = genTableRelationship.getGraphSchema();

        tableRelationship.setDependencyMap(graphSchema.generateTableGraph(columnIDs,
                genTableRelationship.getNumRelationships(), relationshipMap));

        return parseTableRelationship(tableRelationship);
    }
}
