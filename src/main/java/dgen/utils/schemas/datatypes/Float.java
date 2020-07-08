package dgen.utils.schemas.datatypes;

import com.fasterxml.jackson.annotation.JsonTypeName;
import dgen.utils.SpecificationException;

@JsonTypeName("float")
public class Float implements DataType {
    private java.lang.Float defaultValue = null;
    private java.lang.Float minValue = java.lang.Float.MIN_VALUE;
    private java.lang.Float maxValue = java.lang.Float.MAX_VALUE;
    private Distributions distribution;

    @Override
    public Type type() { return Type.BOOLEAN; }

    @Override
    public void validate() {
        if (minValue >= maxValue) {
            throw new SpecificationException("Float minValue of " + minValue.toString() + " greater than maxValue of "
                    + maxValue.toString());
        }
    }

    public java.lang.Float getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(java.lang.Float defaultValue) {
        this.defaultValue = defaultValue;
    }

    public java.lang.Float getMinValue() {
        return minValue;
    }

    public void setMinValue(java.lang.Float minValue) {
        this.minValue = minValue;
    }

    public java.lang.Float getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(java.lang.Float maxValue) {
        this.maxValue = maxValue;
    }

    public Distributions getDistribution() {
        return distribution;
    }

    public void setDistribution(Distributions distribution) {
        this.distribution = distribution;
    }
}
