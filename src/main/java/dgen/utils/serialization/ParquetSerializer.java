package dgen.utils.serialization;

import dgen.column.ColumnConfig;
import dgen.dataset.Dataset;
import dgen.dataset.DatasetConfig;
import dgen.dataset.DatasetGenerator;
import dgen.datatypes.DataType;
import dgen.tables.Table;
import dgen.tables.TableConfig;
import dgen.utils.parsers.SpecificationParser;
import dgen.utils.parsers.specs.datatypespecs.DataTypeSpec;
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
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParquetSerializer {

    Dataset dataset;
    DatasetConfig datasetConfig;

    public ParquetSerializer(Dataset dataset) {
        this.dataset = dataset;
        this.datasetConfig = dataset.getDatasetConfig();
    }

    /**
     * Writes a Dataset object into Parquet files. Each table with the Dataset object will be a
     * .parquet file and all .parquet files will be in a folder named after the database.
     * @param parentDir Parent directory to place dataset directory in.
     * @param metadataPath Path to write dataset metadata to.
     */
//    @Override
    public void serialize(String parentDir, String metadataPath) throws Exception {

        for (TableConfig tableConfig: (List<TableConfig>) datasetConfig.getObject(DatasetConfig.TABLE_CONFIGS)) {
            Schema parquetSchema;
            Table table = dataset.getTable(tableConfig.getInt(TableConfig.TABLE_ID));
            parquetSchema = new Schema.Parser().parse(generateAvroSchema(tableConfig, table));
            List<GenericData.Record> parquetRecords = generateParquetRecords(parquetSchema, table);

            String pathName = parentDir + "/" + dataset.getAttributeName() + "/" + table.getAttributeName() + ".parquet";
            Path parquetPath = new Path(pathName);
            writeToParquet(parquetSchema, parquetRecords, parquetPath);
        }

        Serializer.outputMetadata(metadataPath, dataset);
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

    // FIXME: Avro doesn't allow field names to start with a number :(
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

    private String generateAvroSchema(TableConfig tableConfig, Table table) throws IOException {
        Map<String, Object> tableAvro = new HashMap<>();
        tableAvro.put("type", "record");
        tableAvro.put("name", fieldNameToAvroName(table.getAttributeName()));

        List<Map<String, Object>> columnAvros = new ArrayList<>();
        for (ColumnConfig columnConfig: (List<ColumnConfig>) tableConfig.getObject(TableConfig.COLUMN_CONFIGS)) {
            String columnName = table.getColumnName(columnConfig.getInt(ColumnConfig.COLUMN_ID));
            columnAvros.add(columnToAvro(columnConfig, columnName));
        }
        tableAvro.put("fields", columnAvros);

        return (new ObjectMapper().writeValueAsString(tableAvro));
    }

    private List<GenericData.Record> generateParquetRecords(Schema parquetSchema, Table table) {
        List<GenericData.Record> parquetRecords = new ArrayList<>();

        ArrayList<ArrayList<DataType>> rows = table.getData();
        ArrayList<String> attributeNames = new ArrayList<>();

//        for (String attributeName: table.getColumnNames()) {
//            attributeNames.add(fieldNameToAvroName(attributeName));
//        }


        for (ArrayList<DataType> row: rows) {
            GenericData.Record record = new GenericData.Record(parquetSchema);
            for (int i = 0; i < row.size(); i++) {
                record.put(attributeNames.get(i), row.get(i).value());
            }
            parquetRecords.add(record);
        }

        return parquetRecords;
    }

    public void writeToParquet(Schema avroSchema, List<GenericData.Record> recordsToWrite, Path fileToWrite) throws IOException {
        int blockSize = 1024 * 100;
        int pageSize = 65535 * 100;

        try (ParquetWriter<GenericData.Record> writer = AvroParquetWriter
                .<GenericData.Record>builder(fileToWrite)
                .withSchema(avroSchema)
                .withConf(new Configuration())
                .withCompressionCodec(CompressionCodecName.SNAPPY)
                .withPageSize(pageSize)
                .withRowGroupSize(blockSize)
                .build()) {
            for (GenericData.Record record : recordsToWrite) {
                writer.write(record);
            }
        }
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
