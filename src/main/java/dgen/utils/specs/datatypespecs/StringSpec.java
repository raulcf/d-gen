package dgen.utils.specs.datatypespecs;

import com.fasterxml.jackson.annotation.JsonTypeName;
import dgen.utils.SpecificationException;
import dgen.utils.specs.datatypespecs.distributionspecs.DistributionSpec;
import dgen.utils.specs.datatypespecs.distributionspecs.UniformDistributionSpec;

@JsonTypeName("string")
public class StringSpec implements DataTypeSpec {
    private java.lang.String defaultValue;
    private java.lang.String regexPattern;
    private Integer minLength = 0;
    private Integer maxLength = 1000; // TODO: To be decided
    private DistributionSpec distribution = new UniformDistributionSpec();
    private java.lang.String validChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvxyz";
    private Long randomSeed;

    @Override
    public DataTypes type() { return DataTypes.STRING; }

    @Override
    public void validate() {
        if (minLength >= maxLength) {
            throw new SpecificationException("String minLength of " + minLength.toString()
                    + " greater than maxLength of " + maxLength.toString());
        }
    }

    public java.lang.String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(java.lang.String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public java.lang.String getRegexPattern() {
        return regexPattern;
    }

    public void setRegexPattern(java.lang.String regexName) {
        this.regexPattern = regexName;
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

    public DistributionSpec getDistribution() {
        return distribution;
    }

    public void setDistribution(DistributionSpec distribution) {
        this.distribution = distribution;
    }

    public java.lang.String getValidChars() {
        return validChars;
    }

    public void setValidChars(java.lang.String validChars) {
        this.validChars = validChars;
    }

    public Long getRandomSeed() {
        return randomSeed;
    }

    public void setRandomSeed(Long randomSeed) {
        this.randomSeed = randomSeed;
    }
}
