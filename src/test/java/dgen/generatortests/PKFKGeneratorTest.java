package dgen.generatortests;

import dgen.column.Column;
import dgen.column.ColumnGenerator;
import dgen.coreconfig.DGException;
import dgen.dataset.DatasetConfig;
import dgen.dataset.DatasetGenerator;
import dgen.datatypes.DataType;
import dgen.datatypes.IntegerType;
import dgen.pkfk.FKGenerator;
import dgen.utils.SpecificationParser;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class PKFKGeneratorTest {

    SpecificationParser specificationParser;
    DatasetGenerator datasetGenerator;
    long randomSeed;

    @Before
    public void setup() {
        specificationParser = new SpecificationParser();
        randomSeed = 1;
    }

    @Test
    public void testValidate() {
        List<DataType> determinantData = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            determinantData.add(new IntegerType(i));
        }
        specificationParser.parseYAML("src/test/java/dgen/generatortests/examples/6.yaml");
        datasetGenerator = DatasetConfig.specToGenerator(specificationParser.getDatabase());
        ColumnGenerator columnGenerator = datasetGenerator.getColumnGenerator(1, 1);

        try {
            FKGenerator.validate(determinantData, columnGenerator, 10);
            fail("Failed to catch incompatible ColumnGenerator");
        } catch (DGException e) { }

        determinantData = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            determinantData.add(new IntegerType(i));
        }
        specificationParser.parseYAML("src/test/java/dgen/generatortests/examples/6.yaml");
        datasetGenerator = DatasetConfig.specToGenerator(specificationParser.getDatabase());
        columnGenerator = datasetGenerator.getColumnGenerator(1, 1);

        try {
            FKGenerator.validate(determinantData, columnGenerator, 10);
        } catch (DGException e) {
            fail("Validate failed when it should have passed");
        }
    }

    @Test
    public void testFKGenerator() {
        specificationParser.parseYAML("src/test/java/dgen/generatortests/examples/8.yaml");
        datasetGenerator = DatasetConfig.specToGenerator(specificationParser.getDatabase());
        ColumnGenerator pkColumn = datasetGenerator.getColumnGenerator(1, 1);
        List<DataType> pkValues = pkColumn.generateColumn(1000).getData();
        ColumnGenerator fkColumn1 = datasetGenerator.getColumnGenerator(2, 2);
        FKGenerator.validate(pkValues, datasetGenerator.getColumnGenerator(2, 2), 1000);

        setup();
        specificationParser.parseYAML("src/test/java/dgen/generatortests/examples/8.yaml");
        datasetGenerator = DatasetConfig.specToGenerator(specificationParser.getDatabase());
        pkColumn = datasetGenerator.getColumnGenerator(1, 1);
        pkValues = pkColumn.generateColumn(1000).getData();
        ColumnGenerator fkColumn2 = datasetGenerator.getColumnGenerator(2, 2);
        FKGenerator.validate(pkValues, datasetGenerator.getColumnGenerator(2, 2), 1000);

        Column parsedColumn1 = fkColumn1.generateColumn(1000);
        Column parsedColumn2 = fkColumn2.generateColumn(1000);

        assertEquals(parsedColumn1.getData().size(), parsedColumn2.getData().size());
        for (int i = 0; i < 1000; i++) {
            assertEquals(parsedColumn1.getData().get(i), parsedColumn2.getData().get(i));
        }

    }
}
