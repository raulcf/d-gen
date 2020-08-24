package dgen.utils.serialization.config;

import dgen.coreconfig.DGException;
import dgen.utils.parsers.specs.serializerspecs.*;
import dgen.utils.serialization.CSVSerializer;
import dgen.utils.serialization.PostgresSerializer;
import dgen.utils.serialization.Serializer;

public interface SerializerConfig {

    static String PARENT_DIRECTORY = "parent.directory";
    static String PARENT_DIRECTORY_DOC = "Parent directory to place serialized files";

    static String METADATA_OUTPUT_PATH = "metadata.output.path";
    static String METADATA_OUTPUT_PATH_DOC = "Path to output dataset metadata";


    static SerializerConfig specToConfig(SerializerSpec serializerSpec) {
        switch (serializerSpec.serializerType()) {
            case CSV:
                CSVSerializerSpec csvSerializerSpec = (CSVSerializerSpec) serializerSpec;
                return CSVConfig.specToConfig(csvSerializerSpec);
            case POSTGRES:
                PostgresSerializerSpec postgresSerializerSpec = (PostgresSerializerSpec) serializerSpec;
                return PostgresConfig.specToConfig(postgresSerializerSpec);
            case PARQUET:
                ParquetSerializerSpec parquetSerializerSpec = (ParquetSerializerSpec) serializerSpec;
                return ParquetConfig.specToConfig(parquetSerializerSpec);
            default:
                throw new DGException("Invalid serializer");
        }
    }

    static Serializer configToGenerator(SerializerConfig serializerConfig) {
        switch (serializerConfig.serializerType()) {
            case CSV:
                CSVConfig csvConfig = (CSVConfig) serializerConfig;
                return new CSVSerializer(csvConfig);
            case POSTGRES:
                PostgresConfig postgresConfig = (PostgresConfig) serializerConfig;
                return new PostgresSerializer(postgresConfig);
        }

        return null;
    }

    public Serializers serializerType();
}
