package dgen.utils.schemas;

import java.util.List;

public class DefTableSchema extends TableSchema {

    public static final String [] REQUIRED_PARAMETERS = {"tableID", "numRows", "columnSchemas"};
    public static final String [] OPTIONAL_PARAMETERS = {"tableName", "randomName", "tableRelationships", "tableConstraints"};

    private final String tableType = "defined";
    private int tableID;
    private Integer numRows = null;
    private List<ColumnSchema> columnSchemas;
    private String tableName;
    private String regexName;
    private boolean randomName = true;

    @Override
    public String getTableType() {
        return tableType;
    }

    @Override
    public int getTableID() {
        return tableID;
    }

    @Override
    public void setTableID(int tableID) {
        this.tableID = tableID;
    }

    @Override
    public Integer getNumRows() {
        return numRows;
    }

    @Override
    public void setNumRows(Integer numRows) {
        this.numRows = numRows;
    }

    @Override
    public List<ColumnSchema> getColumnSchemas() {
        return columnSchemas;
    }

    @Override
    public void setColumnSchemas(List<ColumnSchema> columnSchemas) {
        this.columnSchemas = columnSchemas;
    }

    @Override
    public String getTableName() {
        return tableName;
    }

    @Override
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public String getRegexName() {
        return regexName;
    }

    @Override
    public void setRegexName(String regexName) {
        this.regexName = regexName;
    }

    public boolean isRandomName() {
        return randomName;
    }

    @Override
    public void setRandomName(boolean randomName) {
        this.randomName = randomName;
    }

    @Override
    public String toString() {
        return "DefTableSchema{" +
                "tableType='" + tableType + '\'' +
                ", tableID=" + tableID +
                ", numRows=" + numRows +
                ", columnSchemas=" + columnSchemas +
                ", tableName='" + tableName + '\'' +
                ", regexName='" + regexName + '\'' +
                ", randomName=" + randomName +
                '}';
    }
}
