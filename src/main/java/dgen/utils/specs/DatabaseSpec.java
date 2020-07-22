package dgen.utils.specs;

import com.fasterxml.jackson.annotation.*;
import dgen.utils.specs.relationships.DatabaseRelationshipSpec;

import java.util.*;

@JsonRootName("database")
@JsonPropertyOrder({"databaseName", "tableSpecs"})
public class DatabaseSpec implements Spec {
    private String databaseName;
    private List<TableSpec> tableSpecs;
    private List<DatabaseRelationshipSpec> databaseRelationships = new ArrayList<>();

    @Override
    public void validate() { }

    @Override
    public SpecType specType() { return SpecType.DATABASE; }

    public void setDatabaseName(String databaseName) { this.databaseName = databaseName; }
    public String getDatabaseName() { return databaseName; }

    public void setTableSpecs(List<TableSpec> tableSpecs) { this.tableSpecs = tableSpecs; }
    public List<TableSpec> getTableSpecs() { return tableSpecs; }

    public List<DatabaseRelationshipSpec> getDatabaseRelationships() {
        return databaseRelationships;
    }

    public void setDatabaseRelationships(List<DatabaseRelationshipSpec> databaseRelationships) {
        this.databaseRelationships = databaseRelationships;
    }

    @Override
    public String toString() {
        return "DatabaseSchema{" +
                "databaseName='" + databaseName + '\'' +
                ", tableSchemas=" + tableSpecs +
                ", databaseRelationships=" + databaseRelationships +
                '}';
    }
}