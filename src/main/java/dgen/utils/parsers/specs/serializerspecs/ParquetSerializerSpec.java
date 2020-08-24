package dgen.utils.parsers.specs.serializerspecs;

import org.codehaus.jackson.annotate.JsonTypeName;

@JsonTypeName("parquet")
public class ParquetSerializerSpec implements SerializerSpec {

    String parentDirectory;
    String metadataOutputPath;

    @Override
    public Serializers serializerType() {
        return Serializers.CSV;
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
}
