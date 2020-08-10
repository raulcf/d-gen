package dgen;

import dgen.dataset.DatasetConfig;
import dgen.dataset.DatasetGenerator;
import dgen.utils.parsers.SpecificationParser;

/**
 * This class drives the data generation process, which is divided into 4 stages:
 *
 * 1. Parse the input profile and verify it can be satisfied
 * 2. Configure generators so as to satisfy the input profile
 * 3. Generate a profile of ground truth from the configured dataset
 * 4. Materialize the generation process into a specified output format
 *
 */
public class Main {


    public static void main(String[] args) {
        // Example
        SpecificationParser specificationParser = new SpecificationParser();
//        specificationParser.parseYAML("example_specifications/2_input.yaml");
//        specificationParser.write("example_specifications/2_output.json");
        specificationParser.parseYAML("test.yaml");
        specificationParser.write("test_output.json");
        DatasetGenerator datasetGenerator = DatasetConfig.specToGenerator(specificationParser.getDatabase());
//        Dataset dataset = datasetGenerator.generateDataset();


        System.out.println(datasetGenerator.generateDataset());


    }
}
