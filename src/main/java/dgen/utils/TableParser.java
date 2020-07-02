package dgen.utils;

import dgen.utils.schemas.ColumnSchema;
import dgen.utils.schemas.DefTableSchema;
import dgen.utils.schemas.TableSchema;

import java.util.*;

public class TableParser {
    private List<TableSchema> tables = new ArrayList<>();

    public List<TableSchema> getTables() {
        return tables;
    }
    public void setTables(List<TableSchema> tables) {
        this.tables = tables;
    }

    public void parse(TableSchema t) {
        switch (t.getTableType()) {
            case "defined":
                parseTable(t);
                break;
            case "general":
                parseGenTable(t);
                break;
            default:
                ; // TODO: Throw error
                break;
        }
        
    }

    private void parseGenTable(TableSchema t) {
        Random r = new Random();

        Integer numRows = t.getNumRows();
        int minRows = t.getMinRows();
        int maxRows = t.getMaxRows();

        assert minRows <= maxRows;

        int numTables;

        if (t.getNumTables() != null) {
            numTables = t.getNumTables();
        } else {
            int minTables = t.getMinTables();
            int maxTables = t.getMaxTables();

            assert minTables <= maxTables;

            numTables = r.nextInt(maxTables - minTables + 1) + minTables;
        }

        for (int i = 0; i < numTables; i++) {
            int tableID = r.nextInt(); // TODO: Find better way to generate tableID
            if (numRows == null) {
                numRows = r.nextInt( maxRows - minRows + 1) + maxRows;
            }

            t.setTableID(tableID);
            t.setNumRows(numRows);

            parseTable(t);
        }

    }

    private void parseTable(TableSchema t) {
        DefTableSchema definedTable = new DefTableSchema();

        if (t.getTableName() != null) {
            t.setRandomName(false);
            t.setRegexName(null);
        }
        else if (t.getRegexName() != null) {
            t.setRandomName(false);
        }

        List<ColumnSchema> columns = new ArrayList<>();
        for (ColumnSchema c: t.getColumnSchemas()) {
            ColumnParser parser = new ColumnParser();
            parser.parse(c);
            columns.addAll(parser.getColumns());
        }

        definedTable.setTableID(t.getTableID());
        definedTable.setNumRows(t.getNumRows());
        definedTable.setTableName(t.getTableName());
        definedTable.setRandomName(t.getRandomName());
        definedTable.setColumnSchemas(columns);

        tables.add(definedTable);

    }

}
