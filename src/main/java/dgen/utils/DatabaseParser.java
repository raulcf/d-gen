package dgen.utils;

import dgen.utils.schemas.ColumnSchema;
import dgen.utils.schemas.DatabaseSchema;
import dgen.utils.schemas.TableSchema;
import dgen.utils.schemas.relationships.DatabaseRelationshipSchema;
import dgen.utils.schemas.relationships.DefPKFKSchema;
import dgen.utils.schemas.relationships.TableRelationshipSchema;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseParser {
    private DatabaseSchema database;

    public DatabaseSchema getDatabase() { return database; }
    public void setDatabase(DatabaseSchema database) { this.database = database; }

    /**
     * Parses the tables within a DatabaseSchema object.
     * @param d DatabaseSchema object to parse.
     */
    public void parse(DatabaseSchema d) {
        database = d;

        Map<Integer, TableSchema> tableIDMap = new HashMap<>();
        TableParser parser = new TableParser();
        for (TableSchema t: database.getTableSchemas()) {
            parser.parse(t);
        }
        database.setTableSchemas(parser.getTables());
        tableIDMap.putAll(parser.getTableIDMap());

        /*
        TODO: Consolidate list of DatabaseRelationshipSchemas into one object after parsing.
         Currently each object in databaseRelationships represents only a single PKFK relationship.
         In the future it might make it easier for us if there's only a single object containing all of the mappings.
         TODO: Change this so defined relationships are parsed before general ones
         */
        List<DatabaseRelationshipSchema> databaseRelationships = new ArrayList<>();
        DatabaseRelationshipParser relationshipParser = new DatabaseRelationshipParser(tableIDMap);
        for (DatabaseRelationshipSchema dr: database.getDatabaseRelationships()) {
            databaseRelationships.addAll(relationshipParser.parse(dr));
        }
        database.setDatabaseRelationships(databaseRelationships);
    }

}
