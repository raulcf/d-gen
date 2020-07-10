package dgen.utils.schemas;

import com.fasterxml.jackson.annotation.*;
import dgen.utils.schemas.relationships.DatabaseRelationshipSchema;
import dgen.utils.schemas.relationships.TableRelationshipSchema;

import java.util.*;

@JsonRootName("database")
@JsonPropertyOrder({"databaseName", "tableSchemas"})
public class DatabaseSchema implements Schema {
    private String databaseName;
    private List<TableSchema> tableSchemas;
    private List<DatabaseRelationshipSchema> databaseRelationships = new ArrayList<>();
    /*
        TODO: This makes it easier to easily access tables and columns but it is incredibly space inefficient.
         This is essentially is doubling the size of this object.
     */
    @JsonIgnore
    private Map<Integer, TableSchema> tableIDMap;

    @Override
    public void validate() {
        //TODO: Check whether there are duplicate tableIDs. It might be better to check while parsing.
    }

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

    public Map<Integer, TableSchema> getTableIDMap() {
        return tableIDMap;
    }

    public void setTableIDMap(Map<Integer, TableSchema> tableIDMap) {
        this.tableIDMap = tableIDMap;
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