package dgen.datatypes;

public class IntegerType implements DataType {

    private int value;

    public IntegerType(int value) {
        this.value = value;
    }

    @Override
    public NativeType nativeType() {
        return NativeType.INTEGER;
    }

    @Override
    public int size() {
        return Integer.BYTES;
    }

    @Override
    public Integer value() {
        return this.value;
    }

}
