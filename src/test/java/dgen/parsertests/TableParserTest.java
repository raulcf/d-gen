package dgen.parsertests;

import dgen.utils.RandomGenerator;
import dgen.utils.SpecificationException;
import dgen.utils.TableParser;
import dgen.utils.specs.*;
import dgen.utils.specs.datatypespecs.IntegerSpec;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TableParserTest {

    TableParser tableParser;
    long randomSeed;

    @Before
    public void setup() {
        randomSeed = 1;
        tableParser = new TableParser(new RandomGenerator(randomSeed));
    }

    @Test
    public void testParseTableName() {
        // Checks to make sure random name and regex name are false and null
        DefTableSpec tableSpec = new DefTableSpec();
        tableSpec.setTableName("hello world");
        tableSpec.setNumRows(1);
        tableSpec.setColumnSpecs(new ArrayList<>());
        tableParser.parse(tableSpec);
        DefTableSpec parsedTableSpec = (DefTableSpec) tableParser.getTables().get(0);
        assertNull(parsedTableSpec.getRegexName());
        assertFalse(parsedTableSpec.isRandomName());

        // Checks to make sure random name is false
        setup();
        tableSpec = new DefTableSpec();
        tableSpec.setRegexName("[a-z]{2}");
        tableSpec.setNumRows(1);
        tableSpec.setColumnSpecs(new ArrayList<>());
        tableParser.parse(tableSpec);
        parsedTableSpec = (DefTableSpec) tableParser.getTables().get(0);
        assertFalse(parsedTableSpec.isRandomName());
    }

    @Test(expected = SpecificationException.class)
    public void testCheckTableID() {
        // Checks to make sure duplicate columnIDs aren't allowed
        DefTableSpec tableSpec1 = new DefTableSpec();
        DefTableSpec tableSpec2 = new DefTableSpec();
        tableSpec1.setColumnSpecs(new ArrayList<>());
        tableSpec2.setColumnSpecs(new ArrayList<>());
        tableSpec1.setNumRows(1);
        tableSpec2.setNumRows(1);

        tableParser.parse(tableSpec1);
        tableParser.parse(tableSpec2);
    }

    @Test(expected = SpecificationException.class)
    public void testTableValidate() {
        DefTableSpec tableSpec = new DefTableSpec();
        tableSpec.setNumRows(-1);
        tableSpec.validate();
    }

    @Test
    public void testParseTable() {
        DefTableSpec tableSpec = new DefTableSpec();
        DefColumnSpec columnSpec = new DefColumnSpec();
        List<ColumnSpec> columnSpecs = new ArrayList<>();

        columnSpec.setColumnName("hello world");
        columnSpec.setDataTypeSpec(new IntegerSpec());
        columnSpecs.add(columnSpec);
        tableSpec.setColumnSpecs(columnSpecs);
        tableSpec.setNumRows(1);

        tableParser.parse(tableSpec);
        DefTableSpec parsedTableSpec = (DefTableSpec) tableParser.getTables().get(0);

        assertNotNull(parsedTableSpec.getRandomSeed());
        assertNotNull(parsedTableSpec.getTableID());
        assertNull(parsedTableSpec.getColumnSpecs().get(0).getRegexName());
    }

    @Test
    public void testParseGenTable() {
        GenTableSpec genTableSpec = new GenTableSpec();
        DefColumnSpec defColumnSpec = new DefColumnSpec();
        List<ColumnSpec> columnSpecs = new ArrayList<>();

        defColumnSpec.setDataTypeSpec(new IntegerSpec());
        columnSpecs.add(defColumnSpec);
        genTableSpec.setColumnSpecs(columnSpecs);

        tableParser.parse(genTableSpec);
        List<TableSpec> defTableSpecs1 = tableParser.getTables();
        setup();

        genTableSpec = new GenTableSpec();
        defColumnSpec = new DefColumnSpec();
        columnSpecs = new ArrayList<>();

        defColumnSpec.setDataTypeSpec(new IntegerSpec());
        columnSpecs.add(defColumnSpec);
        genTableSpec.setColumnSpecs(columnSpecs);

        tableParser.parse(genTableSpec);
        List<TableSpec> defTableSpecs2 = tableParser.getTables();

        assertEquals(defTableSpecs1.size(), defTableSpecs2.size());
        for (int i = 0; i < defTableSpecs1.size(); i++) {
            assertEquals(defTableSpecs1.get(i).getTableID(), defTableSpecs2.get(i).getTableID());
            assertEquals(defTableSpecs1.get(i).getNumRows(), defTableSpecs2.get(i).getNumRows());
            assertTrue(defTableSpecs1.get(i) instanceof DefTableSpec);
            assertTrue(defTableSpecs2.get(i) instanceof DefTableSpec);
        }
    }
}
