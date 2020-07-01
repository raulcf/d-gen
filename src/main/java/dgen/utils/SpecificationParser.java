package dgen.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import dgen.utils.schemas.databaseSchema;
import dgen.utils.schemas.specificationSchema;

import java.io.File;

public class SpecificationParser {
    public specificationSchema specification;

    public databaseSchema parse(String path) {
        File file = new File(path);
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

        try {
            specification = mapper.readValue(file, specificationSchema.class);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }

        DatabaseParser parser = new DatabaseParser();
        parser.parse(specification.getDatabase());

        return parser.getDatabase();

    }

    public static void main(String[] args) {
        SpecificationParser parser = new SpecificationParser();
        databaseSchema d = parser.parse("column_test.yaml");
        System.out.println(parser.specification.getDatabase());
        System.out.println(d);

    }

}
