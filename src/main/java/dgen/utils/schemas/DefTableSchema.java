package dgen.utils.schemas;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.List;

@JsonTypeName("defTable")
@JsonPropertyOrder({"tableID", "tableName", "numRows", "regexName", "randomName", "columnSchemas"})
public class DefTableSchema implements TableSchema, Schema {

    private int tableID;
    private Integer numRows = null;
    private List<ColumnSchema> columnSchemas;
    private String tableName;
    private String regexName;
    private boolean randomName = true;

    @Override
    public String schemaType() { return "defTable"; }

    @Override
    public void validate() {
        //TODO: Check whether there are duplicate columnIDs. It might be better to check while parsing.
    }

    public int getTableID() {
        return tableID;
    }

    public void setTableID(int tableID) {
        this.tableID = tableID;
    }

    public Integer getNumRows() {
        return numRows;
    }

    public void setNumRows(Integer numRows) {
        this.numRows = numRows;
    }

    public List<ColumnSchema> getColumnSchemas() {
        return columnSchemas;
    }

    public void setColumnSchemas(List<ColumnSchema> columnSchemas) {
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
        return "DefTableSchema{" +
                "tableID=" + tableID +
                ", numRows=" + numRows +
                ", columnSchemas=" + columnSchemas +
                ", tableName='" + tableName + '\'' +
                ", regexName='" + regexName + '\'' +
                ", randomName=" + randomName +
                '}';
    }
}