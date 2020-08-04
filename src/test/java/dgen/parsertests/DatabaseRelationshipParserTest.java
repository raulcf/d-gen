package dgen.parsertests;

import dgen.utils.DatabaseRelationshipParser;
import dgen.utils.RandomGenerator;
import dgen.utils.SpecificationException;
import dgen.utils.specs.ColumnSpec;
import dgen.utils.specs.DefForeignKeySpec;
import dgen.utils.specs.PrimaryKeySpec;
import dgen.utils.specs.datatypespecs.IntegerSpec;
import dgen.utils.specs.relationships.DefPKFKSpec;
import dgen.utils.specs.relationships.GenPKFKSpec;
import dgen.utils.specs.relationships.RandomGraph;
import org.javatuples.Pair;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class DatabaseRelationshipParserTest {

    DatabaseRelationshipParser databaseRelationshipParser;
    long randomSeed;

    @Before
    public void setup() {
        randomSeed = 1;
    }

    @Test
    public void testDefPKFKParseMapping() {
        DefPKFKSpec defPKFKSpec = new DefPKFKSpec();
        Map<String, String> mapping = new HashMap<>();
        List<Map<String, String>> mappings = new ArrayList<>();
        mappings.add(mapping);
        mapping.put("0:0", "1:1");
        defPKFKSpec.setPkfkMappings(mappings);
        defPKFKSpec.parseMapping();

        assertEquals(defPKFKSpec.getPrimaryKeys().get(0), Pair.with(0, 0));
        assertEquals(defPKFKSpec.getForeignKeys().get(0), Pair.with(1, 1));

        defPKFKSpec = new DefPKFKSpec();
        List<Pair<Integer, Integer>> primaryKeys = new ArrayList<>();
        List<Pair<Integer, Integer>> foreignKeys = new ArrayList<>();
        primaryKeys.add(Pair.with(0, 0));
        foreignKeys.add(Pair.with(1, 1));
        defPKFKSpec.setPrimaryKeys(primaryKeys);
        defPKFKSpec.setForeignKeys(foreignKeys);
        defPKFKSpec.unparseMapping();


        assertTrue(defPKFKSpec.getPkfkMappings().get(0).containsKey("0:0"));
        assertEquals(defPKFKSpec.getPkfkMappings().get(0).get("0:0"), "1:1");
    }

    @Test(expected = SpecificationException.class)
    public void testDefPKFKValidate() {
        DefPKFKSpec defPKFKSpec = new DefPKFKSpec();
        Map<String, String> mapping = new HashMap<>();
        List<Map<String, String>> mappings = new ArrayList<>();
        mappings.add(mapping);
        mapping.put("0:0", "0:1");
        defPKFKSpec.setPkfkMappings(mappings);

        defPKFKSpec.parseMapping();
        defPKFKSpec.validate();
    }

    @Test
    public void testParseDefPKFK() {
        Map<Integer, ColumnSpec> columnMap;
        Map<Integer, Map<Integer, ColumnSpec>> tableMap = new HashMap<>();
        for (int i = 0; i < 100; i++) {
            columnMap = new HashMap<>();
            PrimaryKeySpec primaryKeySpec = new PrimaryKeySpec();
            DefForeignKeySpec foreignKeySpec = new DefForeignKeySpec();

            primaryKeySpec.setColumnID(1);
            primaryKeySpec.setDataTypeSpec(new IntegerSpec());
            foreignKeySpec.setColumnID(2);

            columnMap.put(1, primaryKeySpec);
            columnMap.put(2, foreignKeySpec);
            tableMap.put(i, columnMap);
        }

        databaseRelationshipParser = new DatabaseRelationshipParser(tableMap, new RandomGenerator(randomSeed));
        DefPKFKSpec defPKFKSpec = new DefPKFKSpec();
        Map<String, String> mapping = new HashMap<>();
        List<Map<String, String>> mappings = new ArrayList<>();

        mappings.add(mapping);
        mapping.put("-1:-1", "0:2");
        defPKFKSpec.setPkfkMappings(mappings);

        try {
            databaseRelationshipParser.parse(defPKFKSpec);
            fail("Failed to catch non-existent pk");
        } catch (SpecificationException e) { }

        mappings = new ArrayList<>();
        mapping = new HashMap<>();
        mapping.put("0:1", "-1:-1");
        mappings.add(mapping);
        defPKFKSpec.setPkfkMappings(mappings);

        try {
            databaseRelationshipParser.parse(defPKFKSpec);
            fail("Failed to catch non-existent fk");
        } catch (SpecificationException e) {}

        mappings = new ArrayList<>();
        mapping = new HashMap<>();
        mapping.put("0:1", "0:2");
        mappings.add(mapping);
        defPKFKSpec.setPkfkMappings(mappings);

        try {
            databaseRelationshipParser.parse(defPKFKSpec);
            fail("Failed to catch duplicate pk-fk relationship");
        } catch (SpecificationException e) {}
    }

    @Test
    public void testParseGenPKFK() {
        Map<Integer, ColumnSpec> columnMap;
        Map<Integer, Map<Integer, ColumnSpec>> tableMap = new HashMap<>();
        for (int i = 0; i < 100; i++) {
            columnMap = new HashMap<>();
            PrimaryKeySpec primaryKeySpec = new PrimaryKeySpec();
            DefForeignKeySpec foreignKeySpec = new DefForeignKeySpec();

            primaryKeySpec.setColumnID(1);
            primaryKeySpec.setDataTypeSpec(new IntegerSpec());
            foreignKeySpec.setColumnID(2);

            columnMap.put(1, primaryKeySpec);
            columnMap.put(2, foreignKeySpec);
            tableMap.put(i, columnMap);
        }

        databaseRelationshipParser = new DatabaseRelationshipParser(tableMap, new RandomGenerator(randomSeed));

        GenPKFKSpec genPKFKSpec1 = new GenPKFKSpec();
        genPKFKSpec1.setNumRelationships(1000);
        genPKFKSpec1.setGraphSpec(new RandomGraph());

        GenPKFKSpec genPKFKSpec2 = new GenPKFKSpec();
        genPKFKSpec2.setNumRelationships(100);
        genPKFKSpec2.setGraphSpec(new RandomGraph());

        try {
            databaseRelationshipParser.parse(genPKFKSpec1);
            fail("Failed to catch too many relationships");
        } catch (SpecificationException e) {}
        genPKFKSpec1.setNumRelationships(100);

        databaseRelationshipParser.parse(genPKFKSpec1);
        DefPKFKSpec parsedPKFKSchema1 = databaseRelationshipParser.getParsedPKFKSchema();

        databaseRelationshipParser = new DatabaseRelationshipParser(tableMap, new RandomGenerator(randomSeed));
        databaseRelationshipParser.parse(genPKFKSpec2);
        DefPKFKSpec parsedPKFKSchema2 = databaseRelationshipParser.getParsedPKFKSchema();

        // Checks that function produces the same results each time
        assertEquals(parsedPKFKSchema1.getPkfkMappings(), parsedPKFKSchema2.getPkfkMappings());

        // Checks that number of relationships matches the number we want
        assertEquals(parsedPKFKSchema1.getPrimaryKeys().size(), 100);
        assertEquals(parsedPKFKSchema1.getForeignKeys().size(), 100);

        // Checks that foreign keys are used only once
        assertEquals((new HashSet<>(parsedPKFKSchema2.getForeignKeys())).size(),
                parsedPKFKSchema2.getForeignKeys().size());

        // Checks that foreign keys and primary keys aren't in the same column
        for (int i = 0; i < parsedPKFKSchema1.getPrimaryKeys().size(); i++) {
            assertNotEquals(parsedPKFKSchema1.getPrimaryKeys().get(i).getValue0(),
                    parsedPKFKSchema1.getForeignKeys().get(i).getValue0());
        }
    }
}
