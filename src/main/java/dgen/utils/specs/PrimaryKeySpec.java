package dgen.utils.specs;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeName;
import dgen.utils.SpecificationException;
import dgen.utils.specs.datatypespecs.DataTypeSpec;

@JsonTypeName("primaryKey")
@JsonPropertyOrder({"columnID", "columnName", "regexName", "randomName", "unique", "hasNull", "nullFrequency",
        "dataType"})
public class PrimaryKeySpec implements ColumnSpec {
    private int columnID;
    private DataTypeSpec dataTypeSpec;
    private String columnName;
    private String regexName;
    private boolean randomName = true;
    private final boolean unique = true;
    private final boolean hasNull = false;
    private final float nullFrequency = 0;

    @Override
    public SpecType specType() { return SpecType.PRIMARYKEY; }

    @Override
    public void validate() {
        if (dataTypeSpec == null) {
            throw new SpecificationException("Primary key with columnID " + columnID + " missing datatype");
        }

        dataTypeSpec.validate();
    }

    public Integer getColumnID() {
        return columnID;
    }

    public void setColumnID(int columnID) {
        this.columnID = columnID;
    }

    public DataTypeSpec getDataTypeSpec() {
        return dataTypeSpec;
    }

    public void setDataTypeSpec(DataTypeSpec dataTypeSpec) {
        this.dataTypeSpec = dataTypeSpec;
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
        return "PrimaryKeySchema{" +
                "columnID=" + columnID +
                ", dataType=" + dataTypeSpec +
                ", columnName='" + columnName + '\'' +
                ", regexName='" + regexName + '\'' +
                ", randomName=" + randomName +
                ", unique=" + unique +
                ", hasNull=" + hasNull +
                ", nullFrequency=" + nullFrequency +
                '}';
    }
}
