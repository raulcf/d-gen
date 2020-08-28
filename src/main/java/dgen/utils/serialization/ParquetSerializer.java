package dgen.utils.serialization;

import dgen.column.ColumnConfig;
import dgen.coreconfig.DGException;
import dgen.dataset.DatasetConfig;
import dgen.dataset.DatasetGenerator;
import dgen.datatypes.DataType;
import dgen.utils.parsers.SpecificationParser;
import dgen.utils.parsers.specs.datatypespecs.DataTypeSpec;
import dgen.utils.parsers.specs.serializerspecs.Serializers;
import dgen.utils.serialization.config.ParquetConfig;
import dgen.utils.serialization.config.SerializerConfig;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.parquet.avro.AvroParquetReader;
import org.apache.parquet.avro.AvroParquetWriter;
import org.apache.parquet.hadoop.ParquetReader;
import org.apache.parquet.hadoop.ParquetWriter;
import org.apache.parquet.hadoop.metadata.CompressionCodecName;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ParquetSerializer implements Serializer {

    private String parentDirectory;
    private String metadataOutputPath;
    private String datasetDirectory;
    private Schema avroTableSchema;
    private ParquetWriter parquetWriter;
    private List<String> columnNames;

    public ParquetSerializer(ParquetConfig parquetConfig) {
        this.parentDirectory = parquetConfig.getString(SerializerConfig.PARENT_DIRECTORY);
        this.metadataOutputPath = parquetConfig.getString(SerializerConfig.METADATA_OUTPUT_PATH);
    }

    @Override
    public Serializers serializerType() { return Serializers.PARQUET; }

    @Override
    public void directorySetup(String directoryName) {
        java.nio.file.Path path = Paths.get(parentDirectory, directoryName);
        datasetDirectory = path.toString();

        try {
            Files.createDirectory(path);
        } catch (Exception e) {
            throw new DGException(path.toString() + " already exists");
        }

    }

    @Override
    public void fileSetup(String fileName) throws IOException {
    }

    @Override
    public void serializationSetup(String tableName, LinkedHashMap<Integer, String> columnNames, List<ColumnConfig> columnConfigs) throws IOException {
        avroTableSchema = new Schema.Parser().parse(generateAvroSchema(columnConfigs, columnNames, tableName));
        parquetWriter = AvroParquetWriter
                .<GenericData.Record>builder(new Path(datasetDirectory + "/" + tableName + ".parquet"))
                .withSchema(avroTableSchema)
                .withConf(new Configuration())
                .withCompressionCodec(CompressionCodecName.SNAPPY)
                .build();
        this.columnNames = columnNames.values().stream().map(n -> fieldNameToAvroName(n)).collect(Collectors.toList());;
    }

    @Override
    public void serialize(List<DataType> row, String tableName) throws IOException {
        GenericData.Record record = new GenericData.Record(avroTableSchema);
        for (int i = 0; i < row.size(); i++) {
            record.put(columnNames.get(i), row.get(i).value());
        }

        parquetWriter.write(record);
    }

    @Override
    public void postSerialization() throws IOException {
        parquetWriter.close();
        parquetWriter = null;
    }

    @Override
    public void cleanup(DatasetConfig datasetConfig, Map<Integer, String> tableNames,
                        Map<Integer, Map<Integer, String>> columnNames) throws IOException {
        Serializer.outputMetadata(metadataOutputPath, tableNames, columnNames);
    }

    private String dataTypeToAvro(DataTypeSpec dataTypeSpec) {
        switch (dataTypeSpec.type()) {
            case INT:
                return "int";
            case FLOAT:
                return "float";
            case BOOLEAN:
                return "boolean";
            case STRING:
                return "string";
        }

        return null;
    }

    // FIXME: Avro doesn't allow field names to start with a number :(. Name changes also aren't reflected in metadata
    private String fieldNameToAvroName(String name) {
        Pattern pattern = Pattern.compile("[A-Za-z_]");
        Matcher matcher = pattern.matcher(String.valueOf(name.charAt(0)));

        if (matcher.find()) {
            return name;
        } else {
            return "_" + name;
        }

    }

    private Map<String, Object> columnToAvro(ColumnConfig columnConfig, String columnName) {
        Map<String, Object> columnAvro = new HashMap<>();
        columnAvro.put("name", fieldNameToAvroName(columnName));

        if (columnConfig.getBoolean(ColumnConfig.HAS_NULL)) {
            List<String> dataType = new ArrayList<>();
            dataType.add(dataTypeToAvro((DataTypeSpec) columnConfig.getObject(ColumnConfig.DATATYPE)));
            dataType.add("null");
            columnAvro.put("type", dataType);
        } else {
            columnAvro.put("type", dataTypeToAvro((DataTypeSpec) columnConfig.getObject(ColumnConfig.DATATYPE)));
        }


        return columnAvro;
    }

    private String generateAvroSchema(List<ColumnConfig> columnConfigs, LinkedHashMap<Integer, String> columnNames,
                                      String tableName) throws IOException {
        Map<String, Object> tableAvro = new HashMap<>();
        tableAvro.put("type", "record");
        tableAvro.put("name", fieldNameToAvroName(tableName));

        List<Map<String, Object>> columnAvros = new ArrayList<>();
        for (ColumnConfig columnConfig: columnConfigs) {
            String columnName = columnNames.get(columnConfig.getInt(ColumnConfig.COLUMN_ID));
            columnAvros.add(columnToAvro(columnConfig, columnName));
        }
        tableAvro.put("fields", columnAvros);

        return (new ObjectMapper().writeValueAsString(tableAvro));
    }

    // Method for testing
    public void readFromParquet(Path filePathToRead) throws IOException {
        try (ParquetReader<GenericData.Record> reader = AvroParquetReader
                .<GenericData.Record>builder(filePathToRead)
                .withConf(new Configuration())
                .build()) {

            GenericData.Record record;
            while ((record = reader.read()) != null) {
                System.out.println(record);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        SpecificationParser specificationParser = new SpecificationParser();

        specificationParser.parseYAML("test.yaml");
        specificationParser.write("test_output.json");
        DatasetGenerator datasetGenerator = DatasetConfig.specToGenerator(specificationParser.getDatabase());
//        Dataset dataset = datasetGenerator.generateDataset();

//        ParquetSerializer parquetSerializer = new ParquetSerializer(dataset);
//        parquetSerializer.serialize("/Users/ryan/Documents/test");
    }
}
