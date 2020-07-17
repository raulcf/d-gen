package dgen.datatypes.generators;

import dgen.datatypes.DataType;
import dgen.datatypes.FloatType;
import dgen.datatypes.IntegerType;
import dgen.datatypes.NativeType;
import dgen.datatypes.config.FloatTypeConfig;
import dgen.datatypes.config.IntegerTypeConfig;
import dgen.distributions.Distribution;
import dgen.distributions.GaussianDistribution;

import java.util.Random;

public class FloatTypeGenerator implements DataTypeGenerator {

    private final NativeType nativeType = NativeType.FLOAT;

    private Float defaultValue;
    private float lowerBoundDomain;
    private float upperBoundDomain;
    private int sizeInBytes;

    // TODO: we'll likely move all random to a new package so we can keep track of all seeds used to generate the data
    private Random rnd;

    public FloatTypeGenerator(FloatTypeConfig dtc) {
        this.defaultValue = dtc.getFloat(FloatTypeConfig.DEFAULT_VALUE);
        this.lowerBoundDomain = dtc.getFloat(FloatTypeConfig.LOWER_BOUND_DOMAIN);
        this.upperBoundDomain = dtc.getFloat(FloatTypeConfig.UPPER_BOUND_DOMAIN);
        this.sizeInBytes = dtc.getInt(FloatTypeConfig.SIZE_IN_BYTES);

        rnd = new Random();
    }

    private FloatTypeGenerator() {
        this.lowerBoundDomain = 0;
        this.upperBoundDomain = Float.MAX_VALUE;
        this.sizeInBytes = Float.SIZE;

        rnd = new Random();
    }

    public static FloatTypeGenerator makeDefault() {
        return new FloatTypeGenerator();
    }

    @Override
    public DataType drawWithReplacement(Distribution samplingDistribution) {
        // TODO: Maybe move this to another function since it doesn't depend on samplingDistribution
        if (defaultValue != null) { return new FloatType(defaultValue); }

        // TODO: unless we need to know the specific distribution here, this switch should dissappear.
        switch(samplingDistribution.distributionType()) {
            case UNIFORM:
                return uniformSample();
            case GAUSSIAN:
                return gaussianSample((GaussianDistribution) samplingDistribution);
            case ZIPF:
                // TODO: to implement
                return null;
        }
        return null;
    }

    @Override
    public DataType drawWithoutReplacement(Distribution samplingDistribution) {
        return null;
    }

    private DataType uniformSample() {
        float value = (upperBoundDomain - lowerBoundDomain) * rnd.nextFloat() + lowerBoundDomain;
        return new FloatType(value);
    }

    private DataType gaussianSample(GaussianDistribution gaussianDistribution) {
        float standardDeviation = gaussianDistribution.getStandardDeviation();
        float mean = gaussianDistribution.getMean();

        return new FloatType((float) (rnd.nextGaussian() * standardDeviation + mean));
    }
}
