package dgen.generatortests;

import dgen.column.Column;
import dgen.coreconfig.DGException;
import dgen.dataset.DatasetConfig;
import dgen.dataset.DatasetGenerator;
import dgen.tables.Table;
import dgen.utils.parsers.SpecificationParser;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

public class TableGeneratorTest {

    SpecificationParser specificationParser;
    DatasetGenerator datasetGenerator;

    @Before
    public void setup() {
        specificationParser = new SpecificationParser();
    }

    @Test
    public void testNames() {
        Table table;

        specificationParser.parseYAML("src/test/java/dgen/generatortests/examples/6.yaml");
        datasetGenerator = DatasetConfig.specToGenerator(specificationParser.getDatabase());
        table = datasetGenerator.generateDataset().getTables().get(0);
        assertEquals(table.getAttributeName(), "hello world");

        setup();
        specificationParser.parseYAML("src/test/java/dgen/generatortests/examples/7.yaml");
        datasetGenerator = DatasetConfig.specToGenerator(specificationParser.getDatabase());
        table = datasetGenerator.generateDataset().getTables().get(0);
        Pattern pattern = Pattern.compile("[a-z]{2}");
        Matcher matcher = pattern.matcher(table.getAttributeName());
        assertTrue(matcher.find());
    }

    @Test
    public void testUnique() {
        Column column;

        specificationParser.parseYAML("src/test/java/dgen/generatortests/examples/3.yaml");
        datasetGenerator = DatasetConfig.specToGenerator(specificationParser.getDatabase());
        column = datasetGenerator.generateDataset().getTables().get(0).getColumns().get(0);
        assertEquals((new HashSet<>(column.getData())).size(), 10);

        setup();
        specificationParser.parseYAML("src/test/java/dgen/generatortests/examples/4.yaml");
        datasetGenerator = DatasetConfig.specToGenerator(specificationParser.getDatabase());
        column = datasetGenerator.generateDataset().getTables().get(0).getColumns().get(0);
        assertTrue((new HashSet<>(column.getData())).size() <= 10);
    }

    @Test
    public void testColumnGenerator() {
        Table table1;
        Table table2;

        specificationParser.parseYAML("src/test/java/dgen/generatortests/examples/5.yaml");
        datasetGenerator = DatasetConfig.specToGenerator(specificationParser.getDatabase());
        table1 = datasetGenerator.generateDataset().getTables().get(0);

        setup();
        specificationParser.parseYAML("src/test/java/dgen/generatortests/examples/5.yaml");
        datasetGenerator = DatasetConfig.specToGenerator(specificationParser.getDatabase());
        table2 = datasetGenerator.generateDataset().getTables().get(0);

        // Check to make sure generated columns are the same
        assertEquals(table1.getAttributeName(),table2.getAttributeName());
        assertEquals(table1.getColumns().size(), table2.getColumns().size());
        assertEquals(table1.getColumns().size(), specificationParser.getDatabase().getTableSpecs().size());

        Column column1;
        Column column2;
        for (int i = 0; i < table1.getColumns().size(); i++) {
            column1 = table1.getColumns().get(i);
            column2 = table2.getColumns().get(i);

            assertEquals(column1.getAttributeName(), column2.getAttributeName());
            assertEquals(column1.getData().size(), column2.getData().size());
            for (int j = 0; j < column2.getData().size(); j++) {
                assertEquals(column1.getData().get(j), column2.getData().get(j));
            }
        }
    }

    @Test
    public void testTableRelationships() {
        specificationParser.parseYAML("src/test/java/dgen/generatortests/examples/9.yaml");
        try {
            datasetGenerator = DatasetConfig.specToGenerator(specificationParser.getDatabase());
            fail("Failed to catch incompatible cyclic dependency");
        } catch (DGException e) {}

        setup();
        specificationParser.parseYAML("src/test/java/dgen/generatortests/examples/10.yaml");
        datasetGenerator = DatasetConfig.specToGenerator(specificationParser.getDatabase());

    }
}
