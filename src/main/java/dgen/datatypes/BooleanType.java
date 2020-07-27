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

    @Override
    public boolean equals(Object o) {
        boolean result = false;
        if (o == null || o.getClass() != getClass()) {
            result = false;
        } else {
            BooleanType booleanType = (BooleanType) o;
            if (this.value == booleanType.value()) {
                result = true;
            }
        }
        return result;
    }

    @Override
    public int hashCode() {
        return Boolean.hashCode(this.value);
    }
}
