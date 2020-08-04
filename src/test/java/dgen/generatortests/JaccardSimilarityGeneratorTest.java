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
import dgen.tablerelationships.dependencyfunctions.jaccardsimilarity.JaccardSimilarityConfig;
import dgen.tablerelationships.dependencyfunctions.jaccardsimilarity.JaccardSimilarityGenerator;
import dgen.utils.SpecificationParser;
import dgen.utils.specs.relationships.dependencyFunctions.JaccardSimilarity;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class JaccardSimilarityGeneratorTest {

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
            determinantData.add(new IntegerType(-1));
        }
        specificationParser.parseYAML("src/test/java/dgen/generatortests/examples/6.yaml");
        datasetGenerator = DatasetConfig.specToGenerator(specificationParser.getDatabase());
        ColumnGenerator columnGenerator = datasetGenerator.getColumnGenerator(1, 1);
        Map<Object, Object> originals = new HashMap<>();
        originals.put("similarity", 0.5f);
        JaccardSimilarityConfig jaccardSimilarityConfig = new JaccardSimilarityConfig(originals);
        assertFalse(JaccardSimilarityGenerator.validate(determinantData, columnGenerator,
                jaccardSimilarityConfig, determinantData.size()));

        determinantData = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            determinantData.add(new IntegerType(i));
            determinantData.add(new IntegerType(-1));
        }
        specificationParser.parseYAML("src/test/java/dgen/generatortests/examples/6.yaml");
        datasetGenerator = DatasetConfig.specToGenerator(specificationParser.getDatabase());
        columnGenerator = datasetGenerator.getColumnGenerator(1, 1);
        originals = new HashMap<>();
        originals.put("similarity", 0.45f);
        jaccardSimilarityConfig = new JaccardSimilarityConfig(originals);
        assertTrue(JaccardSimilarityGenerator.validate(determinantData, columnGenerator,
                jaccardSimilarityConfig, 10));
    }


}
