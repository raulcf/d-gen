package dgen.datatypes;

public class FloatType implements DataType {

    private float value;

    public FloatType(float value)  { this.value = value; }

    @Override
    public NativeType nativeType() {
        return NativeType.FLOAT;
    }

    @Override
    public int size() {
        return Float.BYTES;
    }

    @Override
    public Float value() {
        return this.value;
    }

    @Override
    public boolean equals(Object o) {
        boolean result = false;
        if (o == null || o.getClass() != getClass()) {
            result = false;
        } else {
            FloatType floatType = (FloatType) o;
            if (this.value == floatType.value()) {
                result = true;
            }
        }
        return result;
    }

    @Override
    public int hashCode() {
        return Float.hashCode(this.value);
    }
}
