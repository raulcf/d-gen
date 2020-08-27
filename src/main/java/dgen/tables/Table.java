package dgen.tables;

import java.util.Map;

/**
 * This class is mainly just for storing name/ID info. for metadata and serialization.
 */
public class Table {

    private Map<Integer, String> columnNames;
    private String attributeName;
    private Map<Integer, String> columnNameMap;
    private int tableID;

    public Table(int tableID, String attributeName, Map<Integer, String> columnNames) {
        this.tableID = tableID;
        this.attributeName = attributeName;
        this.columnNames = columnNames;

    }

    public int getTableID() {
        return tableID;
    }

    public String getColumnName(int columnID) { return columnNameMap.get(columnID); }

    public Map<Integer, String> getColumnNames() { return columnNames; }

    public String getAttributeName() {
        return attributeName;
    }
}
