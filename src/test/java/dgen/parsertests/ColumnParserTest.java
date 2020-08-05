package dgen.parsertests;

import dgen.utils.parsers.ColumnParser;
import dgen.utils.parsers.RandomGenerator;
import dgen.utils.parsers.SpecificationException;
import dgen.utils.parsers.specs.*;
import dgen.utils.parsers.specs.datatypespecs.IntegerSpec;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class ColumnParserTest {
    ColumnParser columnParser;
    long randomSeed;

    @Before
    public void setup() {
        randomSeed = 1;
        columnParser = new ColumnParser(new RandomGenerator(randomSeed));
    }

    @Test
    public void testParseColumnName() {
        // Checks to make sure random name and regex name are false and null
        DefColumnSpec columnSpec = new DefColumnSpec();
        columnSpec.setColumnName("hello world");
        columnSpec.setDataTypeSpec(new IntegerSpec());
        columnParser.parse(columnSpec);
        DefColumnSpec parsedColumnSpec = (DefColumnSpec) columnParser.getColumns().get(0);
        assertNull(parsedColumnSpec.getRegexName());
        assertFalse(parsedColumnSpec.isRandomName());

        // Checks to make sure random name is false
        setup();
        columnSpec = new DefColumnSpec();
        columnSpec.setRegexName("[a-z]{2}");
        columnSpec.setDataTypeSpec(new IntegerSpec());
        columnParser.parse(columnSpec);
        parsedColumnSpec = (DefColumnSpec) columnParser.getColumns().get(0);
        assertFalse(parsedColumnSpec.isRandomName());
    }

    @Test(expected = SpecificationException.class)
    public void testCheckColumnID() {
        // Checks to make sure duplicate columnIDs aren't allowed
        DefColumnSpec columnSpec1 = new DefColumnSpec();
        DefColumnSpec columnSpec2 = new DefColumnSpec();
        columnSpec1.setDataTypeSpec(new IntegerSpec());
        columnSpec2.setDataTypeSpec(new IntegerSpec());

        columnParser.parse(columnSpec1);
        columnParser.parse(columnSpec2);
    }

    @Test
    public void testParseColumn() {
        DefColumnSpec columnSpec = new DefColumnSpec();
        IntegerSpec integerSpec = new IntegerSpec();
        integerSpec.setDefaultValue(0);
        columnSpec.setDataTypeSpec(integerSpec);
        columnParser.parse(columnSpec);
        DefColumnSpec parsedColumnSpec = (DefColumnSpec) columnParser.getColumns().get(0);

        assertNotNull(parsedColumnSpec.getRandomSeed());
        assertNull(((IntegerSpec) parsedColumnSpec.getDataTypeSpec()).getMaxValue());
        assertNull(((IntegerSpec) parsedColumnSpec.getDataTypeSpec()).getMinValue());
    }

    @Test
    public void testParseGenColumn() {
        GenColumnSpec genColumnSpec = new GenColumnSpec();
        genColumnSpec.setDataTypeSpec(new IntegerSpec());
        columnParser.parse(genColumnSpec);
        List<ColumnSpec> defColumnSpecs1 = columnParser.getColumns();
        setup();
        genColumnSpec = new GenColumnSpec();
        genColumnSpec.setDataTypeSpec(new IntegerSpec());
        columnParser.parse(genColumnSpec);
        List<ColumnSpec> defColumnSpecs2 = columnParser.getColumns();

        assertEquals(defColumnSpecs1.size(), defColumnSpecs2.size());
        for (int i = 0; i < defColumnSpecs1.size(); i++) {
            assertEquals(defColumnSpecs1.get(i).getColumnID(), defColumnSpecs2.get(i).getColumnID());
            assertTrue(defColumnSpecs1.get(i) instanceof DefColumnSpec);
            assertTrue(defColumnSpecs2.get(i) instanceof DefColumnSpec);
        }

    }

    @Test
    public void testParsePrimaryKey() {
        PrimaryKeySpec primaryKeySpec = new PrimaryKeySpec();
        IntegerSpec integerSpec = new IntegerSpec();
        integerSpec.setDefaultValue(0);
        primaryKeySpec.setDataTypeSpec(integerSpec);
        columnParser.parse(primaryKeySpec);
        PrimaryKeySpec parsedColumnSpec = (PrimaryKeySpec) columnParser.getColumns().get(0);

        assertNotNull(parsedColumnSpec.getRandomSeed());
        assertNull(((IntegerSpec) parsedColumnSpec.getDataTypeSpec()).getMaxValue());
        assertNull(((IntegerSpec) parsedColumnSpec.getDataTypeSpec()).getMinValue());
    }

    @Test
    public void testParseForeignKey() {
        DefForeignKeySpec defForeignKeySpec = new DefForeignKeySpec();
        columnParser.parse(defForeignKeySpec);
        DefForeignKeySpec parsedColumnSpec = (DefForeignKeySpec) columnParser.getColumns().get(0);

        assertNotNull(parsedColumnSpec.getRandomSeed());
    }

    @Test
    public void testParseGenForeignKey() {
        GenForeignKeySpec genForeignKeySpec = new GenForeignKeySpec();
        columnParser.parse(genForeignKeySpec);
        List<ColumnSpec> defColumnSpecs1 = columnParser.getColumns();
        setup();
        genForeignKeySpec = new GenForeignKeySpec();
        columnParser.parse(genForeignKeySpec);
        List<ColumnSpec> defColumnSpecs2 = columnParser.getColumns();

        assertEquals(defColumnSpecs1.size(), defColumnSpecs2.size());
        for (int i = 0; i < defColumnSpecs1.size(); i++) {
            assertEquals(defColumnSpecs1.get(i).getColumnID(), defColumnSpecs2.get(i).getColumnID());
            assertTrue(defColumnSpecs1.get(i) instanceof DefForeignKeySpec);
            assertTrue(defColumnSpecs2.get(i) instanceof DefForeignKeySpec);
        }

    }
}
