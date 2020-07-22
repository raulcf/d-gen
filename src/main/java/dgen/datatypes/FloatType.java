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
}
