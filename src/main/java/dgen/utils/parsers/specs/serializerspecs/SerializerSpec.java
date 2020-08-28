package dgen.utils.parsers.specs.serializerspecs;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({@JsonSubTypes.Type(value = CSVSerializerSpec.class, name = "csv"),
        @JsonSubTypes.Type(value = PostgresSerializerSpec.class, name = "postgres"),
        @JsonSubTypes.Type(value = ParquetSerializerSpec.class, name = "parquet")})
@JsonTypeName("serializer")
public interface SerializerSpec {

    Serializers serializerType();

    String getParentDirectory();

    void setParentDirectory(String parentDirectory);

    String getMetadataOutputPath();

    void setMetadataOutputPath(String metadataOutputPath);

    String getLowLevelOutputPath();

    void setLowLevelOutputPath(String outputPath);
}
