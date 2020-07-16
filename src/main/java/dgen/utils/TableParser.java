package dgen.utils;

import dgen.utils.schemas.ColumnSchema;
import dgen.utils.schemas.DefTableSchema;
import dgen.utils.schemas.GenTableSchema;
import dgen.utils.schemas.TableSchema;
import dgen.utils.schemas.relationships.RelationshipType;
import dgen.utils.schemas.relationships.TableRelationshipSchema;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;


public class TableParser {
    private List<TableSchema> tables = new ArrayList<>();
    /* Mapping of tableIDs to a mapping of columns */
    private Map<Integer, Map<Integer, ColumnSchema>> tableMap = new HashMap<>();

    /**
     * Parses TableSchema type objects into a list of TableSchema objects.
     * @param table TableSchema object to parse.
     */
    public void parse(TableSchema table) {
        switch (table.schemaType()) {
            case GENTABLE:
                GenTableSchema genTable = (GenTableSchema) table;
                parseGenTable(genTable);
                break;
            case DEFTABLE:
                DefTableSchema defTable = (DefTableSchema) table;
                parseTable(defTable);
                break;
        }
    }

    /**
     * Checks whether a table has a table name, regex name, or random name.
     * @param table TableSchema object to parse.
     */
    private void parseTableName(TableSchema table) {
        if (table.getTableName() != null) {
            table.setRandomName(false);
            table.setRegexName(null);
        } else table.setRandomName(table.getRegexName() == null);
    }

    /**
     * Checks whether a tableID already exists in a database and throws an error if it does.
     * @param tableID tableID to check.
     */
    private void checkTableID(int tableID) {
        if (tableMap.containsKey(tableID)) {
            throw new SpecificationException("tableID " + tableID + " already exists");
        }
    }

    /**
     * Generates a new tableID that doesn't already exist.
     * @param r Random object used to generate tableIDs.
     * @return Unique tableID.
     */
    private int generateTableID(Random r) {
        Set<Integer> tableIDs = tableMap.keySet();

        if (tableIDs.size() == Integer.MAX_VALUE) {
            throw new SpecificationException("Max number of tables reached");
        }

        int tableID = r.nextInt();
        while (tableIDs.contains(tableID)) { tableID = r.nextInt(); }
        return tableID;
    }

    /**
     * Parses a GenTableSchema object into a list of DefTableSchema objects.
     * @param genTable GenTableSchema object to parse.
     */
    private void parseGenTable(GenTableSchema genTable) {
        genTable.validate();
        Random r = new Random();

        Integer numRows = genTable.getNumRows();
        int minRows = genTable.getMinRows();
        int maxRows = genTable.getMaxRows();

        int numTables;

        if (genTable.getNumTables() != null) {
            numTables = genTable.getNumTables();
        } else {
            int minTables = genTable.getMinTables();
            int maxTables = genTable.getMaxTables();

            numTables = r.nextInt(maxTables - minTables + 1) + minTables;
        }

        for (int i = 0; i < numTables; i++) {
            int tableID = generateTableID(r);
            if (numRows == null) {
                numRows = r.nextInt( maxRows - minRows + 1) + maxRows;
            }

            DefTableSchema defTable = new DefTableSchema();
            defTable.setColumnSchemas(genTable.getColumnSchemas()); // Not sure if I should be creating new objects here
            defTable.setRandomName(genTable.isRandomName());
            defTable.setRegexName(genTable.getRegexName());
            defTable.setTableName(genTable.getTableName());
            defTable.setTableRelationships(genTable.getTableRelationships());
            defTable.setTableID(tableID);
            defTable.setNumRows(numRows);

            parseTable(defTable);
        }

    }

    /**
     * Parses a DefTableSchema object and adds it to the tables list.
     * @param defTable DefTableSchema object to parse.
     */
    private void parseTable(DefTableSchema defTable) {
        defTable.validate();
        checkTableID(defTable.getTableID());
        parseTableName(defTable);

        /* Mapping of columnIDs to columnSchema objects */
        ColumnParser columnParser = new ColumnParser();
        for (ColumnSchema c: defTable.getColumnSchemas()) {
            columnParser.parse(c);
        }
        List<ColumnSchema> columns = new ArrayList<>(columnParser.getColumns());
        Map<Integer, ColumnSchema> columnMap = new HashMap<>(columnParser.getColumnMap());
        defTable.setColumnSchemas(columns);

        List<TableRelationshipSchema> tableRelationships = new ArrayList<>();
        TableRelationshipParser tableRelationshipParser = new TableRelationshipParser(columnMap);
        tableRelationships.sort(new Comparator<TableRelationshipSchema>() { // Puts defined relationships before general ones
            @Override
            public int compare(TableRelationshipSchema t1, TableRelationshipSchema t2) {
                if (t1.relationshipType() == t2.relationshipType()) { return 0; }
                return t1.relationshipType() == RelationshipType.DEFTABLE ? -1: 1;
            }
        });

        /*x
         * TODO: Reduce the number of TableRelationshipSchema objects to one per dependencyFunction.
         * Parsing the table relationships in this format results in one DefTableRelationshipSchema
         * object for each item in the defTable.getTableRelationships list.
         */
        for (TableRelationshipSchema tableRelationship: defTable.getTableRelationships()) {
            tableRelationships.add(tableRelationshipParser.parse(tableRelationship));
        }
        defTable.setTableRelationships(tableRelationships);

        tableMap.put(defTable.getTableID(), columnMap);
        tables.add(defTable);

    }

    public List<TableSchema> getTables() {
        return tables;
    }

    public void setTables(List<TableSchema> tables) {
        this.tables = tables;
    }

    public Map<Integer, Map<Integer, ColumnSchema>> getTableMap() {
        return tableMap;
    }

    public void setTableMap(Map<Integer, Map<Integer, ColumnSchema>> tableMap) {
        this.tableMap = tableMap;
    }
}
