package dgen.parsertests;

import dgen.utils.parsers.DatabaseParser;
import dgen.utils.parsers.SpecificationException;
import dgen.utils.parsers.specs.*;
import dgen.utils.parsers.specs.datatypespecs.IntegerSpec;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNull;

public class DatabaseParserTest {

    DatabaseParser databaseParser;
    long randomSeed;

    @Before
    public void setup() {
        databaseParser = new DatabaseParser();
        randomSeed = 1;
    }

    @Test(expected = SpecificationException.class)
    public void testDatabaseValidate() {
        DatabaseSpec databaseSpec = new DatabaseSpec();
        databaseSpec.validate();
    }

    @Test
    public void testParseDatabase() {
        DefColumnSpec columnSpec = new DefColumnSpec();
        DefTableSpec tableSpec = new DefTableSpec();
        DatabaseSpec databaseSpec = new DatabaseSpec();
        List<ColumnSpec> columnSpecs = new ArrayList<>();
        List<TableSpec> tableSpecs = new ArrayList<>();

        columnSpec.setDataTypeSpec(new IntegerSpec());
        columnSpecs.add(columnSpec);
        tableSpec.setColumnSpecs(columnSpecs);
        tableSpecs.add(tableSpec);
        tableSpec.setNumRows(1);
        tableSpec.setTableName("hello world");
        databaseSpec.setTableSpecs(tableSpecs);
        databaseSpec.setDatabaseName("hello world");
        databaseSpec.setRandomSeed(randomSeed);

        databaseParser.parse(databaseSpec);
        DatabaseSpec parsedDatabase = databaseParser.getDatabase();
        assertNull(parsedDatabase.getTableSpecs().get(0).getRegexName());
    }
}
