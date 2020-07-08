package dgen.utils.schemas;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.*;

@JsonRootName("database")
@JsonPropertyOrder({"databaseName", "tableSchemas"})
public class DatabaseSchema implements Schema {
    private String databaseName;
    private List<TableSchema> tableSchemas;

    @Override
    public void validate() {
        //TODO: Check whether there are duplicate tableIDs. It might be better to check while parsing.
    }

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