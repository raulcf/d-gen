package dgen.utils;

import dgen.utils.schemas.databaseSchema;
import dgen.utils.schemas.tableSchema;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DatabaseParser {
    private databaseSchema database;
//    private HashMap<String, Object> databaseParameters;

    public DatabaseParser() { database = new databaseSchema(); }

    public databaseSchema getDatabase() { return database; }
    public void setDatabase(databaseSchema database) { this.database = database; }

    public void parse(HashMap<String, Object> databaseParameters) {
        for (String parameter: database.requiredParameters) {
            if (!(databaseParameters.containsKey(parameter))) {
                ; //TODO: Throw error
            }
        }

        database.setDatabaseName((String) databaseParameters.get("databaseName"));
        List<tableSchema> tables = new ArrayList<>();

        for (HashMap<String, Object> tableSchema: (List<HashMap<String, Object>>) databaseParameters.get("tableSchemas")) {
            TableParser parser = new TableParser();
            parser.parse(tableSchema);
            tables.addAll(parser.getTables());
        }

        database.setTableSchemas(tables);
    }


    public static void main(String[] args) {
        DatabaseParser d = new DatabaseParser();
        d.parse(new HashMap<String, Object>());
    }
}
