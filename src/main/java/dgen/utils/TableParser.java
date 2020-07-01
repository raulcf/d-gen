package dgen.utils;

import dgen.utils.schemas.columnSchema;
import dgen.utils.schemas.tableSchema;

import java.util.*;

public class TableParser {
    private List<tableSchema> tables;

    public List<tableSchema> getTables() {
        return tables;
    }

    public void setTables(List<tableSchema> tables) {
        this.tables = tables;
    }

    public TableParser() { tables = new ArrayList<>(); }

    public void parse(Map<String, Object> tableParameters) {
        String tableType = tableParameters.keySet().iterator().next(); // Ugly

        switch (tableType) {
            case "table":
                parseTable((Map<String, Object>) tableParameters.get("table"));
                break;
            case "genTable":
                parseGenTable((Map<String, Object>) tableParameters.get("genTable"));
                break;
        }
        
    }

    private void parseGenTable(Map<String, Object> tableParameters) {
        Integer numRows = null;
        Integer numTables = null;
        int minRows = 1;
        int maxRows = 100; // TODO: Decide on this later
        int tableID;

        Random r = new Random();

        if (tableParameters.containsKey("numTables")) {
            numTables = (int) tableParameters.get("numTables");
        } else if (tableParameters.containsKey("minTables") && tableParameters.containsKey("maxTables")) {
            int minTables = (int) tableParameters.get("minTables");
            int maxTables = (int) tableParameters.get("maxTables");

            tableParameters.remove("minTables");
            tableParameters.remove("maxTables");

            if (minTables > maxTables) {
                ; //TODO: Throw error
            } else {
                numTables = r.nextInt(maxTables - minTables + 1) + minTables;
            }

        } else {
            ; //TODO: Throw error
        }

        if (tableParameters.containsKey("numRows")) {
            numRows = (int) tableParameters.get("numRows");
        } else if (tableParameters.containsKey("minRows") && tableParameters.containsKey("maxRows")) {
            minRows = (int) tableParameters.get("minRows");
            maxRows = (int) tableParameters.get("maxRows");

            tableParameters.remove("minRows");
            tableParameters.remove("maxRows");

            if (minRows > maxRows) { } //TODO: Throw error

        } else {
            ; //TODO: Throw error
        }

        for (int i = 0; i < numTables; i++) {
            Map<String, Object> definedTableParameters = new HashMap<>(tableParameters);
            tableID = r.nextInt(); // TODO: Find better way to generate tableID

            if (numRows == null) {
                numRows = r.nextInt( maxRows - minRows + 1) + maxRows;
            }

            definedTableParameters.put("tableID", tableID);
            definedTableParameters.put("numRows", numRows);

            parseTable(definedTableParameters);
        }

    }

    private void parseTable(Map<String, Object> tableParameters) {
        for (String parameter: tableSchema.requiredParameters) {
            if (!(tableParameters.containsKey(parameter))) {
                ; //TODO: Throw error
            }
        }

        tableSchema table = new tableSchema();
        List<columnSchema> columns = new ArrayList<>();
        table.setTableID((Integer) tableParameters.get("tableID"));
        table.setNumRows((Integer) tableParameters.get("numRows"));

        if (tableParameters.containsKey("tableName")) {
            table.setTableName((String) tableParameters.get("tableName"));
            table.setRandomName(false);
        } else if (tableParameters.containsKey("regexName")) {
            table.setRegexName((String) tableParameters.get("regexName"));
            table.setRandomName(false);
        }

        for (HashMap<String, Object> columnSchema: (List<HashMap<String, Object>>) tableParameters.get("columnSchemas")) {
            ColumnParser parser = new ColumnParser();
            parser.parse(columnSchema);
            columns.addAll(parser.getColumns());
        }

        table.setColumnSchemas(columns);
        tables.add(table);

    }

}
