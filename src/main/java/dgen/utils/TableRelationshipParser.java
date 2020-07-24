package dgen.utils;


import dgen.utils.specs.ColumnSpec;
import dgen.utils.specs.relationships.GraphSpec;
import dgen.utils.specs.relationships.dependencyFunctions.DependencyFunction;
import dgen.utils.specs.relationships.DefTableRelationshipSpec;
import dgen.utils.specs.relationships.GenTableRelationshipSpec;
import dgen.utils.specs.relationships.TableRelationshipSpec;
import dgen.utils.specs.relationships.dependencyFunctions.JaccardSimilarity;

import java.util.*;

public class TableRelationshipParser {
    /* Mapping of all relationships in a table */
    private Map<Integer, Set<Integer>> relationshipMap = new HashMap<>();
    private int numRelationships = 0;
    private final Map<Integer, ColumnSpec> columnMap;
    private RandomGenerator rnd;

    public TableRelationshipParser(Map<Integer, ColumnSpec> columnMap, RandomGenerator rnd) {
        this.columnMap = columnMap;
        this.rnd = rnd;
    }

    public DefTableRelationshipSpec parse(TableRelationshipSpec relationshipSchema) {
        switch (relationshipSchema.relationshipType()) {
            case DEFTABLE:
                DefTableRelationshipSpec defTableRelationship = (DefTableRelationshipSpec) relationshipSchema;
                return parseTableRelationship(defTableRelationship);
            case GENTABLE:
                GenTableRelationshipSpec genTableRelationship = (GenTableRelationshipSpec) relationshipSchema;
                return parseGenTableRelationship(genTableRelationship);
            default:
                throw new SpecificationException("Type " + relationshipSchema.relationshipType() +
                        " not parsable by TableRelationshipParser");
        }
    }

    /**
     * Parses a DefTableRelationshipSchema object and preforms very basic checks to ensure that the relationship
     * is valid.
     *
     * @param tableRelationship DefTableRelationshipSchema object to parse.
     * @return A parsed and validated DefTableRelationshipSchema object.
     */
    public DefTableRelationshipSpec parseTableRelationship(DefTableRelationshipSpec tableRelationship) {
        Map<Integer, Set<Integer>> dependencyMap = tableRelationship.getDependencyMap();

        for (Integer start : dependencyMap.keySet()) {
            for (Integer end : dependencyMap.get(start)) {
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
                ColumnSpec startColumn = columnMap.get(start);
                ColumnSpec endColumn = columnMap.get(end);
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
     *
     * @param genTableRelationship GenTableRelationshipSchema object to parse.
     * @return Parsed DefTableRelationshipSchema object.
     */
    public DefTableRelationshipSpec parseGenTableRelationship(GenTableRelationshipSpec genTableRelationship) {
        if (genTableRelationship.getNumRelationships() + numRelationships > columnMap.size() * (columnMap.size() - 1)) {
            throw new SpecificationException("Too many relationships in one table");
        }

        DefTableRelationshipSpec tableRelationship = new DefTableRelationshipSpec();
        List<Integer> columnIDs = new ArrayList<>(columnMap.keySet());

        tableRelationship.setDependencyFunction(genTableRelationship.getDependencyFunction());
        GraphSpec graphSpec = genTableRelationship.getGraphSpec();
        graphSpec.setRandomGenerator(rnd);

        tableRelationship.setDependencyMap(graphSpec.generateTableGraph(columnIDs,
                genTableRelationship.getNumRelationships(), relationshipMap));

        return parseTableRelationship(tableRelationship);
    }
}
