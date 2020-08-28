package dgen.utils.parsers;


import dgen.utils.parsers.specs.ColumnSpec;
import dgen.utils.parsers.specs.relationshipspecs.DefTableRelationshipSpec;
import dgen.utils.parsers.specs.relationshipspecs.GenTableRelationshipSpec;
import dgen.utils.parsers.specs.relationshipspecs.GraphSpec;
import dgen.utils.parsers.specs.relationshipspecs.TableRelationshipSpec;
import dgen.utils.parsers.specs.relationshipspecs.dependencyFunctions.DependencyFunction;

import java.util.*;

public class TableRelationshipParser {
    /* Mapping of all relationships in a table */
    private Map<Integer, Set<Integer>> relationshipMap = new HashMap<>();
    private int numRelationships = 0;
    private final Map<Integer, ColumnSpec> columnMap;
    private Set<Integer> usedColumns;
    private RandomGenerator rnd;

    public TableRelationshipParser(Map<Integer, ColumnSpec> columnMap, RandomGenerator rnd) {
        this.columnMap = columnMap;
        this.rnd = rnd;
        this.usedColumns = new HashSet<>();
    }

    public DefTableRelationshipSpec parse(TableRelationshipSpec relationshipSchema) {
        relationshipSchema.validate();

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
    private DefTableRelationshipSpec parseTableRelationship(DefTableRelationshipSpec tableRelationship) {
        Map<Integer, Set<Integer>> dependencyMap = tableRelationship.getDependencyMap();

        for (Integer determinant : dependencyMap.keySet()) {
            for (Integer dependent : dependencyMap.get(determinant)) {
                if (determinant.equals(dependent)) {
                    throw new SpecificationException("Columns can't have relationships with themselves");
                }
                if (!(columnMap.containsKey(determinant))) {
                    throw new SpecificationException("Column with columnID " + determinant + " not found");
                }
                if (!(columnMap.containsKey(dependent))) {
                    throw new SpecificationException("Column with columnID " + dependent + " not found");
                }
                if (relationshipMap.containsKey(determinant) && relationshipMap.get(determinant).contains(dependent)) {
                    throw new SpecificationException("Relationship between columnID " + determinant + " and " + dependent +
                            " already exists");
                }
                if (usedColumns.contains(dependent)) {
                    throw new SpecificationException("Relationship already exists");
                }

                DependencyFunction dependencyFunction = tableRelationship.getDependencyFunction();
                ColumnSpec startColumn = columnMap.get(determinant);
                ColumnSpec endColumn = columnMap.get(dependent);
                dependencyFunction.validate(startColumn, endColumn);

                if (relationshipMap.containsKey(determinant)) {
                    Set<Integer> ends = relationshipMap.get(determinant);
                    ends.add(dependent);
                    relationshipMap.replace(determinant, ends);
                } else {
                    Set<Integer> ends = new HashSet<>();
                    ends.add(dependent);
                    relationshipMap.put(determinant, ends);
                }

                numRelationships += 1;
                usedColumns.add(dependent);
            }
        }

        if (tableRelationship.getRandomSeed() == null) {
            tableRelationship.setRandomSeed(rnd.nextLong());
        }

        return tableRelationship;
    }

    /**
     * Parses a GenTableRelationshipSchema object into a DefTableRelationshipSchema object with a dependency map.
     *
     * @param genTableRelationship GenTableRelationshipSchema object to parse.
     * @return Parsed DefTableRelationshipSchema object.
     */
    private DefTableRelationshipSpec parseGenTableRelationship(GenTableRelationshipSpec genTableRelationship) {
        if (genTableRelationship.getNumRelationships() + numRelationships > columnMap.size() - usedColumns.size()) {
            throw new SpecificationException("Too many relationships in one table");
        }

        DefTableRelationshipSpec tableRelationship = new DefTableRelationshipSpec();
        List<Integer> columnIDs = new ArrayList<>(columnMap.keySet());

        tableRelationship.setDependencyFunction(genTableRelationship.getDependencyFunction());
        GraphSpec graphSpec = genTableRelationship.getGraphSpec();
        graphSpec.setRandomGenerator(rnd);

        tableRelationship.setDependencyMap(graphSpec.generateTableGraph(columnIDs,
                genTableRelationship.getNumRelationships(), relationshipMap, new HashSet<>(usedColumns)));

        return parseTableRelationship(tableRelationship);
    }
}
