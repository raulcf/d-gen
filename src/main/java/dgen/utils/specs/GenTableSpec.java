package dgen.utils.specs;

import com.fasterxml.jackson.annotation.JsonTypeName;
import dgen.utils.SpecificationException;
import dgen.utils.specs.relationships.TableRelationshipSpec;

import java.util.ArrayList;
import java.util.List;

@JsonTypeName("genTable")
public class GenTableSpec implements TableSpec {

    private Integer numRows = null;
    private Integer minRows = 1;
    private Integer maxRows = 100; // TODO: Decide on this later
    private Integer numTables;
    private Integer minTables = 1;
    private Integer maxTables = 100; // TODO: Decide on this later
    private List<ColumnSpec> columnSpecs;
    private String tableName;
    private String regexName;
    private boolean randomName = true;
    private List<TableRelationshipSpec> tableRelationships = new ArrayList<>();
    private final Long randomSeed = null;

    @Override
    public SpecType specType() { return SpecType.GENTABLE; }

    @Override
    public void validate() {
        if (minRows > maxRows) {
            throw new SpecificationException("genTable minRows of " + minRows + " greater than maxColumns of "
                    + maxRows);
        }
        if (minTables > maxTables) {
            throw new SpecificationException("genTable minTables of " + minTables + " greater than maxTables of "
                    + maxTables);
        }
    }

    public Integer getTableID() { return null; }

    public Integer getNumRows() {
        return numRows;
    }

    public void setNumRows(Integer numRows) {
        this.numRows = numRows;
    }

    public List<ColumnSpec> getColumnSpecs() {
        return columnSpecs;
    }

    public void setColumnSpecs(List<ColumnSpec> columnSpecs) {
        this.columnSpecs = columnSpecs;
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

    public List<TableRelationshipSpec> getTableRelationships() {
        return tableRelationships;
    }

    public void setTableRelationships(List<TableRelationshipSpec> tableRelationships) {
        this.tableRelationships = tableRelationships;
    }

    public Long getRandomSeed() {
        return randomSeed;
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
                ", columnSchemas=" + columnSpecs +
                ", tableName='" + tableName + '\'' +
                ", regexName='" + regexName + '\'' +
                ", randomName=" + randomName +
                '}';
    }
}
