package dgen.utils;

import dgen.utils.specs.ColumnSpec;
import dgen.utils.specs.DatabaseSpec;
import dgen.utils.specs.TableSpec;
import dgen.utils.specs.relationships.DatabaseRelationshipSpec;
import dgen.utils.specs.relationships.RelationshipType;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class DatabaseParser {
    private DatabaseSpec database;

    public DatabaseSpec getDatabase() { return database; }
    public void setDatabase(DatabaseSpec database) { this.database = database; }

    /**
     * Parses the tables within a DatabaseSchema object.
     * @param d DatabaseSchema object to parse.
     */
    public void parse(DatabaseSpec d) {
        database = d;

        TableParser tableParser = new TableParser();
        for (TableSpec tableSchema: database.getTableSpecs()) {
            tableParser.parse(tableSchema);
        }
        database.setTableSpecs(tableParser.getTables());
        Map<Integer, Map<Integer, ColumnSpec>> tableMap = tableParser.getTableMap();
        /*
        TODO: Consolidate list of DatabaseRelationshipSchemas into one object after parsing.
         Currently each object in databaseRelationships represents only a single PKFK relationship.
         In the future it might make it easier for us if there's only a single object containing all of the mappings.
         */
        DatabaseRelationshipParser databaseRelationshipParser = new DatabaseRelationshipParser(tableMap);
        List<DatabaseRelationshipSpec> databaseRelationships = database.getDatabaseRelationships();
        databaseRelationships.sort(new Comparator<DatabaseRelationshipSpec>() {
            @Override
            public int compare(DatabaseRelationshipSpec d1, DatabaseRelationshipSpec d2) {
                if (d1.relationshipType() == d2.relationshipType()) { return 0; }
                return d1.relationshipType() == RelationshipType.DEFPKFK ? -1: 1;
            }
        });
        for (DatabaseRelationshipSpec databaseRelationship: databaseRelationships) {
            databaseRelationshipParser.parse(databaseRelationship);
        }

        List<DatabaseRelationshipSpec> parsedPKFKList = new ArrayList<>();
        parsedPKFKList.add(databaseRelationshipParser.getParsedPKFKSchema());
        database.setDatabaseRelationships(parsedPKFKList);
    }

}
