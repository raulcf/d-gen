package dgen.utils;

import dgen.utils.schemas.*;

import java.util.*;

public class ColumnParser {
    private List<ColumnSchema> columns = new ArrayList<>();

    public List<ColumnSchema> getColumns() {
        return columns;
    }
    public void setColumns(List<ColumnSchema> columns) {
        this.columns = columns;
    }

    public void parse(ColumnSchema c) {
        switch (c.getColumnType()) {
            case "defined":
                parseColumn(c);
                break;
            case "general":
                parseGenColumn(c);
                break;
        }

    }

    private void parseGenColumn(ColumnSchema c) {
        Random r = new Random();

        int numColumns;

        if (c.getNumColumns() != null) {
            numColumns = c.getNumColumns();
        } else {
            int minColumns = c.getMinColumns();
            int maxColumns = c.getMaxColumns();

            assert minColumns <= maxColumns;

            numColumns = r.nextInt(maxColumns - minColumns + 1) + maxColumns;
        }

        for (int i = 0; i < numColumns; i++) {
            int columnId = r.nextInt(); // TODO: Find better way to generate tableID

            c.setColumnID(columnId);
            parseColumn(c);
        }

    }

    private void parseColumn(ColumnSchema c) {
        DefColumnSchema definedColumn = new DefColumnSchema();

        if (c.getColumnType() != null) {
            c.setRandomName(false);
            c.setRegexName(null);
        }
        else if (c.getRegexName() != null) {
            c.setRandomName(false);
        }

        DataTypeParser parser = new DataTypeParser();
        parser.parse(c.getDataType());

        definedColumn.setColumnID(c.getColumnID());
        definedColumn.setUnique(c.isUnique());
        definedColumn.setColumnName(c.getColumnName());
        definedColumn.setRandomName(c.getRandomName());
        definedColumn.setHasNull(c.isHasNull());
        definedColumn.setNullFrequency(c.getNullFrequency());
        definedColumn.setDataType(parser.getDataType());
        columns.add(definedColumn);

    }
}
