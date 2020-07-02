package dgen.utils.schemas;

public class DefColumnSchema extends ColumnSchema {

    public static final String [] REQUIRED_PARAMETERS = {"columnID", "dataType"};
    public static final String [] OPTIONAL_PARAMETERS = {"columnName", "regexName", "randomName", "unique", "hasNull", "nullFrequency"};

    private int columnID;
    private final String columnType = "defined";
    private Object dataType; // Will change this to more specific type
    private String columnName;
    private String regexName;
    private boolean randomName = true;
    private boolean unique = false;
    private boolean hasNull = false;
    private float nullFrequency = 0;

    @Override
    public int getColumnID() {
        return columnID;
    }

    @Override
    public void setColumnID(int columnID) {
        this.columnID = columnID;
    }

    @Override
    public String getColumnType() {
        return columnType;
    }

    @Override
    public Object getDataType() {
        return dataType;
    }

    @Override
    public void setDataType(Object dataType) {
        this.dataType = dataType;
    }

    @Override
    public String getColumnName() {
        return columnName;
    }

    @Override
    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    @Override
    public String getRegexName() {
        return regexName;
    }

    @Override
    public void setRegexName(String regexName) {
        this.regexName = regexName;
    }

    @Override
    public boolean isRandomName() {
        return randomName;
    }

    @Override
    public void setRandomName(boolean randomName) {
        this.randomName = randomName;
    }

    @Override
    public boolean isUnique() {
        return unique;
    }

    @Override
    public void setUnique(boolean unique) {
        this.unique = unique;
    }

    @Override
    public boolean isHasNull() {
        return hasNull;
    }

    @Override
    public void setHasNull(boolean hasNull) {
        this.hasNull = hasNull;
    }

    @Override
    public float getNullFrequency() {
        return nullFrequency;
    }

    @Override
    public void setNullFrequency(float nullFrequency) {
        this.nullFrequency = nullFrequency;
    }

    @Override
    public String toString() {
        return "DefColumnSchema{" +
                "columnID=" + columnID +
                ", columnType='" + columnType + '\'' +
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
