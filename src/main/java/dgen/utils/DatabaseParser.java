package dgen.utils;

import dgen.utils.schemas.ColumnSchema;
import dgen.utils.schemas.DatabaseSchema;
import dgen.utils.schemas.TableSchema;
import dgen.utils.schemas.relationships.DatabaseRelationshipSchema;
import dgen.utils.schemas.relationships.DefPKFKSchema;
import dgen.utils.schemas.relationships.RelationshipType;

import java.util.ArrayList;
import java.util.Comparator;
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

        TableParser tableParser = new TableParser();
        for (TableSchema tableSchema: database.getTableSchemas()) {
            tableParser.parse(tableSchema);
        }
        database.setTableSchemas(tableParser.getTables());
        Map<Integer, Map<Integer, ColumnSchema>> tableMap = tableParser.getTableMap();
        /*
        TODO: Consolidate list of DatabaseRelationshipSchemas into one object after parsing.
         Currently each object in databaseRelationships represents only a single PKFK relationship.
         In the future it might make it easier for us if there's only a single object containing all of the mappings.
         */
        DatabaseRelationshipParser databaseRelationshipParser = new DatabaseRelationshipParser(tableMap);
        List<DatabaseRelationshipSchema> databaseRelationships = database.getDatabaseRelationships();
        databaseRelationships.sort(new Comparator<DatabaseRelationshipSchema>() {
            @Override
            public int compare(DatabaseRelationshipSchema d1, DatabaseRelationshipSchema d2) {
                if (d1.relationshipType() == d2.relationshipType()) { return 0; }
                return d1.relationshipType() == RelationshipType.DEFPKFK ? -1: 1;
            }
        });
        for (DatabaseRelationshipSchema databaseRelationship: databaseRelationships) {
            databaseRelationshipParser.parse(databaseRelationship);
        }

        List<DatabaseRelationshipSchema> parsedPKFKList = new ArrayList<>();
        parsedPKFKList.add(databaseRelationshipParser.getParsedPKFKSchema());
        database.setDatabaseRelationships(parsedPKFKList);
    }

}
