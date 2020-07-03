package dgen.utils.schemas.datatypes;

public class Float implements DataType {
    private Float defaultValue = null;
    private float minValue = java.lang.Float.MIN_VALUE;
    private float maxValue = java.lang.Float.MAX_VALUE;
    private Distributions distribution;

    public Type type() { return Type.BOOLEAN; }

    public Float getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(Float defaultValue) {
        this.defaultValue = defaultValue;
    }

    public float getMinValue() {
        return minValue;
    }

    public void setMinValue(float minValue) {
        this.minValue = minValue;
    }

    public float getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(float maxValue) {
        this.maxValue = maxValue;
    }

    public Distributions getDistribution() {
        return distribution;
    }

    public void setDistribution(Distributions distribution) {
        this.distribution = distribution;
    }
}
