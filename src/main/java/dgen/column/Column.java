package dgen.column;

import dgen.datatypes.DataType;

import java.util.List;

public class Column {

    private String attributeName;
    private List<DataType> data;

    public Column(String attributeName, List<DataType> data) {
        this.attributeName = attributeName;
        this.data = data;
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
