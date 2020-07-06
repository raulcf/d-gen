package dgen.utils.schemas;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeName;
import dgen.utils.schemas.datatypes.DataType;

@JsonTypeName("foreignKey")
@JsonPropertyOrder({"columnID", "columnName", "regexName", "randomName", "unique", "hasNull", "nullFrequency",
        "dataType"})
public class DefForeignKeySchema implements ColumnSchema, Schema {
    private int columnID;
    private final DataType dataType = null;
    private String columnName;
    private String regexName;
    private boolean randomName = true;
    private final boolean unique = false;
    private final boolean hasNull = false;
    private final float nullFrequency = 0;

    @Override
    public String schemaType() { return "defForeignKey"; }

    @Override
    public void validate() {};

    public int getColumnID() {
        return columnID;
    }

    public void setColumnID(int columnID) {
        this.columnID = columnID;
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

    public boolean isHasNull() {
        return hasNull;
    }

    public float getNullFrequency() {
        return nullFrequency;
    }

    @Override
    public String toString() {
        return "DefForeignKeySchema{" +
                "columnID=" + columnID +
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
