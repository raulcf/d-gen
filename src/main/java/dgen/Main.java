package dgen;

import dgen.dataset.DatasetConfig;
import dgen.dataset.DatasetGenerator;
import dgen.utils.parsers.SpecificationParser;
import org.apache.commons.cli.*;

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

    public static void main(String[] args) throws Exception {
        Options options = new Options();

        Option specPath = new Option("i", "input", true, "input specification path");
        specPath.setRequired(true);
        options.addOption(specPath);

        Option specOutputPath = new Option("o", "output", true, "low-level spec output path");
        options.addOption(specOutputPath);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        String specPathValue = cmd.getOptionValue("input");
        String specOutputPathValue = cmd.getOptionValue("spec-output");

        SpecificationParser specificationParser = new SpecificationParser();
        specificationParser.parseYAML(specPathValue);

        if (specOutputPathValue != null) {
            specificationParser.write(specOutputPathValue);
        }
        DatasetGenerator datasetGenerator = DatasetConfig.specToGenerator(specificationParser.getDatabase());

        datasetGenerator.generateDataset();
    }
}
