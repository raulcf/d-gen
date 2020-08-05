package dgen.utils.parsers.specs;

import com.fasterxml.jackson.annotation.JsonTypeName;
import dgen.utils.parsers.SpecificationException;
import dgen.utils.parsers.specs.datatypespecs.DataTypeSpec;

@JsonTypeName("genColumn")
public class GenColumnSpec implements ColumnSpec {

    private Integer numColumns = null;
    private Integer minColumns = 1;
    private Integer maxColumns = 100; // TODO: Decide on this later
    private DataTypeSpec dataTypeSpec;
    private String columnName;
    private String regexName;
    private boolean randomName = true;
    private boolean unique = false;
    private boolean hasNull = false;
    private float nullFrequency;
    private Long randomSeed = null;
    
    @Override
    public SpecType specType() { return SpecType.GENCOLUMN; }

    @Override
    public void validate() {
        if (minColumns > maxColumns) {
            throw new SpecificationException("genColumn minColumns of " + minColumns + " greater than maxColumns of "
                    + maxColumns);
        }
    }

    @Override
    public GenColumnSpec copy() {
        GenColumnSpec genColumnSpec = new GenColumnSpec();
        genColumnSpec.setDataTypeSpec(dataTypeSpec.copy());
        genColumnSpec.setMinColumns(minColumns);
        genColumnSpec.setMaxColumns(maxColumns);
        genColumnSpec.setNumColumns(numColumns);
        genColumnSpec.setColumnName(columnName);
        genColumnSpec.setRegexName(regexName);
        genColumnSpec.setRandomName(randomName);
        genColumnSpec.setUnique(unique);
        genColumnSpec.setHasNull(hasNull);
        genColumnSpec.setNullFrequency(nullFrequency);
        genColumnSpec.setRandomSeed(randomSeed);

        return genColumnSpec;
    }

    public Integer getColumnID() { return null; }

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
}
