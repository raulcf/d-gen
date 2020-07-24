package dgen.utils;

import dgen.utils.specs.ColumnSpec;
import dgen.utils.specs.DefTableSpec;
import dgen.utils.specs.GenTableSpec;
import dgen.utils.specs.TableSpec;
import dgen.utils.specs.relationships.RelationshipType;
import dgen.utils.specs.relationships.TableRelationshipSpec;

import java.util.*;


public class TableParser {
    private List<TableSpec> tables = new ArrayList<>();
    /* Mapping of tableIDs to a mapping of columns */
    private Map<Integer, Map<Integer, ColumnSpec>> tableMap = new HashMap<>();

    private RandomGenerator rnd;

    public TableParser(RandomGenerator rnd) { this.rnd = rnd; }

    /**
     * Parses TableSchema type objects into a list of TableSchema objects.
     * @param table TableSchema object to parse.
     */
    public void parse(TableSpec table) {
        switch (table.specType()) {
            case GENTABLE:
                GenTableSpec genTable = (GenTableSpec) table;
                parseGenTable(genTable);
                break;
            case DEFTABLE:
                DefTableSpec defTable = (DefTableSpec) table;
                parseTable(defTable);
                break;
        }
    }

    /**
     * Checks whether a table has a table name, regex name, or random name.
     * @param table TableSchema object to parse.
     */
    private void parseTableName(TableSpec table) {
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
     * @return Unique tableID.
     */
    private int generateTableID() {
        Set<Integer> tableIDs = tableMap.keySet();

        if (tableIDs.size() == Integer.MAX_VALUE) {
            throw new SpecificationException("Max number of tables reached");
        }

        int tableID = rnd.nextInt();
        while (tableIDs.contains(tableID)) { tableID = rnd.nextInt(); }
        return tableID;
    }

    /**
     * Parses a GenTableSchema object into a list of DefTableSchema objects.
     * @param genTable GenTableSchema object to parse.
     */
    private void parseGenTable(GenTableSpec genTable) {
        genTable.validate();

        Integer numRows = genTable.getNumRows();
        int minRows = genTable.getMinRows();
        int maxRows = genTable.getMaxRows();

        int numTables;

        if (genTable.getNumTables() != null) {
            numTables = genTable.getNumTables();
        } else {
            int minTables = genTable.getMinTables();
            int maxTables = genTable.getMaxTables();

            numTables = rnd.nextInt(maxTables - minTables) + minTables;
        }

        for (int i = 0; i < numTables; i++) {
            int tableID = generateTableID();
            if (numRows == null) {
                numRows = rnd.nextInt( maxRows - minRows) + maxRows;
            }

            DefTableSpec defTable = new DefTableSpec();
            defTable.setColumnSpecs(genTable.getColumnSpecs()); // Not sure if I should be creating new objects here
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
    private void parseTable(DefTableSpec defTable) {
        defTable.validate();
        checkTableID(defTable.getTableID());
        parseTableName(defTable);

        if (defTable.getRandomSeed() == null) {
            defTable.setRandomSeed(rnd.nextLong());
        }

        /* Mapping of columnIDs to columnSchema objects */
        ColumnParser columnParser = new ColumnParser(rnd);
        for (ColumnSpec c: defTable.getColumnSpecs()) {
            columnParser.parse(c);
        }
        List<ColumnSpec> columns = new ArrayList<>(columnParser.getColumns());
        Map<Integer, ColumnSpec> columnMap = new HashMap<>(columnParser.getColumnMap());
        defTable.setColumnSpecs(columns);

        List<TableRelationshipSpec> tableRelationships = new ArrayList<>();
        TableRelationshipParser tableRelationshipParser = new TableRelationshipParser(columnMap, rnd);
        tableRelationships.sort(new Comparator<TableRelationshipSpec>() { // Puts defined relationships before general ones
            @Override
            public int compare(TableRelationshipSpec t1, TableRelationshipSpec t2) {
                if (t1.relationshipType() == t2.relationshipType()) { return 0; }
                return t1.relationshipType() == RelationshipType.DEFTABLE ? -1: 1;
            }
        });

        for (TableRelationshipSpec tableRelationship: defTable.getTableRelationships()) {
            tableRelationships.add(tableRelationshipParser.parse(tableRelationship));
        }
        defTable.setTableRelationships(tableRelationships);

        tableMap.put(defTable.getTableID(), columnMap);
        tables.add(defTable);

    }

    public List<TableSpec> getTables() {
        return tables;
    }

    public void setTables(List<TableSpec> tables) {
        this.tables = tables;
    }

    public Map<Integer, Map<Integer, ColumnSpec>> getTableMap() {
        return tableMap;
    }

    public void setTableMap(Map<Integer, Map<Integer, ColumnSpec>> tableMap) {
        this.tableMap = tableMap;
    }
}
