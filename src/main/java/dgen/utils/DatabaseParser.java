package dgen.utils;

import dgen.utils.schemas.DatabaseSchema;
import dgen.utils.schemas.TableSchema;

import java.util.ArrayList;
import java.util.List;

public class DatabaseParser {
    private DatabaseSchema database;

    public DatabaseSchema getDatabase() { return database; }
    public void setDatabase(DatabaseSchema database) { this.database = database; }

    public void parse(DatabaseSchema d) {
        database = d;
        List<TableSchema> tables = new ArrayList<>();

        for (TableSchema t: database.getTableSchemas()) {
            TableParser parser = new TableParser();
            parser.parse(t);
            tables.addAll(parser.getTables());
        }

        database.setTableSchemas(tables);
    }
}
