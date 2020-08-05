package dgen.parsertests;

import dgen.utils.parsers.RandomGenerator;
import dgen.utils.parsers.SpecificationException;
import dgen.utils.parsers.TableRelationshipParser;
import dgen.utils.parsers.specs.ColumnSpec;
import dgen.utils.parsers.specs.DefColumnSpec;
import dgen.utils.parsers.specs.datatypespecs.IntegerSpec;
import dgen.utils.parsers.specs.datatypespecs.StringSpec;
import dgen.utils.parsers.specs.relationships.*;
import dgen.utils.parsers.specs.relationships.dependencyFunctions.FunctionalDependency;
import dgen.utils.parsers.specs.relationships.dependencyFunctions.JaccardSimilarity;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class TableRelationshipParserTest {

    TableRelationshipParser tableRelationshipParser;
    long randomSeed;

    @Before
    public void setup() { randomSeed = 1; }

    @Test
    public void testJaccardSimilarityValidate() {
        DefColumnSpec columnSpec1 = new DefColumnSpec();
        DefColumnSpec columnSpec2 = new DefColumnSpec();
        columnSpec1.setDataTypeSpec(new IntegerSpec());
        columnSpec2.setDataTypeSpec(new StringSpec());

        JaccardSimilarity jaccardSimilarity = new JaccardSimilarity();
        jaccardSimilarity.setSimilarity(1.5f);
        try {
            jaccardSimilarity.validate(columnSpec1, columnSpec2);
            fail("Failed to catch similarity > 1");
        } catch (SpecificationException e) {}

        jaccardSimilarity.setSimilarity(-1);
        try {
            jaccardSimilarity.validate(columnSpec1, columnSpec2);
            fail("Failed to catch similarity < 0");
        } catch (SpecificationException e) {}

        jaccardSimilarity.setSimilarity(0);
        try {
            // TODO: Needs more testing when we implement more datatype checks for jaccard similarity
            jaccardSimilarity.validate(columnSpec1, columnSpec2);
            fail("Failed to catch differing dataTypeSpec");
        } catch (SpecificationException e) {}
    }

    @Test
    public void testFunctionalDependencyValidate() {
        DefColumnSpec columnSpec1 = new DefColumnSpec();
        DefColumnSpec columnSpec2 = new DefColumnSpec();
        columnSpec1.setHasNull(true);

        FunctionalDependency functionalDependency = new FunctionalDependency();

        try {
            functionalDependency.validate(columnSpec1, columnSpec2);
            fail("Failed to catch column can have nulls");
        } catch (SpecificationException e) {}

    }

    @Test
    public void testParseTableRelationship() {
        Map<Integer, ColumnSpec> columnMap = new HashMap<>();
        DefColumnSpec columnSpec;
        for (int i = 0; i < 100; i++) {
            columnSpec = new DefColumnSpec();
            columnSpec.setColumnID(i);
            columnSpec.setDataTypeSpec(new IntegerSpec());
            columnMap.put(i, columnSpec);
        }
        tableRelationshipParser = new TableRelationshipParser(columnMap, new RandomGenerator(randomSeed));

        DefTableRelationshipSpec tableRelationship = new DefTableRelationshipSpec();
        Map<Integer, Set<Integer>> dependencyMap = new HashMap<>();
        dependencyMap.put(0, new HashSet<>(Arrays.asList(0, 1)));
        tableRelationship.setDependencyMap(dependencyMap);

        try {
            tableRelationshipParser.parse(tableRelationship);
            fail("Failed to catch relationship with self");
        } catch (SpecificationException e) {}

        tableRelationship = new DefTableRelationshipSpec();
        dependencyMap = new HashMap<>();
        dependencyMap.put(-1, new HashSet<>(Arrays.asList(0, 1)));
        tableRelationship.setDependencyMap(dependencyMap);

        try {
            tableRelationshipParser.parse(tableRelationship);
            fail("Failed to catch nonexistent determinant");
        } catch (SpecificationException e) {}

        tableRelationship = new DefTableRelationshipSpec();
        dependencyMap = new HashMap<>();
        dependencyMap.put(0, new HashSet<>(Arrays.asList(-1)));
        tableRelationship.setDependencyMap(dependencyMap);

        try {
            tableRelationshipParser.parse(tableRelationship);
            fail("Failed to catch nonexistent dependent");
        } catch (SpecificationException e) {}

        tableRelationship = new DefTableRelationshipSpec();
        dependencyMap = new HashMap<>();
        dependencyMap.put(0, new HashSet<>(Arrays.asList(1)));
        tableRelationship.setDependencyMap(dependencyMap);
        tableRelationship.setDependencyFunction(new FunctionalDependency());

        DefTableRelationshipSpec tableRelationship2 = new DefTableRelationshipSpec();
        Map<Integer, Set<Integer>> dependencyMap2 = new HashMap<>();
        dependencyMap2.put(0, new HashSet<>(Arrays.asList(1)));
        tableRelationship2.setDependencyMap(dependencyMap2);
        tableRelationship2.setDependencyFunction(new FunctionalDependency());

        try {
            tableRelationshipParser.parse(tableRelationship);
            tableRelationshipParser.parse(tableRelationship2);
            fail("Failed to catch duplicate relationship");
        } catch (SpecificationException e) {}

    }

    @Test
    public void testParseGenTableRelationship() {
        Map<Integer, ColumnSpec> columnMap = new HashMap<>();
        DefColumnSpec columnSpec;
        for (int i = 0; i < 100; i++) {
            columnSpec = new DefColumnSpec();
            columnSpec.setColumnID(i);
            columnSpec.setDataTypeSpec(new IntegerSpec());
            columnMap.put(i, columnSpec);
        }
        tableRelationshipParser = new TableRelationshipParser(columnMap, new RandomGenerator(randomSeed));

        GenTableRelationshipSpec genTableRelationship = new GenTableRelationshipSpec();
        genTableRelationship.setNumRelationships(101);
        genTableRelationship.setDependencyFunction(new FunctionalDependency());
        genTableRelationship.setGraphSpec(new RandomGraph());

        try {
            tableRelationshipParser.parse(genTableRelationship);
            fail("Failed to catch too many relationships");
        } catch (SpecificationException e) {}

        GraphSpec graphSpec;
        DefTableRelationshipSpec parsedTableRelationshipSpec;
        Map<Integer, Set<Integer>> parsedDependencyMap;
        List<Integer> dependents;
        GenTableRelationshipSpec genTableRelationship2;
        for (GraphType graphType: GraphType.values()) {
            switch (graphType) {
                case RANDOM:
                    graphSpec = new RandomGraph();
                    break;
                default:
                    graphSpec =  null;
            }

            tableRelationshipParser = new TableRelationshipParser(columnMap, new RandomGenerator(randomSeed));
            genTableRelationship = new GenTableRelationshipSpec();
            genTableRelationship.setNumRelationships(100);
            genTableRelationship.setDependencyFunction(new FunctionalDependency());
            genTableRelationship.setGraphSpec(graphSpec);
            parsedTableRelationshipSpec = tableRelationshipParser.parse(genTableRelationship);
            parsedDependencyMap = parsedTableRelationshipSpec.getDependencyMap();
            dependents = new ArrayList<>();

            for (Set<Integer> dependentSet: parsedDependencyMap.values()) {
                dependents.addAll(dependentSet);
            }

            assertEquals(dependents.size(), 100);
            assertEquals(dependents.size(), (new HashSet<>(dependents)).size());

            tableRelationshipParser = new TableRelationshipParser(columnMap, new RandomGenerator(randomSeed));
            genTableRelationship2 = new GenTableRelationshipSpec();
            genTableRelationship2.setNumRelationships(100);
            genTableRelationship2.setDependencyFunction(new FunctionalDependency());
            genTableRelationship2.setGraphSpec(graphSpec);
            assertEquals(parsedDependencyMap,
                    tableRelationshipParser.parse(genTableRelationship).getDependencyMap());
        }
    }

}
