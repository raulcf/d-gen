package dgen;

import dgen.dataset.Dataset;
import dgen.dataset.DatasetConfig;
import dgen.dataset.DatasetGenerator;
import dgen.utils.parsers.SpecificationParser;
import dgen.utils.serialization.CSVSerializer;
//import dgen.utils.serialization.ParquetSerializer;
import dgen.utils.serialization.ParquetSerializer;
import dgen.utils.serialization.PostgresSerializer;
import org.apache.commons.io.FileUtils;

import java.io.File;

public class benchmark {

    public static void main(String[] args) throws Exception {
        File datasetPath = new File("11");
        File metadataPath = new File("test_dataset.json");
        FileUtils.deleteDirectory(datasetPath);
        metadataPath.delete();

        // Example
        long t0 = System.currentTimeMillis();
        SpecificationParser specificationParser = new SpecificationParser();
//        specificationParser.parseYAML("example_specifications/2_input.yaml");
//        specificationParser.write("example_specifications/2_output.json");
        specificationParser.parseYAML("test.yaml");
        specificationParser.write("test_output.json");
        DatasetGenerator datasetGenerator = DatasetConfig.specToGenerator(specificationParser.getDatabase());
        datasetGenerator.generateDataset();

//        serializer.serialize(".", "test_dataset.json");
        System.out.println("Finished in " + (System.currentTimeMillis() - t0));

//        long length = 0;
//        for (File file : datasetPath.listFiles()) {
//            length += file.length();
//        }

//        System.out.println((double) length / 1000000);


//        System.out.println(dataset.toString());
//        System.out.println(dataset.getTables().size());

    }
}
