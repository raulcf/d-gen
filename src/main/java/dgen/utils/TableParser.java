package dgen.utils;

import dgen.utils.schemas.*;
import dgen.utils.schemas.relationships.TableRelationshipSchema;

import java.util.*;

public class TableParser {
    private List<TableSchema> tables = new ArrayList<>();

    public List<TableSchema> getTables() {
        return tables;
    }
    public void setTables(List<TableSchema> tables) {
        this.tables = tables;
    }

    /**
     * Parses TableSchema type objects into a list of TableSchema objects.
     * @param table TableSchema object to parse.
     */
    public void parse(TableSchema table) {
        switch (table.schemaType()) {
            case "genTable":
                GenTableSchema genTable = (GenTableSchema) table;
                parseGenTable(genTable);
                break;
            case "defTable":
                DefTableSchema defTable = (DefTableSchema) table;
                parseTable(defTable);
                break;
        }
    }

    /**
     * Parses a GenTableSchema object into a list of DefTableSchema objects.
     * @param genTable
     */
    private void parseGenTable(GenTableSchema genTable) {
        Random r = new Random();

        genTable.validate();

        Integer numRows = genTable.getNumRows();
        int minRows = genTable.getMinRows();
        int maxRows = genTable.getMaxRows();

        assert minRows <= maxRows;

        int numTables;

        if (genTable.getNumTables() != null) {
            numTables = genTable.getNumTables();
        } else {
            int minTables = genTable.getMinTables();
            int maxTables = genTable.getMaxTables();

            assert minTables <= maxTables;

            numTables = r.nextInt(maxTables - minTables + 1) + minTables;
        }

        for (int i = 0; i < numTables; i++) {
            int tableID = r.nextInt(); // TODO: Find better way to generate tableID
            if (numRows == null) {
                numRows = r.nextInt( maxRows - minRows + 1) + maxRows;
            }

            DefTableSchema defTable = new DefTableSchema();
            defTable.setColumnSchemas(genTable.getColumnSchemas());
            defTable.setRandomName(genTable.isRandomName());
            defTable.setRegexName(genTable.getRegexName());
            defTable.setTableName(genTable.getTableName());
            defTable.setTableID(tableID);
            defTable.setNumRows(numRows);

            parseTable(defTable);
        }

    }

    /**
     * Parses the name and columns of a DefTableSchema object.
     * @param defTable DefTableSchema object to parse.
     */
    private void parseTable(DefTableSchema defTable) {
        if (defTable.getTableName() != null) {
            defTable.setRandomName(false);
            defTable.setRegexName(null);
        }
        else if (defTable.getRegexName() != null) {
            defTable.setRandomName(false);
        }

        List<ColumnSchema> columns = new ArrayList<>();
        Map<Integer, ColumnSchema> columnIDMap = new HashMap<>();
        for (ColumnSchema c: defTable.getColumnSchemas()) {
            ColumnParser parser = new ColumnParser();
            parser.parse(c);
            columns.addAll(parser.getColumns());
            columnIDMap.putAll(parser.getColumnIDMap());
        }
        defTable.setColumnSchemas(columns);
        defTable.validate();

        List<TableRelationshipSchema> tableRelationships = new ArrayList<>();
        TableRelationshipParser parser = new TableRelationshipParser(columnIDMap);
        for (TableRelationshipSchema tr: defTable.getTableRelationships()) {
            tableRelationships.add(parser.parse(tr));
        }
        defTable.setTableRelationships(tableRelationships);

        tables.add(defTable);

    }

}
