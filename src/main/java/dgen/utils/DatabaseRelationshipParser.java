package dgen.utils;

import dgen.utils.schemas.ColumnSchema;
import dgen.utils.schemas.relationships.DatabaseRelationshipSchema;
import dgen.utils.schemas.relationships.DefPKFKSchema;
import dgen.utils.schemas.relationships.GenPKFKSchema;
import dgen.utils.schemas.relationships.GraphSchema;
import org.javatuples.Pair;

import java.util.*;

public class DatabaseRelationshipParser {
    /* Mapping of existing relationships within a database */
    private Map<Pair<Integer, Integer>, Set<Pair<Integer, Integer>>> relationshipMap = new HashMap<>();
    /* Map of how many foreign keys within a table */
    private Map<Integer, Integer> tableFKCount = new HashMap<>();
    private final int maxNumRelationships;
    private int numRelationships = 0;
    private DefPKFKSchema parsedPKFKSchema = new DefPKFKSchema();
    private Set<Pair<Integer, Integer>> primaryKeys = new HashSet<>();
    private Set<Pair<Integer, Integer>> foreignKeys = new HashSet<>();

    /**
     * Processes and stores the tableIDs and columnIDs of all foreign keys and primary keys.
     * @param tableMap Map of tableID to TableSchema object.
     */
    public DatabaseRelationshipParser(Map<Integer, Map<Integer, ColumnSchema>> tableMap) {
        for (int tableID: tableMap.keySet()) {
            Map<Integer, ColumnSchema> columnMap = tableMap.get(tableID);
            for (int columnID: columnMap.keySet()) {
                ColumnSchema column = columnMap.get(columnID);
                switch (column.schemaType()) {
                    case DEFFOREIGNKEY:
                        Pair<Integer, Integer> foreignKeyPair = Pair.with(tableID, columnID);
                        if (tableFKCount.containsKey(tableID)) {
                            tableFKCount.replace(tableID, tableFKCount.get(tableID) + 1);
                        } else {
                            tableFKCount.put(tableID, 1);
                        }

                        foreignKeys.add(foreignKeyPair);
                        break;
                    case PRIMARYKEY:
                        Pair<Integer, Integer> primaryKeyPair = Pair.with(tableID, columnID);
                        primaryKeys.add(primaryKeyPair);
                        break;
                }
            }
        }

        int numRelationships = foreignKeys.size();

        for (int tableID: tableFKCount.keySet()) {
            if (tableFKCount.get(tableID) > primaryKeys.size()) {
                numRelationships = Integer.min(numRelationships, foreignKeys.size() - tableFKCount.get(tableID));
            }
        }

        maxNumRelationships = numRelationships;
    }

    /**
     * Parses a DatabaseRelationshipSchema objects into a list of DefPKFKSchema objects.
     * @param databaseRelationship DatabaseRelationshipSchema object to parse.
     * @return List of parsed DefPKFK objects.
     */
    public void parse(DatabaseRelationshipSchema databaseRelationship) {
        switch (databaseRelationship.relationshipType()) {
            case GENPKFK:
                GenPKFKSchema genPKFK = (GenPKFKSchema) databaseRelationship;
                parseGenPKFK(genPKFK);
                break;
            case DEFPKFK:
                DefPKFKSchema defPKFK = (DefPKFKSchema) databaseRelationship;
                parseDefPKFK(defPKFK);
                break;
            default:
                throw new SpecificationException("Cannot parse " + databaseRelationship.relationshipType() +
                        " with DatabaseRelationshipParser");
        }
    }

    /**
     * Checks to make sure that a PKFK relationship is valid (PK & FK exist, no duplicate relationships).
     * @param defPKFK DefPKFKSchema object to parse.
     * @return List containing the single parsed DefPKFKSchema object.
     */
    private void parseDefPKFK(DefPKFKSchema defPKFK) {
        defPKFK.parseMapping();
        defPKFK.validate();

        List<Pair<Integer, Integer>> parsedPrimaryKeys = parsedPKFKSchema.getPrimaryKeys();
        List<Pair<Integer, Integer>> parsedForeignKeys = parsedPKFKSchema.getForeignKeys();

        for (int i = 0; i < defPKFK.getPrimaryKeys().size(); i++) {
            Pair<Integer, Integer> primaryKey = defPKFK.getPrimaryKeys().get(i);
            Pair<Integer, Integer> foreignKey = defPKFK.getForeignKeys().get(i);

            if (relationshipMap.containsKey(primaryKey) && relationshipMap.get(primaryKey).contains(foreignKey)) {
                throw new SpecificationException("Duplicate PK-FK relationship");
            }
            if (!primaryKeys.contains(primaryKey)) {
                throw new SpecificationException("tableID " + primaryKey.getValue0() + " and columnID " + primaryKey.getValue1() + " not a primary key");
            }
            if (!foreignKeys.contains(foreignKey)) {
                throw new SpecificationException("tableID " + foreignKey.getValue0() + " and columnID " + foreignKey.getValue1() + " not a foreign key");
            }

            foreignKeys.remove(foreignKey);
            numRelationships += 1;
            parsedPrimaryKeys.add(primaryKey);
            parsedForeignKeys.add(foreignKey);
        }

        parsedPKFKSchema.setPrimaryKeys(parsedPrimaryKeys);
        parsedPKFKSchema.setForeignKeys(parsedForeignKeys);
    }

    /**
     * Parses a GenPKFKSchema object into multiple DefPKFKSchema objects.
     * @param genPKFK GenPKFKSchema to parse.
     * @return List of DefPKFKSchema objects.
     */
    private void parseGenPKFK(GenPKFKSchema genPKFK) {
        if (numRelationships + genPKFK.getNumRelationships() > maxNumRelationships) {
            throw new SpecificationException("Too many PK-FK relationships");
        }

        GraphSchema graphSchema = genPKFK.getGraphSchema();
        Map<Pair<Integer, Integer>, Set<Pair<Integer, Integer>>> mapping = graphSchema.generateDatabaseGraph(new ArrayList<>(primaryKeys),
                new ArrayList<>(foreignKeys), genPKFK.getNumRelationships(), relationshipMap);

        List<Pair<Integer, Integer>> parsedPrimaryKeys = parsedPKFKSchema.getPrimaryKeys();
        List<Pair<Integer, Integer>> parsedForeignKeys = parsedPKFKSchema.getForeignKeys();

        for (Pair<Integer, Integer> primaryKey: mapping.keySet()) {
            for (Pair<Integer, Integer> foreignKey: mapping.get(primaryKey)) {
                parsedPrimaryKeys.add(primaryKey);
                parsedForeignKeys.add(foreignKey);
            }
        }

        parsedPKFKSchema.setPrimaryKeys(parsedPrimaryKeys);
        parsedPKFKSchema.setForeignKeys(parsedForeignKeys);
    }

    public DefPKFKSchema getParsedPKFKSchema() {
        parsedPKFKSchema.unparseMapping();
        return parsedPKFKSchema;
    }

    public void setParsedPKFKSchema(DefPKFKSchema parsedPKFKSchema) {
        this.parsedPKFKSchema = parsedPKFKSchema;
    }
}
