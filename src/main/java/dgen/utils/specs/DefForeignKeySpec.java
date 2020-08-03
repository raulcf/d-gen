package dgen.utils.specs;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeName;
import dgen.utils.specs.datatypespecs.DataTypeSpec;

@JsonTypeName("foreignKey")
@JsonPropertyOrder({"columnID", "randomSeed", "columnName", "regexName", "randomName", "unique", "hasNull", "nullFrequency",
        "dataType"})
public class DefForeignKeySpec implements ColumnSpec {
    private int columnID;
    private String columnName;
    private String regexName;
    private boolean randomName = true;
    private boolean unique = false;
    private boolean hasNull = false;
    private float nullFrequency = 0;
    private Long randomSeed;

    @Override
    public SpecType specType() { return SpecType.DEFFOREIGNKEY; }

    @Override
    public void validate() {}

    public Integer getColumnID() {
        return columnID;
    }

    public void setColumnID(int columnID) {
        this.columnID = columnID;
    }

    public DataTypeSpec getDataTypeSpec() {
        return null;
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

    public Long getRandomSeed() {
        return randomSeed;
    }

    public void setRandomSeed(Long randomSeed) {
        this.randomSeed = randomSeed;
    }

    @Override
    public String toString() {
        return "DefForeignKeySchema{" +
                "columnID=" + columnID +
                ", columnName='" + columnName + '\'' +
                ", regexName='" + regexName + '\'' +
                ", randomName=" + randomName +
                ", unique=" + unique +
                ", hasNull=" + hasNull +
                ", nullFrequency=" + nullFrequency +
                '}';
    }
}
