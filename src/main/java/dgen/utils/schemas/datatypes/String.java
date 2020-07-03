package dgen.utils.schemas.datatypes;

public class String implements DataType{
    private java.lang.String defaultValue;
    private java.lang.String regexName;
    private Integer minLength = 0;
    private Integer maxLength = 1000; // TODO: To be decided
    private Distributions distribution;
    private java.lang.String validChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvxyz";

    public Type type() { return Type.STRING; }

    public java.lang.String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(java.lang.String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public java.lang.String getRegexName() {
        return regexName;
    }

    public void setRegexName(java.lang.String regexName) {
        this.regexName = regexName;
    }

    public Integer getMinLength() {
        return minLength;
    }

    public void setMinLength(Integer minLength) {
        this.minLength = minLength;
    }

    public Integer getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }

    public Distributions getDistribution() {
        return distribution;
    }

    public void setDistribution(Distributions distribution) {
        this.distribution = distribution;
    }

    public java.lang.String getValidChars() {
        return validChars;
    }

    public void setValidChars(java.lang.String validChars) {
        this.validChars = validChars;
    }
}
