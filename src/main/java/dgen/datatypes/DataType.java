package dgen.datatypes;

import dgen.distributions.Distribution;

import java.util.List;
import java.util.stream.Stream;

/**
 * A Data type is specified to refer to each of the types that DGEN can generate. It could be a primitive/native type,
 * but also semantically higher types like dates, alphanumeric IDs, or more complex types.
 */
public interface DataType {

    /**
     * Returns the native type in which any instance of this DataType is represented
     * @return
     */
    NativeType nativeType();

    /**
     * Return in bytes the size of one value of the datatype
     * @return
     */
    int size();

    /**
     * Returns the object, of some native type
     * @return
     */
    Object value();

}
