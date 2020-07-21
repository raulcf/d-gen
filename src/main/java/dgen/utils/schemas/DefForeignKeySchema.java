package dgen.utils.schemas;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeName;
import dgen.utils.schemas.datatypeschemas.DataTypeSchema;

@JsonTypeName("foreignKey")
@JsonPropertyOrder({"columnID", "columnName", "regexName", "randomName", "unique", "hasNull", "nullFrequency",
        "dataType"})
public class DefForeignKeySchema implements ColumnSchema {
    private int columnID;
    private final DataTypeSchema dataTypeSchema = null;
    private String columnName;
    private String regexName;
    private boolean randomName = true;
    private boolean unique = false;
    private boolean hasNull = false;
    private float nullFrequency = 0;

    @Override
    public SchemaType schemaType() { return SchemaType.DEFFOREIGNKEY; }

    @Override
    public void validate() {}

    public Integer getColumnID() {
        return columnID;
    }

    public void setColumnID(int columnID) {
        this.columnID = columnID;
    }

    public DataTypeSchema getDataTypeSchema() {
        return dataTypeSchema;
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
        return "DefForeignKeySchema{" +
                "columnID=" + columnID +
                ", dataType=" + dataTypeSchema +
                ", columnName='" + columnName + '\'' +
                ", regexName='" + regexName + '\'' +
                ", randomName=" + randomName +
                ", unique=" + unique +
                ", hasNull=" + hasNull +
                ", nullFrequency=" + nullFrequency +
                '}';
    }
}
