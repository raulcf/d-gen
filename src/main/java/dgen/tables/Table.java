package dgen.tables;

import dgen.datatypes.DataType;

import java.util.*;

public class Table {

    private Map<Integer, String> columnNames;
    // Column or row orientation will depend on serialization method
    private ArrayList<ArrayList<DataType>> data;
    private String attributeName;
    private Map<Integer, String> columnNameMap;
    private int tableID;

//    public Table(int tableID, String attributeName, List<String> columnNames, ArrayList<ArrayList<DataType>> data,
//                 Map<Integer, String> columnNameMap) {
//        this.tableID = tableID;
//        this.attributeName = attributeName;
//        this.columnNames = columnNames;
//        this.data = data;
//        this.columnNameMap = columnNameMap;
//    }

    public Table(int tableID, String attributeName, Map<Integer, String> columnNames) {
        this.tableID = tableID;
        this.attributeName = attributeName;
        this.columnNames = columnNames;

    }

    public int getTableID() {
        return tableID;
    }

    public String getColumnName(int columnID) { return columnNameMap.get(columnID); }

    public ArrayList<ArrayList<DataType>> getData() {
        return data;
    }

    public Map<Integer, String> getColumnNames() { return columnNames; }

    public String getAttributeName() {
        return attributeName;
    }
}
