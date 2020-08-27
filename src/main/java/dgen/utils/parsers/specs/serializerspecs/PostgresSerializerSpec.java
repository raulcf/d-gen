package dgen.utils.parsers.specs.serializerspecs;

import org.codehaus.jackson.annotate.JsonTypeName;

@JsonTypeName("postgres")
public class PostgresSerializerSpec implements SerializerSpec {

    String parentDirectory;
    String metadataOutputPath;
    String lowLevelOutputPath = null;

    @Override
    public Serializers serializerType() {
        return Serializers.POSTGRES;
    }

    @Override
    public String getParentDirectory() {
        return parentDirectory;
    }

    @Override
    public void setParentDirectory(String parentDirectory) {
        this.parentDirectory = parentDirectory;
    }

    @Override
    public String getMetadataOutputPath() {
        return metadataOutputPath;
    }

    @Override
    public void setMetadataOutputPath(String metadataOutputPath) {
        this.metadataOutputPath = metadataOutputPath;
    }

    @Override
    public String getLowLevelOutputPath() {
        return lowLevelOutputPath;
    }

    @Override
    public void setLowLevelOutputPath(String lowLevelOutputPath) {
        this.lowLevelOutputPath = lowLevelOutputPath;
    }
}
