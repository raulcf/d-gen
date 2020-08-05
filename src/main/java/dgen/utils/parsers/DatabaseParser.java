package dgen.utils.parsers;

import dgen.utils.parsers.specs.ColumnSpec;
import dgen.utils.parsers.specs.DatabaseSpec;
import dgen.utils.parsers.specs.TableSpec;
import dgen.utils.parsers.specs.relationships.DatabaseRelationshipSpec;
import dgen.utils.parsers.specs.relationships.RelationshipType;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class DatabaseParser {
    private DatabaseSpec database;
    private RandomGenerator rnd;

    public DatabaseSpec getDatabase() { return database; }
    public void setDatabase(DatabaseSpec database) { this.database = database; }

    /**
     * Parses the tables within a DatabaseSchema object.
     * @param d DatabaseSchema object to parse.
     */
    public void parse(DatabaseSpec d) {
        database = d;
        rnd = new RandomGenerator(database.getRandomSeed());

        TableParser tableParser = new TableParser(rnd);
        for (TableSpec tableSchema: database.getTableSpecs()) {
            tableParser.parse(tableSchema);
        }
        database.setTableSpecs(tableParser.getTables());
        Map<Integer, Map<Integer, ColumnSpec>> tableMap = tableParser.getTableMap();

        DatabaseRelationshipParser databaseRelationshipParser = new DatabaseRelationshipParser(tableMap, rnd);
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
