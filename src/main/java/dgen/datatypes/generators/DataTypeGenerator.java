package dgen.generators;

import dgen.datatypes.DataType;
import dgen.distributions.Distribution;

import java.util.List;
import java.util.stream.Stream;

/**
 * A datatype generator is a generator of DataTypes, which are types defined by DGEN
 */
public interface DataTypeGenerator {

    /**
     * Sample one value of type DataType with replacement
     * @param samplingDistribution
     * @return
     */
    DataType drawWithReplacement(Distribution samplingDistribution);

    /**
     * Sample one value of type DataType without replacement
     * @param samplingDistribution
     * @return
     */
    DataType drawWithoutReplacement(Distribution samplingDistribution);

}
