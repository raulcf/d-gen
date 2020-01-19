package dgen.datatypes;

/**
 * This enum is to differentiate the different machine types a generator may produce. D-GEN defines many DataTypes,
 * that can range from integers to dates, alphanumeric identifiers, etc. However, any DataType is represented with a
 * NativeType of the kind described here.
 */
public enum NativeType {
    BOOLEAN,
    INTEGER,
    FLOAT,
    STRING
}
