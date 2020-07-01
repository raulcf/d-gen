package dgen.utils.schemas;

import java.util.ArrayList;
import java.util.List;

public class tableSchema {
    private int tableID;
    private int numRows;
    private List<columnSchema> columnSchemas;
    private String tableName;
    private String regexName;
    private boolean randomName = true;

    public static final String [] requiredParameters = {"tableID", "numRows", "columnSchemas"};
    public static final String [] optionalParameters = {"tableName", "randomName", "tableRelationships", "tableConstraints"};

    public int getTableID() {
        return tableID;
    }

    public void setTableID(int tableID) {
        this.tableID = tableID;
    }

    public int getNumRows() {
        return numRows;
    }

    public void setNumRows(int numRows) {
        this.numRows = numRows;
    }

    public List<columnSchema> getColumnSchemas() {
        return columnSchemas;
    }

    public void setColumnSchemas(List<columnSchema> columnSchemas) {
        this.columnSchemas = columnSchemas;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getRegexName() {
        return regexName;
    }

    public void setRegexName(String regexName) {
        this.regexName = regexName;
    }

    public boolean isRandomName() {
        return randomName;
    }

    public void setRandomName(boolean randomName) {
        this.randomName = randomName;
    }

    @Override
    public String toString() {
        return "tableSchema{" +
                "tableID=" + tableID +
                ", numRows=" + numRows +
                ", columnSchemas=" + columnSchemas +
                ", tableName='" + tableName + '\'' +
                ", regexName='" + regexName + '\'' +
                ", randomName=" + randomName +
                '}';
    }
}
