package dgen.utils;

import dgen.utils.schemas.ColumnSchema;
import dgen.utils.schemas.DefTableSchema;
import dgen.utils.schemas.GenTableSchema;
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

    public void parse(TableSchema table) {
        switch (table.schemaType()) {
            case "general":
                GenTableSchema genTable = (GenTableSchema) table;
                parseGenTable(genTable);
                break;
            case "defined":
                DefTableSchema defTable = (DefTableSchema) table;
                parseTable(defTable);
                break;
        }
    }

    private void parseGenTable(GenTableSchema genTable) {
        Random r = new Random();

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

    private void parseTable(DefTableSchema defTable) {
        if (defTable.getTableName() != null) {
            defTable.setRandomName(false);
            defTable.setRegexName(null);
        }
        else if (defTable.getRegexName() != null) {
            defTable.setRandomName(false);
        }

        List<ColumnSchema> columns = new ArrayList<>();
        for (ColumnSchema c: defTable.getColumnSchemas()) {
            ColumnParser parser = new ColumnParser();
            parser.parse(c);
            columns.addAll(parser.getColumns());
        }

        tables.add(defTable);

    }

    private void parseRelationships() {}

    private void parseGenRelationships() {}

    private void parseDefRelationships() {}

}
