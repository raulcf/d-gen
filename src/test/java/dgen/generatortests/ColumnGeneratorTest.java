package dgen.generatortests;

import dgen.column.Column;
import dgen.dataset.DatasetConfig;
import dgen.dataset.DatasetGenerator;
import dgen.utils.SpecificationParser;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ColumnGeneratorTest {

    SpecificationParser specificationParser;
    DatasetGenerator datasetGenerator;

    @Before
    public void setup() {
        specificationParser = new SpecificationParser();
    }

    @Test
    public void testNames() {
        Column column;

        specificationParser.parseYAML("src/test/java/dgen/generatortests/examples/1.yaml");
        datasetGenerator = DatasetConfig.specToGenerator(specificationParser.getDatabase());
        column = datasetGenerator.generateDataset().getTables().get(0).getColumns().get(0);
        assertEquals(column.getAttributeName(), "hello world");

        setup();
        specificationParser.parseYAML("src/test/java/dgen/generatortests/examples/2.yaml");
        datasetGenerator = DatasetConfig.specToGenerator(specificationParser.getDatabase());
        column = datasetGenerator.generateDataset().getTables().get(0).getColumns().get(0);
        Pattern pattern = Pattern.compile("[a-z]{2}");
        Matcher matcher = pattern.matcher(column.getAttributeName());
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
        Column column1;
        Column column2;

        specificationParser.parseYAML("src/test/java/dgen/generatortests/examples/5.yaml");
        datasetGenerator = DatasetConfig.specToGenerator(specificationParser.getDatabase());
        column1 = datasetGenerator.generateDataset().getTables().get(0).getColumns().get(0);

        setup();
        specificationParser.parseYAML("src/test/java/dgen/generatortests/examples/5.yaml");
        datasetGenerator = DatasetConfig.specToGenerator(specificationParser.getDatabase());
        column2 = datasetGenerator.generateDataset().getTables().get(0).getColumns().get(0);

        // Check to make sure generated columns are the same
        assertEquals(column1.getAttributeName(),column2.getAttributeName());
        assertEquals(column1.getData().size(), column2.getData().size());
        assertEquals(column1.getData().size(), 100);
        for (int i = 0; i < column2.getData().size(); i++) {
            assertEquals(column1.getData().get(i), column2.getData().get(i));
        }
    }
}
