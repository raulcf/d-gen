package dgen.utils.parsers;

import dgen.utils.parsers.specs.serializerspecs.CSVSerializerSpec;
import dgen.utils.parsers.specs.serializerspecs.ParquetSerializerSpec;
import dgen.utils.parsers.specs.serializerspecs.PostgresSerializerSpec;
import dgen.utils.parsers.specs.serializerspecs.SerializerSpec;

public class SerializerParser {
    private SerializerSpec serializer;

    public SerializerParser(SerializerSpec serializer) {
        this.serializer = serializer;
    }

    public SerializerSpec parse() {
        switch (serializer.serializerType()) {
            case CSV:
                CSVSerializerSpec csvSerializer = (CSVSerializerSpec) serializer;
                return parseCSVSerializer(csvSerializer);
            case POSTGRES:
                PostgresSerializerSpec postgresSerializer = (PostgresSerializerSpec) serializer;
                return parsePostgresSerializer(postgresSerializer);
            case PARQUET:
                ParquetSerializerSpec parquetSerializer = (ParquetSerializerSpec) serializer;
                return parseParquetSerializer(parquetSerializer);
            default:
                throw new SpecificationException("Not valid serializer");
        }
    }

    private CSVSerializerSpec parseCSVSerializer(CSVSerializerSpec csvSerializer) {
        if (csvSerializer.getDelimiter() == null) {
            throw new SpecificationException("Delimiter can't be null");
        }

        return csvSerializer;
    }

    private PostgresSerializerSpec parsePostgresSerializer(PostgresSerializerSpec postgresSerializer) {
        return postgresSerializer;
    }

    private ParquetSerializerSpec parseParquetSerializer(ParquetSerializerSpec parquetSerializer) {
        return parquetSerializer;
    }
}
