package dgen;

import dgen.dataset.Dataset;
import dgen.dataset.DatasetConfig;
import dgen.dataset.DatasetGenerator;
import dgen.utils.parsers.SpecificationParser;
import dgen.utils.serialization.CSVSerializer;
import dgen.utils.serialization.ParquetSerializer;
import dgen.utils.serialization.PostgresSerializer;
import dgen.utils.serialization.Serializer;
import org.apache.commons.cli.*;
import org.apache.commons.io.FileUtils;
import org.apache.zookeeper.Op;

import java.io.File;
import java.io.IOException;

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

    public static void benchmark(String[] args) throws Exception {
        // Example
        long t0 = System.currentTimeMillis();
        SpecificationParser specificationParser = new SpecificationParser();
//        specificationParser.parseYAML("example_specifications/2_input.yaml");
//        specificationParser.write("example_specifications/2_output.json");
        specificationParser.parseYAML("test.yaml");
        specificationParser.write("test_output.json");
        DatasetGenerator datasetGenerator = DatasetConfig.specToGenerator(specificationParser.getDatabase());
        Dataset dataset = datasetGenerator.generateDataset();

        CSVSerializer serializer = new CSVSerializer(dataset);

        File datasetPath = new File("11");
        File metadataPath = new File("test_dataset.json");
        FileUtils.deleteDirectory(datasetPath);
        metadataPath.delete();

        serializer.serialize(".", "test_dataset.json");
        System.out.println("Finished in " + (System.currentTimeMillis() - t0));

        long length = 0;
        for (File file : datasetPath.listFiles()) {
            length += file.length();
        }

        System.out.println((double) length / 1000000);


//        System.out.println(dataset.toString());
//        System.out.println(dataset.getTables().size());


    }

    public static void main(String[] args) throws Exception {
        Options options = new Options();

        Option specPath = new Option("i", "input", true, "input specification path");
        specPath.setRequired(true);
        options.addOption(specPath);

        Option specOutputPath = new Option("so", "spec-output", true, "low-level spec output path");
        options.addOption(specOutputPath);

        Option databaseOutput = new Option("o", "output", true, "output database path");
        databaseOutput.setRequired(true);
        options.addOption(databaseOutput);

        Option serializer = new Option("s", "serializer", true, "serializer type (csv, parquet, postgres");
        serializer.setRequired(true);
        options.addOption(serializer);

        Option metadataOutput = new Option("m", "metadata-output", true, "metadata output file path");
        metadataOutput.setRequired(true);
        options.addOption(metadataOutput);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        String specPathValue = cmd.getOptionValue("input");
        String databaseOutputValue = cmd.getOptionValue("output");
        String serializerValue = cmd.getOptionValue("serializer");
        String metadataOutputValue = cmd.getOptionValue("metadata-output");
        String specOutputPathValue = cmd.getOptionValue("spec-output");

        SpecificationParser specificationParser = new SpecificationParser();
        specificationParser.parseYAML(specPathValue);

        if (specOutputPathValue != null) {
            specificationParser.write("test_output.json");
        }
        DatasetGenerator datasetGenerator = DatasetConfig.specToGenerator(specificationParser.getDatabase());
        Dataset dataset = datasetGenerator.generateDataset();

        Serializer databaseSerializer;
        switch (serializerValue) {
            case ("csv"):
                databaseSerializer = new CSVSerializer(dataset);
                break;
            case ("postgres"):
                databaseSerializer = new PostgresSerializer(dataset);
                break;
            case ("parquet"):
                databaseSerializer = new ParquetSerializer(dataset);
                break;
            default:
                throw new Exception("Invalid serializer type");
        }

        databaseSerializer.serialize(databaseOutputValue, metadataOutputValue);
    }
}
