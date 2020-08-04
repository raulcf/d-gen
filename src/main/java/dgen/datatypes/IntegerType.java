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

    @Override
    public boolean equals(Object o) {
        boolean result = false;
        if (o == null || o.getClass() != getClass()) {
            result = false;
        } else {
            IntegerType integerType = (IntegerType) o;
            if (this.value == integerType.value()) {
                result = true;
            }
        }
        return result;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(this.value);
    }
}
