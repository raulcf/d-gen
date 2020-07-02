package dgen.utils.schemas;

import java.util.*;

public class DatabaseSchema {
    private String databaseName;
    private List<TableSchema> tableSchemas;

    public void setDatabaseName(String databaseName) { this.databaseName = databaseName; }
    public String getDatabaseName() { return databaseName; }

    public void setTableSchemas(List<TableSchema> tableSchemas) { this.tableSchemas = tableSchemas; }
    public List<TableSchema> getTableSchemas() { return tableSchemas; }

    @Override
    public String toString() {
        return "DatabaseSchema{" +
                "databaseName='" + databaseName + '\'' +
                ", tableSchemas=" + tableSchemas +
                '}';
    }
}
