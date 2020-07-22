package dgen.datatypes;

public class BooleanType implements DataType {

    private boolean value;

    public BooleanType(boolean value) { this.value = value; }

    @Override
    public NativeType nativeType() { return NativeType.BOOLEAN; }

    @Override
    public int size() { return 1; } // FIXME: Technically a boolean is 1 bit.

    @Override
    public Boolean value() { return this.value; }
}
