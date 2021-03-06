package dgen.utils.serialization;

import dgen.column.ColumnConfig;
import dgen.coreconfig.DGException;
import dgen.dataset.DatasetConfig;
import dgen.dataset.DatasetGenerator;
import dgen.datatypes.DataType;
import dgen.datatypes.NativeType;
import dgen.tables.Table;
import dgen.tables.TableConfig;
import dgen.utils.parsers.SpecificationParser;
import dgen.utils.parsers.specs.datatypespecs.DataTypeSpec;
import dgen.utils.parsers.specs.serializerspecs.Serializers;
import dgen.utils.serialization.config.PostgresConfig;
import dgen.utils.serialization.config.SerializerConfig;
import org.javatuples.Pair;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class PostgresSerializer implements Serializer {

    String databaseName;
    String parentDirectory;
    String metadataOutputPath;
    BufferedWriter bufferedWriter;


    public PostgresSerializer(PostgresConfig postgresConfig) {
        this.parentDirectory = postgresConfig.getString(SerializerConfig.PARENT_DIRECTORY);
        this.metadataOutputPath = postgresConfig.getString(SerializerConfig.METADATA_OUTPUT_PATH);
    }

    @Override
    public Serializers serializerType() { return Serializers.POSTGRES; }

    @Override
    public void directorySetup(String directoryName) {
        databaseName = directoryName;

        Path path = Paths.get(parentDirectory, databaseName);

        try {
            FileWriter fileWriter = new FileWriter(parentDirectory + "/" + databaseName);
            bufferedWriter = new BufferedWriter(fileWriter, 8 * 1024);
            bufferedWriter.write("CREATE SCHEMA \"" + databaseName + "\"" + "\n\n");
        } catch (Exception e) {
            throw new DGException(path.toString() + " already exists");
        }
    }

    @Override
    public void fileSetup(String fileName) {

    }

    @Override
    public void serializationSetup(String tableName, LinkedHashMap<Integer, String> columnNames, List<ColumnConfig> columnConfigs) throws IOException {
        StringBuilder statement = new StringBuilder();

        statement.append("CREATE TABLE ");
        statement.append("\"" + databaseName + "\".\"" + tableName + "\"" + " (\n");

        for (ColumnConfig columnConfig: columnConfigs) {
            String columnName = columnNames.get(columnConfig.getInt(ColumnConfig.COLUMN_ID));
            statement.append("\t");
            statement.append(columnConfigToStatement(columnConfig, columnName));
            statement.append(",\n");
        }
        statement.deleteCharAt(statement.length() - 2);

        statement.append(");\n");
        System.out.println(statement.toString());
        bufferedWriter.write(statement.toString());
        bufferedWriter.flush();
    }

    @Override
    public void serialize(List<DataType> row, String tableName) throws Exception {
        StringBuilder statement = new StringBuilder();

        // FIXME: this method is called on every row and a new "INSERT INTO" statement is created even though it's unnecessary
        // Not sure how to fix given this setup right now.
        statement.append("INSERT INTO ");
        statement.append("\"" + databaseName + "\".\"" + tableName + "\"");
        statement.append(" VALUES\n");

        List<String> values = new ArrayList<>();
        for (DataType dt: row) {
            if (dt.nativeType() == NativeType.STRING) {
                values.add("'" + dt.value() + "'");
            } else {
                values.add(dt.value().toString());
            }
        }

        statement.append("(" + String.join(",", values) + ")");
        statement.append("\n;\n");

        bufferedWriter.write(statement.toString());
    }

    @Override
    public void postSerialization() {}

    @Override
    public void cleanup(DatasetConfig datasetConfig, Map<Integer, String> tableNames,
                        Map<Integer, Map<Integer, String>> columnNames) throws IOException {

        Map<Pair<Integer, Integer>, Set<Pair<Integer, Integer>>> pkfkMappings = (Map<Pair<Integer, Integer>, Set<Pair<Integer, Integer>>>) datasetConfig.getObject("pk.fk.mappings");
        for (Pair<Integer, Integer> pk : pkfkMappings.keySet()) {
            StringBuilder statement = new StringBuilder();

            String pkColumnName = columnNames.get(pk.getValue0()).get(pk.getValue1());
            String pkTableName = tableNames.get(pk.getValue0());

            statement.append(primaryKeyToStatement(pkTableName, pkColumnName));
            statement.append("\n\n");

            for (Pair<Integer, Integer> fk : pkfkMappings.get(pk)) {
                String fkColumnName = columnNames.get(fk.getValue0()).get(fk.getValue1());
                String fkTableName = tableNames.get(fk.getValue0());

                statement.append(foreignKeyToStatement(pkTableName, pkColumnName, fkTableName, fkColumnName));
                statement.append("\n\n");
            }

            bufferedWriter.write(statement.toString());
        }

        bufferedWriter.flush();
        bufferedWriter.close();

        Serializer.outputMetadata(metadataOutputPath, tableNames, columnNames);
    }

    private String dataTypeToStatement(DataTypeSpec dataTypeSpec) {
        switch (dataTypeSpec.type()) {
            case INT:
                return "INTEGER";
            case FLOAT:
                return "FLOAT";
            case BOOLEAN:
                return "BOOLEAN";
            case STRING:
                return "TEXT";
        }

        return null;
    }

    private String columnConfigToStatement(ColumnConfig columnConfig, String columnName) {
        StringBuilder statement = new StringBuilder();

        statement.append("\"" + columnName + "\"" + " ");
        statement.append(dataTypeToStatement((DataTypeSpec) columnConfig.getObject(ColumnConfig.DATATYPE)) + " ");

        if (columnConfig.getBoolean(ColumnConfig.UNIQUE)) {
            statement.append("UNIQUE ");
        }
        if (! columnConfig.getBoolean(ColumnConfig.HAS_NULL)) {
            statement.append("NOT NULL ");
        }

        return statement.toString();
    }

    private String tableConfigToStatement(TableConfig tableConfig, Table table) {
        StringBuilder statement = new StringBuilder();

        statement.append("CREATE TABLE ");
        statement.append("\"" + databaseName + "\".\"" + table.getAttributeName() + "\"" + " (\n");

        for (ColumnConfig columnConfig: (List<ColumnConfig>) tableConfig.getObject(TableConfig.COLUMN_CONFIGS)) {
            String columnName = table.getColumnName(columnConfig.getInt(ColumnConfig.COLUMN_ID));
            statement.append("\t");
            statement.append(columnConfigToStatement(columnConfig, columnName));
            statement.append(",\n");
        }
        statement.deleteCharAt(statement.length() - 2);

        statement.append(");");
        return statement.toString();
    }

    private String foreignKeyToStatement(String pkTable, String pkColumn, String fkTable, String fkColumn) {
        StringBuilder statement = new StringBuilder();

        statement.append("ALTER TABLE \"");
        statement.append(databaseName + "\".\"" + fkTable + "\"\n");

        statement.append("ADD FOREIGN KEY (\"");
        statement.append(fkColumn);
        statement.append("\") REFERENCES \"");
        statement.append(databaseName + "\".\"" +pkTable + "\" (\"");
        statement.append(pkColumn + "\");");

        return statement.toString();
    }

    private String primaryKeyToStatement(String pkTable, String pkColumn) {
        StringBuilder statement = new StringBuilder();

        statement.append("ALTER TABLE \"");
        statement.append(databaseName + "\".\"" + pkTable + "\"\n");

        statement.append("ADD PRIMARY KEY (\"");
        statement.append(pkColumn);
        statement.append("\");");

        return statement.toString();
    }

    public static void main(String[] args) throws Exception {
        SpecificationParser specificationParser = new SpecificationParser();

        specificationParser.parseYAML("test.yaml");
        specificationParser.write("test_output.json");
        DatasetGenerator datasetGenerator = DatasetConfig.specToGenerator(specificationParser.getDatabase());
//        Dataset dataset = datasetGenerator.generateDataset();

//        PostgresSerializer postgresSerializer = new PostgresSerializer(dataset);
//        postgresSerializer.serialize("/Users/ryan/Documents/postgres_test");
    }
}
