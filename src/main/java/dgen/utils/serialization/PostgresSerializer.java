package dgen.utils.serialization;

import dgen.column.Column;
import dgen.column.ColumnConfig;
import dgen.dataset.Dataset;
import dgen.dataset.DatasetConfig;
import dgen.dataset.DatasetGenerator;
import dgen.datatypes.DataType;
import dgen.datatypes.NativeType;
import dgen.tables.Table;
import dgen.tables.TableConfig;
import dgen.utils.parsers.SpecificationParser;
import dgen.utils.parsers.specs.datatypespecs.DataTypeSpec;
import org.javatuples.Pair;

import java.io.File;
import java.io.FileWriter;
import java.util.*;

public class PostgresSerializer implements Serializer{

    Dataset dataset;
    DatasetConfig datasetConfig;
    String databaseName;

    public PostgresSerializer(Dataset dataset) {
        this.dataset = dataset;
        this.databaseName = dataset.getAttributeName();
        this.datasetConfig = dataset.getDatasetConfig();
    }

    /**
     * Creates a script of PostgreSQL commands that can be used to load a database object into PostgreSQL.
     * Dataset objects aren't loaded as Postgres objects but rather as schemas.
     * @param scriptPath Path of file to write PostgreSQL script to.
     * @param metadataPath Path to write dataset metadata to.
     */
    @Override
    public void serialize(String scriptPath, String metadataPath) throws Exception {
        StringBuilder statement = new StringBuilder();
        statement.append("CREATE SCHEMA \"");
        statement.append(databaseName + "\";");
        statement.append("\n\n");

        for (TableConfig tableConfig: (List<TableConfig>) datasetConfig.getObject(DatasetConfig.TABLE_CONFIGS)) {
            Table table = dataset.getTable(tableConfig.getInt(TableConfig.TABLE_ID));
            statement.append(tableConfigToStatement(tableConfig, table));
            statement.append("\n");

        }

        statement.append("\n");

        // We insert data before defining constraints, but the inserted data should already follow those constraints
        for (Table table: dataset.getTables()) {
            statement.append(insertTableValues(table));
            statement.append("\n\n");
        }

        Map<Pair<Integer, Integer>, Set<Pair<Integer, Integer>>> pkfkMappings = (Map<Pair<Integer, Integer>, Set<Pair<Integer, Integer>>>) datasetConfig.getObject("pk.fk.mappings");
        for (Pair<Integer, Integer> pk : pkfkMappings.keySet()) {
            String pkColumnName = dataset.getColumn(pk.getValue0(), pk.getValue1()).getAttributeName();
            String pkTableName = dataset.getTable(pk.getValue0()).getAttributeName();

            statement.append(primaryKeyToStatement(pkTableName, pkColumnName));
            statement.append("\n\n");

            for (Pair<Integer, Integer> fk : pkfkMappings.get(pk)) {
                String fkColumnName = dataset.getColumn(fk.getValue0(), fk.getValue1()).getAttributeName();
                String fkTableName = dataset.getTable(fk.getValue0()).getAttributeName();

                statement.append(foreignKeyToStatement(pkTableName, pkColumnName, fkTableName, fkColumnName));
                statement.append("\n\n");
            }
        }

        File postgresScript = new File(scriptPath);
        if (postgresScript.exists()) { throw new Exception("Path " + scriptPath + " already exists"); }

        FileWriter postgresScriptWriter = new FileWriter(postgresScript);
        postgresScriptWriter.write(statement.toString());
        postgresScriptWriter.flush();
        postgresScriptWriter.close();

        Serializer.outputMetadata(metadataPath, dataset);
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

    private String columnConfigToStatement(ColumnConfig columnConfig, Column column) {
        StringBuilder statement = new StringBuilder();

        statement.append("\"" + column.getAttributeName() + "\"" + " ");
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
            Column column = table.getColumn(columnConfig.getInt(ColumnConfig.COLUMN_ID));
            statement.append("\t");
            statement.append(columnConfigToStatement(columnConfig, column));
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

    private String insertTableValues(Table table) {
        StringBuilder statement = new StringBuilder();

        statement.append("INSERT INTO ");
        statement.append("\"" + databaseName + "\".\"" + table.getAttributeName() + "\"");
        statement.append(" VALUES\n");

        List<Iterator<DataType>> columnIterators = new ArrayList<>();
        List<Column> columns = table.getColumns();
        for (Column c : columns) {
            Iterator<DataType> it = c.getData().iterator();
            columnIterators.add(it);
        }

        // record
        boolean moreData = true;
        while(moreData) {
            List<String> recordValues = new ArrayList<>();
            for (Iterator<DataType> it : columnIterators) {
                if (! it.hasNext()) {
                    moreData = false;
                    continue;
                }
                DataType dt = it.next();

                String value;
                if (dt.nativeType() == NativeType.STRING) {
                    value = "'" + dt.value() + "'";
                } else {
                    value = dt.value().toString();
                }

                recordValues.add(value);
            }
            String record = String.join(",", recordValues);

            statement.append("(" + record + "),");
            statement.append("\n");
        }

        statement.delete(statement.length() - 6, statement.length());
        statement.append(";");

        return statement.toString();
    }

    public static void main(String[] args) throws Exception {
        SpecificationParser specificationParser = new SpecificationParser();

        specificationParser.parseYAML("test.yaml");
        specificationParser.write("test_output.json");
        DatasetGenerator datasetGenerator = DatasetConfig.specToGenerator(specificationParser.getDatabase());
        Dataset dataset = datasetGenerator.generateDataset();

        PostgresSerializer postgresSerializer = new PostgresSerializer(dataset);
//        postgresSerializer.serialize("/Users/ryan/Documents/postgres_test");
    }
}
