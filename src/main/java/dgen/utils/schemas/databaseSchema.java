package dgen.utils.schemas;

import java.util.*;

public class databaseSchema {
    private String databaseName;
    private List<tableSchema> tableSchemas;

    public final String [] requiredParameters = {"databaseName", "tableSchemas"};
    public final String [] optionalParameters = {"databaseRelationships", "databaseConstraints"};

    public databaseSchema(String databaseName, List<tableSchema> tableSchemas) {
        this.databaseName = databaseName;
        this.tableSchemas = tableSchemas;
    }

    public databaseSchema() {
        tableSchemas = new ArrayList<>();
    }

    public void setDatabaseName(String databaseName) { this.databaseName = databaseName; }
    public String getDatabaseName() { return databaseName; }

    public void setTableSchemas(List<tableSchema> tableSchemas) { this.tableSchemas = tableSchemas; }
    public List<tableSchema> getTableSchemas() { return tableSchemas; }

    @Override
    public String toString() {
        return "databaseSchema{" +
                "databaseName='" + databaseName + '\'' +
                ", tableSchemas=" + tableSchemas +
                ", requiredParameters=" + Arrays.toString(requiredParameters) +
                ", optionalParameters=" + Arrays.toString(optionalParameters) +
                '}';
    }
}
