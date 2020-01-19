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
        return 0;
    }

    @Override
    public Integer value() {
        return this.value;
    }

}
