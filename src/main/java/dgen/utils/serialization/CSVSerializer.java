package dgen.utils.serialization;

import dgen.column.ColumnConfig;
import dgen.coreconfig.DGException;
import dgen.dataset.DatasetConfig;
import dgen.datatypes.DataType;
import dgen.utils.parsers.specs.serializerspecs.Serializers;
import dgen.utils.serialization.config.CSVConfig;
import dgen.utils.serialization.config.SerializerConfig;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class CSVSerializer implements Serializer {

    private String delimiter;
    private String parentDirectory;
    private String metadataOutputPath;
    private String datasetDirectory;
    private BufferedWriter bufferedWriter;

    public CSVSerializer(CSVConfig csvConfig) {
        this.delimiter = csvConfig.getString(CSVConfig.DELIMITER);
        this.parentDirectory = csvConfig.getString(SerializerConfig.PARENT_DIRECTORY);
        this.metadataOutputPath = csvConfig.getString(SerializerConfig.METADATA_OUTPUT_PATH);
    }

    @Override
    public Serializers serializerType() { return Serializers.CSV; }

    @Override
    public void directorySetup(String directoryName) {
         Path path = Paths.get(parentDirectory, directoryName);
         datasetDirectory = path.toString();

        try {
            Files.createDirectory(path);
        } catch (Exception e) {
            throw new DGException(path.toString() + " already exists");
        }

    }

    @Override
    public void fileSetup(String fileName) throws IOException {
        File file = new File(datasetDirectory + "/" + fileName + ".csv");

        if (file.exists()) {
            throw new DGException(file.toString() + " already exists");
        }

        FileWriter fileWriter = new FileWriter(file);
        bufferedWriter = new BufferedWriter(fileWriter, 8 * 1024);
    }

    @Override
    public void serializationSetup(String tableName, LinkedHashMap<Integer, String> columnNames, List<ColumnConfig> columnConfigs) throws IOException {
        bufferedWriter.write(String.join(",", columnNames.values()) + "\n");
    }

    @Override
    public void serialize(List<DataType> row, String tableName) throws IOException {
        List<String> values = row.stream().map(n -> n.value().toString()).collect(Collectors.toList());
        bufferedWriter.write(String.join(delimiter, values) + "\n");
    }

    @Override
    public void postSerialization() throws IOException {
        bufferedWriter.flush();
        bufferedWriter.close();
    }

    @Override
    public void cleanup(DatasetConfig datasetConfig, Map<Integer, String> tableNames,
                        Map<Integer, Map<Integer, String>> columnNames) throws IOException {
        Serializer.outputMetadata(metadataOutputPath, tableNames, columnNames);
    }
}
