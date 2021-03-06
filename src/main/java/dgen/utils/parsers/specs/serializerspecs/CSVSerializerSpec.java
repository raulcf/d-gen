package dgen.utils.parsers.specs.serializerspecs;

import org.codehaus.jackson.annotate.JsonTypeName;

@JsonTypeName("csv")
public class CSVSerializerSpec implements SerializerSpec {

    String delimiter = ",";
    String parentDirectory;
    String metadataOutputPath;
    String lowLevelOutputPath = null;

    @Override
    public Serializers serializerType() {
        return Serializers.CSV;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
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

    public void setLowLevelOutputPath(String lowLevelOutputPath) {
        this.lowLevelOutputPath = lowLevelOutputPath;
    }
}
