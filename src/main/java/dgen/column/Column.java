package dgen.column;

import dgen.datatypes.DataType;

import java.util.List;

public class Column {

    private int columnID;
    private String attributeName;
    private List<DataType> data;

    public Column(int columnID, String attributeName, List<DataType> data) {
        this.columnID = columnID;
        this.attributeName = attributeName;
        this.data = data;
    }

    public int getColumnID() {
        return columnID;
    }

    public List<DataType> getData() {
        return data;
    }

    public String getAttributeName() {
        return attributeName;
    }

    @Override
    public String toString() {
        return "Column{" +
                "attributeName='" + attributeName + '\'' +
                ", data=" + data +
                '}';
    }
}
