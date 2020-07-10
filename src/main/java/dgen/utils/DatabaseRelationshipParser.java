package dgen.utils;

import dgen.utils.schemas.ColumnSchema;
import dgen.utils.schemas.DefTableSchema;
import dgen.utils.schemas.TableSchema;
import dgen.utils.schemas.relationships.DatabaseRelationshipSchema;
import dgen.utils.schemas.relationships.DefPKFKSchema;
import dgen.utils.schemas.relationships.GenPKFKSchema;
import dgen.utils.schemas.relationships.GraphSchema;
import org.javatuples.Pair;

import java.util.*;

public class DatabaseRelationshipParser {
    /* Mapping of existing relationships within a database */
    private Map<Pair<Integer, Integer>, Set<Pair<Integer, Integer>>> relationshipMap = new HashMap<>();
    private int numRelationships = 0;
    private Set<Pair<Integer, Integer>> primaryKeys = new HashSet<>();
    private Set<Pair<Integer, Integer>> foreignKeys = new HashSet<>();

    /**
     * Finds the tableID and columnID of all primary and foreign keys.
     * @param tableIDMap Map of tableID to TableSchema object.
     */
    public DatabaseRelationshipParser(Map<Integer, TableSchema> tableIDMap) {
        for (int tableID: tableIDMap.keySet()) {
            DefTableSchema table = (DefTableSchema) tableIDMap.get(tableID);
            Map<Integer, ColumnSchema> columnIDMap = table.getColumnIDMap();
            for (int columnID: columnIDMap.keySet()) {
                ColumnSchema column = columnIDMap.get(columnID);
                if (column.schemaType() == "defForeignKey") {
                    Pair<Integer, Integer> pair = Pair.with(tableID, columnID);
                    foreignKeys.add(pair);
                } else if (column.schemaType() == "primaryKey") {
                    Pair<Integer, Integer> pair = Pair.with(tableID, columnID);
                    primaryKeys.add(pair);
                }
            }
        }
    }

    public List<DefPKFKSchema> parse(DatabaseRelationshipSchema databaseRelationship) {
        switch (databaseRelationship.relationshipType()) {
            case "genPKFK":
                GenPKFKSchema genPKFK = (GenPKFKSchema) databaseRelationship;
                return parseGenPKFK(genPKFK);
            case "defPKFK":
                DefPKFKSchema defPKFK = (DefPKFKSchema) databaseRelationship;
                return parseDefPKFK(defPKFK);
            default:
                return new ArrayList<DefPKFKSchema>();
        }
    }

    /**
     * Checks to make sure that a PKFK relationship is valid (PK & FK exist, no duplicate relationships).
     * @param defPKFK DefPKFKSchema object to parse.
     * @return Arraylist containing the single parsed DefPKFKSchema object.
     */
    private List<DefPKFKSchema> parseDefPKFK(DefPKFKSchema defPKFK) {
        defPKFK.parseMapping();
        defPKFK.validate();
        Pair<Integer, Integer> start = defPKFK.getStart();
        Pair<Integer, Integer> end = defPKFK.getEnd();

        if (!primaryKeys.contains(start)) {
            throw new SpecificationException("tableID " + start.getValue0() + " and columnID " + start.getValue1() + " not a primary key");
        }
        if (!foreignKeys.contains(end)) {
            throw new SpecificationException("tableID " + start.getValue0() + " and columnID " + start.getValue1() + " not a foreign key");
        }
        if (relationshipMap.containsKey(start) && relationshipMap.get(start).contains(end)) {
            throw new SpecificationException("Duplicate PK-FK relationship");
        }

        List<DefPKFKSchema> defPKFKSchemaList = new ArrayList<>();
        defPKFKSchemaList.add(defPKFK);
        foreignKeys.remove(end);
        return defPKFKSchemaList;
    }

    /**
     * Parses a GenPKFKSchema object into multiple DefPKFKSchema objects.
     * @param genPKFK GenPKFKSchema to parse.
     * @return Arraylist of DefPKFKSchema objects.
     */
    private List<DefPKFKSchema> parseGenPKFK(GenPKFKSchema genPKFK) {
        //TODO: Needs to be more robust to account for PK and FK in the same table
        if (genPKFK.getNumRelationships() + numRelationships > foreignKeys.size()) {
            throw new SpecificationException("Too many relationships in one table");
        }

        GraphSchema graphSchema = genPKFK.getGraphSchema();
        List<Pair<Integer, Integer>> pk = new ArrayList<>();
        List<Pair<Integer, Integer>> fk = new ArrayList<>();
        pk.addAll(primaryKeys);
        fk.addAll(foreignKeys);
        Map<Pair<Integer, Integer>, Set<Pair<Integer, Integer>>> mapping = graphSchema.generateDatabaseGraph(pk,
                fk, genPKFK.getNumRelationships(), relationshipMap);

        List<DefPKFKSchema> defPKFKSchemaList = new ArrayList<>();
        for (Pair<Integer, Integer> start: mapping.keySet()) {
            for (Pair<Integer, Integer> end: mapping.get(start)) {
                DefPKFKSchema defPKFK = new DefPKFKSchema();
                defPKFK.setStart(start);
                defPKFK.setEnd(end);
                defPKFK.unparseMapping();
                defPKFKSchemaList.addAll(parseDefPKFK(defPKFK));
            }
        }
        return defPKFKSchemaList;
    }
}
