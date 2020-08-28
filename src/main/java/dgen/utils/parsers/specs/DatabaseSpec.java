package dgen.utils.parsers.specs;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;
import dgen.utils.parsers.SpecificationException;
import dgen.utils.parsers.specs.relationshipspecs.DatabaseRelationshipSpec;
import dgen.utils.parsers.specs.serializerspecs.CSVSerializerSpec;
import dgen.utils.parsers.specs.serializerspecs.SerializerSpec;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@JsonRootName("database")
@JsonPropertyOrder({"databaseName", "randomSeed", "tableSpecs"})
public class DatabaseSpec implements Spec {
    private String databaseName;
    private Long randomSeed = new Random().nextLong();
    private SerializerSpec serializer = new CSVSerializerSpec();
    @JsonProperty("tables")
    private List<TableSpec> tableSpecs;
    private List<DatabaseRelationshipSpec> databaseRelationships = new ArrayList<>();

    @Override
    public void validate() {
        if (databaseName == null) {
            throw new SpecificationException("Database must have a name");
        }
    }

    @Override
    public SpecType specType() { return SpecType.DATABASE; }

    public void setDatabaseName(String databaseName) { this.databaseName = databaseName; }
    public String getDatabaseName() { return databaseName; }

    public Long getRandomSeed() {
        return randomSeed;
    }

    public void setRandomSeed(Long randomSeed) {
        this.randomSeed = randomSeed;
    }

    public void setTableSpecs(List<TableSpec> tableSpecs) { this.tableSpecs = tableSpecs; }
    public List<TableSpec> getTableSpecs() { return tableSpecs; }

    public List<DatabaseRelationshipSpec> getDatabaseRelationships() {
        return databaseRelationships;
    }

    public void setDatabaseRelationships(List<DatabaseRelationshipSpec> databaseRelationships) {
        this.databaseRelationships = databaseRelationships;
    }

    public SerializerSpec getSerializer() {
        return serializer;
    }

    public void setSerializer(SerializerSpec serializer) {
        this.serializer = serializer;
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