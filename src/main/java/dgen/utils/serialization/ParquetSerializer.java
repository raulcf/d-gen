package dgen.utils.serialization;

import dgen.column.Column;
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

public class ParquetSerializer implements Serializer {

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
     */
    @Override
    public void serialize(String parentDir) throws Exception {
        for (TableConfig tableConfig: (List<TableConfig>) datasetConfig.getObject(DatasetConfig.TABLE_CONFIGS)) {
            Schema parquetSchema;
            Table table = dataset.getTable(tableConfig.getInt(TableConfig.TABLE_ID));
            parquetSchema = new Schema.Parser().parse(generateAvroSchema(tableConfig, table));
            List<GenericData.Record> parquetRecords = generateParquetRecords(parquetSchema, table);

            String pathName = parentDir + "/" + dataset.getAttributeName() + "/" + table.getAttributeName() + ".parquet";
            Path parquetPath = new Path(pathName);
            writeToParquet(parquetSchema, parquetRecords, parquetPath);
        }

        Serializer.outputMetadata(parentDir, dataset);
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
    private String cleanFieldName(String name) {
        Pattern pattern = Pattern.compile("[A-Za-z_]");
        Matcher matcher = pattern.matcher(String.valueOf(name.charAt(0)));

        if (matcher.find()) {
            return name;
        } else {
            return "_" + name;
        }

    }

    private Map<String, Object> columnToAvro(ColumnConfig columnConfig, Column column) {
        Map<String, Object> columnAvro = new HashMap<>();
        columnAvro.put("name", cleanFieldName(column.getAttributeName()));

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
        tableAvro.put("name", cleanFieldName(table.getAttributeName()));

        List<Map<String, Object>> columnAvros = new ArrayList<>();
        for (ColumnConfig columnConfig: (List<ColumnConfig>) tableConfig.getObject(TableConfig.COLUMN_CONFIGS)) {
            Column column = table.getColumn(columnConfig.getInt(ColumnConfig.COLUMN_ID));
            columnAvros.add(columnToAvro(columnConfig, column));
        }
        tableAvro.put("fields", columnAvros);

        return (new ObjectMapper().writeValueAsString(tableAvro));
    }

    private List<GenericData.Record> generateParquetRecords(Schema parquetSchema, Table table) {
        List<GenericData.Record> parquetRecords = new ArrayList<>();
        List<Column> columns = table.getColumns();
        int numColumns = columns.size();

        // header
        List<String> attributeNames = new ArrayList<>();
        List<Iterator<DataType>> columnIterators = new ArrayList<>();
        for (Column c : columns) {
            // attribute data for header
            String attrName = c.getAttributeName();
            attributeNames.add(cleanFieldName(attrName));

            // the rest of the records
            Iterator<DataType> it = c.getData().iterator();
            columnIterators.add(it);
        }

        boolean moreData = true;
        while(moreData) {
            GenericData.Record record = new GenericData.Record(parquetSchema);
            for (int i = 0; i < numColumns; i++) {
                Iterator<DataType> it = columnIterators.get(i);
                String attributeName = attributeNames.get(i);
                if (! it.hasNext()) {
                    moreData = false;
                    continue;
                }
                DataType dt = it.next();
                Object value = dt.value();
                record.put(attributeName, value);
            }

            parquetRecords.add(record);
        }

        parquetRecords.remove(parquetRecords.size() - 1);
        return parquetRecords;
    }

    public void writeToParquet(Schema parquetSchema, List<GenericData.Record> recordsToWrite, Path fileToWrite) throws IOException {
        try (ParquetWriter<GenericData.Record> writer = AvroParquetWriter
                .<GenericData.Record>builder(fileToWrite)
                .withSchema(parquetSchema)
                .withConf(new Configuration())
                .withCompressionCodec(CompressionCodecName.SNAPPY)
                .build()) {

            for (GenericData.Record record : recordsToWrite) {
                writer.write(record);
            }
        }
    }

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
        Dataset dataset = datasetGenerator.generateDataset();

        ParquetSerializer parquetSerializer = new ParquetSerializer(dataset);
        parquetSerializer.serialize("/Users/ryan/Documents/test");
    }
}
