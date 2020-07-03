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

    public void parse(ColumnSchema column) {
        switch (column.schemaType()) {
            case "defined":
                DefColumnSchema defColumn = (DefColumnSchema) column;
                parseColumn(defColumn);
                break;
            case "general":
                GenColumnSchema genColumn = (GenColumnSchema) column;
                parseGenColumn(genColumn);
                break;
        }

    }

    private void parseGenColumn(GenColumnSchema genColumn) {
        Random r = new Random();

        int numColumns;

        if (genColumn.getNumColumns() != null) {
            numColumns = genColumn.getNumColumns();
        } else {
            int minColumns = genColumn.getMinColumns();
            int maxColumns = genColumn.getMaxColumns();

            assert minColumns <= maxColumns;

            numColumns = r.nextInt(maxColumns - minColumns + 1) + maxColumns;
        }

        for (int i = 0; i < numColumns; i++) {
            int columnId = r.nextInt(); // TODO: Find better way to generate tableID

            DefColumnSchema defColumn = new DefColumnSchema(); // TODO: Find smarter way of copying
            defColumn.setRandomName(genColumn.isRandomName());
            defColumn.setRegexName(genColumn.getRegexName());
            defColumn.setColumnName(genColumn.getColumnName());
            defColumn.setColumnID(columnId);

            parseColumn(defColumn);
        }

    }

    private void parseColumn(DefColumnSchema defColumn) {
        if (defColumn.getColumnName() != null) {
            defColumn.setRandomName(false);
            defColumn.setRegexName(null);
        }
        else if (defColumn.getRegexName() != null) {
            defColumn.setRandomName(false);
        }

        DataTypeParser parser = new DataTypeParser();
        parser.parse(defColumn.getDataType());
        defColumn.setDataType(parser.getDataType());

        columns.add(defColumn);

    }
}
