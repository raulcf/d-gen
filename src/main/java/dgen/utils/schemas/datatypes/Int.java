package dgen.utils.schemas.datatypes;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("int")
public class Int implements DataType{
    private Integer defaultValue = null;
    private Integer minValue = Integer.MIN_VALUE;
    private Integer maxValue = Integer.MAX_VALUE;
    private Distributions distribution = Distributions.UNIFORM;

    public Type type() { return Type.INT; }

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
