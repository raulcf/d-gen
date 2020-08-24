package dgen.utils.serialization;

import dgen.column.ColumnConfig;
import dgen.coreconfig.DGException;
import dgen.dataset.Dataset;
import dgen.dataset.DatasetConfig;
import dgen.dataset.DatasetGenerator;
import dgen.datatypes.DataType;
import dgen.tables.Table;
import dgen.utils.parsers.SpecificationParser;
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
//
//    public CSVSerializer(String delimiter, String parentDirectory, String metadataOutputPath) {
//        this.delimiter = ",";
//        this.parentDirectory = parentDirectory;
//        this.metadataOutputPath = metadataOutputPath;
//    }

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
        Path path = Paths.get(datasetDirectory, fileName);

        try {
            FileWriter fileWriter = new FileWriter(datasetDirectory + "/" + fileName + ".csv");
            bufferedWriter = new BufferedWriter(fileWriter, 8 * 1024);
        } catch (Exception e) {
            throw new DGException(path.toString() + " already exists");
        }
    }

    @Override
    public void serializationSetup(String tableName, LinkedHashMap<Integer, String> columnNames, List<ColumnConfig> columnConfigs) throws IOException {
        bufferedWriter.write(String.join(",", columnNames.values()) + "\n");
    }

    @Override
    public void serialize(List<DataType> row, String tableName) throws IOException {
        List<String> values = row.stream().map(n -> n.value().toString()).collect(Collectors.toList());
        System.out.println(values);
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
    }

    /**
     * Writes a Dataset object to CSV files. Each table within the dataset will
     * be a .csv file and all .csv files will be in a folder named after the database.
     *
     * @param parentDir    Parent directory to place Dataset directory in.
     * @param metadataPath Path to write dataset metadata to.
     */
//    @Override
//    public void serialize(String parentDir, String metadataPath) throws Exception {
//        long t0 = System.currentTimeMillis();
//        FileWriter csvWriter;
//
//        String datasetDirPath = parentDir + "/" + dataset.getAttributeName();
//        File datasetDir = new File(datasetDirPath);
//
//        if (datasetDir.exists()) {
//            throw new Exception("Directory for " + datasetDirPath + " already exists");
//        }
//        datasetDir.mkdir();
//
//        for (Table table : dataset.getTables()) {
//            File csvFile = new File(datasetDirPath + "/" + table.getAttributeName() + ".csv");
//
//            if (csvFile.exists()) {
//                throw new Exception("File " + csvFile + " already exists");
//            }
//
//            csvWriter = new FileWriter(csvFile);
//            csvWriter.write(tableToCSV(table));
//
//            csvWriter.flush();
//            csvWriter.close();
//        }
//
//        Serializer.outputMetadata(metadataPath, dataset);
//        System.out.println("Serialized to csv in " + (System.currentTimeMillis() - t0));
//    }

    public String tableToCSV(Table table) {
        StringBuilder sb = new StringBuilder();
        ArrayList<ArrayList<DataType>> rows = table.getData();

//        sb.append(String.join(delimiter, table.getColumnNames()));
        sb.append("\n");

        for (ArrayList<DataType> row: rows) {
            ArrayList<String> data = new ArrayList<>();
            for (DataType dt: row) {
                data.add(dt.value().toString());
            }
            sb.append(String.join(delimiter, data));
            sb.append("\n");
        }

        return sb.toString();
    }

//    public String tableToCSV() {
//        StringBuffer sb = new StringBuffer();
//        List<Column> columns = table.getColumns();
//
//        // header
//        List<String> attributeNames = new ArrayList<>();
//        List<Iterator<DataType>> columnIterators = new ArrayList<>();
//        for (Column c : columns) {
//            // attribute data for header
//            String attrName = c.getAttributeName();
//            attributeNames.add(attrName);
//
//            // the rest of the records
//            Iterator<DataType> it = c.getData().iterator();
//            columnIterators.add(it);
//        }
//
//        String header = String.join(delimiter, attributeNames);
//        sb.append(header).append('\n');
//
//        boolean moreData = true;
//        while(moreData) {
//            List<String> recordValues = new ArrayList<>();
//            for (Iterator<DataType> it : columnIterators) {
//                if (! it.hasNext()) {
//                    moreData = false;
//                    continue;
//                }
//                DataType dt = it.next();
//                String value = dt.value().toString();
//                recordValues.add(value);
//            }
//            String record = String.join(delimiter, recordValues);
//            sb.append(record).append('\n');
//        }
//
//        return sb.toString();
//    }

//    public static void main(String[] args) throws Exception {
//        SpecificationParser specificationParser = new SpecificationParser();
//
//        specificationParser.parseYAML("test.yaml");
//        specificationParser.write("test_output.json");
//        DatasetGenerator datasetGenerator = DatasetConfig.specToGenerator(specificationParser.getDatabase());
//        Dataset dataset = datasetGenerator.generateDataset();
//
//        CSVSerializer csvSerializer = new CSVSerializer(dataset, ",");
////        csvSerializer.serialize("/Users/ryan/Downloads/");
//    }
}
