package dgen.generatortests;

import dgen.attributegenerators.DefaultAttributeNameGenerator;
import dgen.column.ColumnGenerator;
import dgen.dataset.DatasetConfig;
import dgen.dataset.DatasetGenerator;
import dgen.datatypes.DataType;
import dgen.datatypes.IntegerType;
import dgen.datatypes.generators.DataTypeGenerator;
import dgen.tablerelationships.dependencyfunctions.funcdeps.FuncDepConfig;
import dgen.tablerelationships.dependencyfunctions.funcdeps.FuncDepGenerator;
import dgen.utils.parsers.SpecificationParser;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FuncDepGeneratorTest {

    SpecificationParser specificationParser;
    DatasetGenerator datasetGenerator;

    @Before
    public void setup() {
        specificationParser = new SpecificationParser();
    }

    @Test
    public void testValidate() {
        List<DataType> determinantData = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            determinantData.add(new IntegerType(0));
        }

        specificationParser.parseYAML("src/test/java/dgen/generatortests/examples/6.yaml");
        datasetGenerator = DatasetConfig.specToGenerator(specificationParser.getDatabase());
        ColumnGenerator columnGenerator = datasetGenerator.getColumnGenerator(1, 1);
        assertFalse(FuncDepGenerator.validate(determinantData, columnGenerator,
                new FuncDepConfig(new HashMap<>()), 10));

        determinantData = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            determinantData.add(new IntegerType(i));
        }
        specificationParser.parseYAML("src/test/java/dgen/generatortests/examples/6.yaml");
        datasetGenerator = DatasetConfig.specToGenerator(specificationParser.getDatabase());
        columnGenerator = datasetGenerator.getColumnGenerator(1, 1);
        assertTrue(FuncDepGenerator.validate(determinantData, columnGenerator,
                new FuncDepConfig(new HashMap<>()), 10));
    }

    @Test
    public void testFuncDepGenerator() {
        List<DataType> determinantData = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            determinantData.add(new IntegerType(i));
            determinantData.add(new IntegerType(i));
        }

        specificationParser.parseYAML("src/test/java/dgen/generatortests/examples/6.yaml");
        datasetGenerator = DatasetConfig.specToGenerator(specificationParser.getDatabase());
        ColumnGenerator columnGenerator = datasetGenerator.getColumnGenerator(1, 1);
        DataTypeGenerator dataTypeGenerator = columnGenerator.getDtg();
        FuncDepGenerator funcDepGenerator = new FuncDepGenerator(1, new FuncDepConfig(new HashMap<>()),
                dataTypeGenerator, determinantData, determinantData.size());
        ColumnGenerator dependant = new ColumnGenerator(funcDepGenerator,
                new DefaultAttributeNameGenerator("hello world"));
        assertTrue(FuncDepGenerator.validate(determinantData, dependant,
                new FuncDepConfig(new HashMap<>()), determinantData.size()));

    }
}
