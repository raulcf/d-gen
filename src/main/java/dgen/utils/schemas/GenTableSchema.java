package dgen.utils.schemas;

import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.List;

@JsonTypeName("genTable")
public class GenTableSchema implements TableSchema {

    private Integer numRows = null;
    private Integer minRows = 1;
    private Integer maxRows = 100; // TODO: Decide on this later
    private Integer numTables;
    private Integer minTables = 1;
    private Integer maxTables = 100; // TODO: Decide on this later
    private List<ColumnSchema> columnSchemas;
    private String tableName;
    private String regexName;
    private boolean randomName = true;

    @Override
    public String schemaType() { return "general"; }

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

    public Integer getMinRows() {
        return minRows;
    }

    public void setMinRows(Integer minRows) {
        this.minRows = minRows;
    }

    public Integer getMaxRows() {
        return maxRows;
    }

    public void setMaxRows(Integer maxRows) {
        this.maxRows = maxRows;
    }

    public Integer getNumTables() {
        return numTables;
    }

    public void setNumTables(Integer numTables) {
        this.numTables = numTables;
    }

    public Integer getMinTables() {
        return minTables;
    }

    public void setMinTables(Integer minTables) {
        this.minTables = minTables;
    }

    public Integer getMaxTables() {
        return maxTables;
    }

    public void setMaxTables(Integer maxTables) {
        this.maxTables = maxTables;
    }

    @Override
    public String toString() {
        return "GenTableSchema{" +
                "numRows=" + numRows +
                ", minRows=" + minRows +
                ", maxRows=" + maxRows +
                ", numTables=" + numTables +
                ", minTables=" + minTables +
                ", maxTables=" + maxTables +
                ", columnSchemas=" + columnSchemas +
                ", tableName='" + tableName + '\'' +
                ", regexName='" + regexName + '\'' +
                ", randomName=" + randomName +
                '}';
    }
}
