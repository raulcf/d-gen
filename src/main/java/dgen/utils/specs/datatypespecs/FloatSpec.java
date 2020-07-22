package dgen.utils.specs.datatypespecs;

import com.fasterxml.jackson.annotation.JsonTypeName;
import dgen.utils.SpecificationException;
import dgen.utils.specs.datatypespecs.distributionspecs.DistributionSpec;
import dgen.utils.specs.datatypespecs.distributionspecs.Distributions;
import dgen.utils.specs.datatypespecs.distributionspecs.UniformDistributionSpec;

@JsonTypeName("float")
public class FloatSpec implements DataTypeSpec {
    private java.lang.Float defaultValue = null;
    private java.lang.Float minValue = (float) 0;
    private java.lang.Float maxValue = java.lang.Float.MAX_VALUE;
    private DistributionSpec distribution = new UniformDistributionSpec();

    @Override
    public DataTypes type() { return DataTypes.BOOLEAN; }

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

    public DistributionSpec getDistribution() {
        return distribution;
    }

    public void setDistribution(DistributionSpec distribution) {
        this.distribution = distribution;
    }
}
