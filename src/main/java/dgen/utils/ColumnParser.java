package dgen.utils;

import dgen.utils.schemas.columnSchema;

import java.util.*;

public class ColumnParser {
    private List<columnSchema> columns;

    public List<columnSchema> getColumns() {
        return columns;
    }

    public void setColumns(List<columnSchema> columns) {
        this.columns = columns;
    }

    public ColumnParser() { columns = new ArrayList<>(); }

    public void parse(Map<String, Object> columnParameters) {
        String columnType = columnParameters.keySet().iterator().next(); // Ugly

        switch (columnType) {
            case "column":
                parseColumn((Map<String, Object>) columnParameters.get("column"));
                break;
            case "genColumn":
                parseGenColumn((Map<String, Object>) columnParameters.get("genColumn"));
                break;
        }

    }

    private void parseGenColumn(Map<String, Object> columnParameters) {
        Integer numColumns = null;
        int minColumns = 1;
        int maxColumns = 100; // TODO: Decide on this later
        int tableID;

        Random r = new Random();

        if (columnParameters.containsKey("numColumns")) {
            numColumns = (int) columnParameters.get("numColumns");
        } else if (columnParameters.containsKey("minColumns") && columnParameters.containsKey("maxColumns")) {
            minColumns = (int) columnParameters.get("minColumns");
            maxColumns = (int) columnParameters.get("maxColumns");

            columnParameters.remove("minColumns");
            columnParameters.remove("maxColumns");

            if (minColumns > maxColumns) {
                ; //TODO: Throw error
            } else {
                numColumns = r.nextInt(maxColumns - minColumns + 1) + minColumns;
            }

        } else {
            ; //TODO: Throw error
        }

        for (int i = 0; i < numColumns; i++) {
            Map<String, Object> definedColumnParameters = new HashMap<>(columnParameters);
            tableID = r.nextInt(); // TODO: Find better way to generate tableID

            if (numColumns == null) {
                numColumns = r.nextInt( maxColumns - minColumns + 1) + maxColumns;
            }

            definedColumnParameters.put("columnID", tableID);

            parseColumn(definedColumnParameters);
        }

    }

    private void parseColumn(Map<String, Object> columnParameters) {
        for (String parameter: columnSchema.requiredParameters) {
            if (!(columnParameters.containsKey(parameter))) {
                ; //TODO: Throw error
            }
        }
        columnSchema column = new columnSchema();
        column.setColumnID((Integer) columnParameters.get("columnID"));
        //TODO: Parse datatype

        if (columnParameters.containsKey("columnName")) {
            column.setColumnName((String) columnParameters.get("columnName"));
            column.setRandomName(false);
        } else if (columnParameters.containsKey("regexName")) {
            column.setRegexName((String) columnParameters.get("regexName"));
            column.setRandomName(false);
        }
        if (columnParameters.containsKey("unique"))
            column.setUnique(Boolean.parseBoolean((String) columnParameters.get("unique")));

        if (columnParameters.containsKey("null")) {
            column.setHasNull(Boolean.parseBoolean((String) columnParameters.get("null")));

            if (columnParameters.containsKey("nullFrequency"))
                column.setNullFrequency(Float.parseFloat((String) columnParameters.get("nullFrequency")));
        }

        columns.add(column);

    }
}
