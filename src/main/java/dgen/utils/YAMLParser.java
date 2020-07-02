package dgen.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import dgen.utils.schemas.DatabaseSchema;

import java.io.File;

public class YAMLParser {
    DatabaseSchema database;

    public void parse(String path) {
        File file = new File(path);
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

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

        try {
            mapper.writeValue(file, database);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        YAMLParser parser = new YAMLParser();
        parser.parse("new_test.yaml");
        System.out.println(parser.database);
        parser.write("new_test.yaml");
    }
}