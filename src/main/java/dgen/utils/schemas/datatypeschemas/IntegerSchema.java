package dgen.utils.schemas.datatypeschemas;

import com.fasterxml.jackson.annotation.JsonTypeName;
import dgen.utils.SpecificationException;

@JsonTypeName("int")
public class IntegerSchema implements DataTypeSchema {
    private Integer defaultValue = null;
    private Integer minValue = Integer.MIN_VALUE;
    private Integer maxValue = Integer.MAX_VALUE;
    private Distributions distribution = Distributions.UNIFORM;

    @Override
    public Type type() { return Type.INT; }

    @Override
    public void validate() {
        if (minValue >= maxValue) {
            throw new SpecificationException("Int minValue of " + minValue.toString() + " greater than maxValue of "
                    + maxValue.toString());
        }
    }

    public Integer getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(Integer defaultValue) {
        this.defaultValue = defaultValue;
    }

    public Integer getMinValue() {
        return minValue;
    }

    public void setMinValue(Integer minValue) {
        this.minValue = minValue;
    }

    public Integer getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(Integer maxValue) {
        this.maxValue = maxValue;
    }

    public Distributions getDistribution() {
        return distribution;
    }

    public void setDistribution(Distributions distribution) {
        this.distribution = distribution;
    }

    @Override
    public java.lang.String toString() {
        return "Int{" +
                "defaultValue=" + defaultValue +
                ", minValue=" + minValue +
                ", maxValue=" + maxValue +
                ", distribution=" + distribution +
                '}';
    }
}
