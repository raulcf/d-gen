package dgen.datatypes;

public class StringType implements DataType {

    private String value;

    public StringType(String value)  { this.value = value; }

    @Override
    public NativeType nativeType() {
        return NativeType.STRING;
    }

    @Override
    public int size() {
        return Character.BYTES * value.length();
    }

    @Override
    public String value() {
        return this.value;
    }

    @Override
    public boolean equals(Object o) {
        boolean result = false;
        if (o == null || o.getClass() != getClass()) {
            result = false;
        } else {
            StringType stringType = (StringType) o;
            if (this.value.equals(stringType.value())) {
                result = true;
            }
        }
        return result;
    }

    @Override
    public int hashCode() {
        return this.value.hashCode();
    }
}
