package dgen.utils.schemas;

public class genColumn extends columnSchema {
    public Object dataType = "random";
    public int numColumns;
    public int minColumns = 1;
    public int maxColumns = 100; // TODO: Decide on a default max
}
