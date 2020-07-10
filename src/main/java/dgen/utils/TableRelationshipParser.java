package dgen.utils;

import dgen.utils.schemas.*;
import dgen.utils.schemas.relationships.GraphSchema;
import dgen.utils.schemas.relationships.dependencyFunctions.DependencyFunction;
import dgen.utils.schemas.relationships.DefTableRelationshipSchema;
import dgen.utils.schemas.relationships.GenTableRelationshipSchema;
import dgen.utils.schemas.relationships.TableRelationshipSchema;

import java.util.*;

public class TableRelationshipParser {
    private Map<Integer, Set<Integer>> relationshipMap = new HashMap<>();
    private int numRelationships = 0;
    private Map<Integer, ColumnSchema> columnIDMap;

    public TableRelationshipParser(Map<Integer, ColumnSchema> columnIDMap) {
        this.columnIDMap = columnIDMap;
    }

    public DefTableRelationshipSchema parse(TableRelationshipSchema relationshipSchema) {
        switch (relationshipSchema.relationshipType()) {
            case "defTableRelationship":
                DefTableRelationshipSchema defTableRelationship = (DefTableRelationshipSchema) relationshipSchema;
                return parseTableRelationship(defTableRelationship);
            case "genTableRelationship":
                GenTableRelationshipSchema genTableRelationship = (GenTableRelationshipSchema) relationshipSchema;
                return parseGenTableRelationship(genTableRelationship);
        }
        return new DefTableRelationshipSchema();
    }

    public DefTableRelationshipSchema parseTableRelationship(DefTableRelationshipSchema tableRelationship) {
        Map<Integer, Set<Integer>> dependencyMap = tableRelationship.getDependencyMap();

        for (Integer start: dependencyMap.keySet()) {
            for (Integer end: dependencyMap.get(start)) {
                if (start.equals(end)) {
                    throw new SpecificationException("Cannot create relationship between the same column");
                }
                if (!(columnIDMap.containsKey(start))) {
                    throw new SpecificationException("Column with columnID " + start + " not found");
                }
                if (!(columnIDMap.containsKey(end))) {
                    throw new SpecificationException("Column with columnID " + end + " not found");
                }
                if (relationshipMap.containsKey(start) && relationshipMap.get(start).contains(end)) {
                    throw new SpecificationException("Relationship between columnID " + start + " and " + end +
                            " already exists");
                }

                DependencyFunction dependencyFunction = tableRelationship.getDependencyFunction();
                DefColumnSchema startColumn = (DefColumnSchema) columnIDMap.get(start);
                DefColumnSchema endColumn = (DefColumnSchema) columnIDMap.get(end);
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
        if (genTableRelationship.getNumRelationships() + numRelationships > columnIDMap.size() * (columnIDMap.size() - 1)) {
            throw new SpecificationException("Too many relationships in one table");
        }

        DefTableRelationshipSchema tableRelationship = new DefTableRelationshipSchema();
        tableRelationship.setDependencyFunction(genTableRelationship.getDependencyFunction());
        GraphSchema graphSchema = genTableRelationship.getGraphSchema();
        List<Integer> columnIDs = new ArrayList<>(columnIDMap.keySet());
        tableRelationship.setDependencyMap(graphSchema.generateTableGraph(columnIDs,
                genTableRelationship.getNumRelationships(), relationshipMap));
        return parseTableRelationship(tableRelationship);
    }


    public Map<Integer, Set<Integer>> getRelationshipMap() {
        return relationshipMap;
    }

    public void setRelationshipMap(Map<Integer, Set<Integer>> relationshipMap) {
        this.relationshipMap = relationshipMap;
    }

    public Map<Integer, ColumnSchema> getColumnIDMap() {
        return columnIDMap;
    }

    public void setColumnIDMap(Map<Integer, ColumnSchema> columnIDMap) {
        this.columnIDMap = columnIDMap;
    }
}
