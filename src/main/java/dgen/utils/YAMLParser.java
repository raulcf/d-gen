package dgen.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import dgen.utils.schemas.DatabaseSchema;

import java.io.File;

public class YAMLParser {
    private DatabaseSchema database;

    public DatabaseSchema getDatabase() {
        return database;
    }

    public void parse(String path) {
        File file = new File(path);
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
        mapper.enable(DeserializationFeature.UNWRAP_ROOT_VALUE);

        try {
            database = mapper.readValue(file, DatabaseSchema.class);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }

        DatabaseParser parser = new DatabaseParser();
        parser.parse(database);
        database = parser.getDatabase();
    }

    public void write(String path) {
        File file = new File(path);
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
        mapper.enable(DeserializationFeature.UNWRAP_ROOT_VALUE);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        try {
            mapper.writeValue(file, database);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        YAMLParser parser = new YAMLParser();
        parser.parse("column_test.yaml");
        parser.write("column_test_output.yaml");
    }
}