package dgen.datatypes;

import dgen.config.DataTypeConfig;
import dgen.distributions.Distribution;
import dgen.generators.DataTypeGenerator;

import java.util.List;
import java.util.stream.Stream;

public class IntegerTypeGenerator implements DataType, DataTypeGenerator {

    private final NativeType nativeType = NativeType.INTEGER;

    private int lowerBoundDomain;
    private int upperBoundDomain;
    private int sizeInBytes;

    public IntegerDG(DataTypeConfig dtc) {
        this.lowerBoundDomain = dtc.getInt(DataTypeConfig.LOWER_BOUND_DOMAIN);
        this.upperBoundDomain = dtc.getInt(DataTypeConfig.UPPER_BOUND_DOMAIN);
        this.sizeInBytes = dtc.getInt(DataTypeConfig.SIZE_IN_BYTES);
    }

    @Override
    public NativeType nativeType() {
        return this.nativeType;
    }

    @Override
    public int size() {
        return this.sizeInBytes;
    }

    @Override
    public Stream<> createStream() {
        return null;
    }

    @Override
    public List<Object> createCollection(int size) {
        return null;
    }

    @Override
    public Integer drawWithReplacement(Distribution samplingDistribution) {
        return null;
    }

    @Override
    public Integer drawWithoutReplacement(Distribution samplingDistribution) {
        return null;
    }
}
