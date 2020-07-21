package dgen.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import dgen.utils.schemas.DatabaseSchema;

import java.io.File;


public class SpecificationParser {
    private DatabaseSchema database;

    public DatabaseSchema getDatabase() {
        return database;
    }


    /**
     * Parses a d-gen specification file from a YAML format to a DatabaseSchema object.
     * @param path Path to d-gen specification YAML.
     */
    public void parseYAML(String path) {
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

    /**
     * Parses a d-gen specification file from a JSON format to a DatabaseSchema object.
     * @param path Path to d-gen specification YAML.
     */
    public void parseJSON(String path) {
        File file = new File(path);
        ObjectMapper mapper = new ObjectMapper();
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

    /**
     * Writes the parsed DatabaseSchema object into a d-gen JSON specification.
     * @param path Path to write to.
     */
    public void write(String path) {
        File file = new File(path);
        ObjectMapper mapper = new ObjectMapper();
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
        SpecificationParser parser = new SpecificationParser();
        final long startTime = System.currentTimeMillis();
        parser.parseYAML("example_specifications/2_input.yaml");
        final long endTime = System.currentTimeMillis();
        System.out.println(endTime - startTime);
        parser.write("example_specifications/2_output.json");
    }
}