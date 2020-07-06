package dgen.utils.schemas;

import dgen.utils.SpecificationException;
import dgen.utils.schemas.datatypes.DataType;
import dgen.utils.schemas.datatypes.Int;

public class GenForeignKeySchema implements ColumnSchema, Schema {
    private Integer numColumns = null;
    private Integer minColumns = 1;
    private Integer maxColumns = 100; // TODO: Decide on this later
    private final DataType dataType = null;
    private String columnName;
    private String regexName;
    private boolean randomName = true;
    private boolean unique = false;
    private boolean hasNull = false;
    private float nullFrequency;

    @Override
    public String schemaType() {
        return "genForeignKey";
    }

    @Override
    public void validate() {
        if (minColumns > maxColumns) {
            throw new SpecificationException("genForeignKey minColumns of " + minColumns + " greater than maxColumns of "
                    + maxColumns);
        }
    }

    public Integer getNumColumns() {
        return numColumns;
    }

    public void setNumColumns(Integer numColumns) {
        this.numColumns = numColumns;
    }

    public Integer getMinColumns() {
        return minColumns;
    }

    public void setMinColumns(Integer minColumns) {
        this.minColumns = minColumns;
    }

    public Integer getMaxColumns() {
        return maxColumns;
    }

    public void setMaxColumns(Integer maxColumns) {
        this.maxColumns = maxColumns;
    }

    public DataType getDataType() {
        return dataType;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
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

    public boolean isUnique() {
        return unique;
    }

    public void setUnique(boolean unique) {
        this.unique = unique;
    }

    public boolean isHasNull() {
        return hasNull;
    }

    public void setHasNull(boolean hasNull) {
        this.hasNull = hasNull;
    }

    public float getNullFrequency() {
        return nullFrequency;
    }

    public void setNullFrequency(float nullFrequency) {
        this.nullFrequency = nullFrequency;
    }

    @Override
    public String toString() {
        return "GenForeignKeySchema{" +
                "numColumns=" + numColumns +
                ", minColumns=" + minColumns +
                ", maxColumns=" + maxColumns +
                ", dataType=" + dataType +
                ", columnName='" + columnName + '\'' +
                ", regexName='" + regexName + '\'' +
                ", randomName=" + randomName +
                ", unique=" + unique +
                ", hasNull=" + hasNull +
                ", nullFrequency=" + nullFrequency +
                '}';
    }
}
