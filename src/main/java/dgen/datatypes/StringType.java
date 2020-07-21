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
        return 0;
    }

    @Override
    public String value() {
        return this.value;
    }
}
