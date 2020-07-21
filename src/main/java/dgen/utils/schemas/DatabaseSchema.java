package dgen.utils.schemas;

import com.fasterxml.jackson.annotation.*;
import dgen.utils.schemas.relationships.DatabaseRelationshipSchema;

import java.util.*;

@JsonRootName("database")
@JsonPropertyOrder({"databaseName", "tableSchemas"})
public class DatabaseSchema implements Schema {
    private String databaseName;
    private List<TableSchema> tableSchemas;
    private List<DatabaseRelationshipSchema> databaseRelationships = new ArrayList<>();

    @Override
    public void validate() { }

    @Override
    public SchemaType schemaType() { return SchemaType.DATABASE; }

    public void setDatabaseName(String databaseName) { this.databaseName = databaseName; }
    public String getDatabaseName() { return databaseName; }

    public void setTableSchemas(List<TableSchema> tableSchemas) { this.tableSchemas = tableSchemas; }
    public List<TableSchema> getTableSchemas() { return tableSchemas; }

    public List<DatabaseRelationshipSchema> getDatabaseRelationships() {
        return databaseRelationships;
    }

    public void setDatabaseRelationships(List<DatabaseRelationshipSchema> databaseRelationships) {
        this.databaseRelationships = databaseRelationships;
    }

    @Override
    public String toString() {
        return "DatabaseSchema{" +
                "databaseName='" + databaseName + '\'' +
                ", tableSchemas=" + tableSchemas +
                ", databaseRelationships=" + databaseRelationships +
                '}';
    }
}