package dgen.utils.schemas;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.*;

@JsonRootName("database")
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
