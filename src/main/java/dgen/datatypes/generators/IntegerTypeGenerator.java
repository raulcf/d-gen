package dgen.datatypes.generators;

import dgen.datatypes.IntegerType;
import dgen.datatypes.config.IntegerTypeConfig;
import dgen.datatypes.DataType;
import dgen.datatypes.NativeType;
import dgen.distributions.Distribution;
import dgen.distributions.GaussianDistribution;
import dgen.distributions.UniformDistribution;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class IntegerTypeGenerator implements DataTypeGenerator {

    private final NativeType nativeType = NativeType.INTEGER;

    private Set<Integer> drawnIntegers = new HashSet<>();

    private Integer defaultValue;
    private int lowerBoundDomain;
    private int upperBoundDomain;
    private Distribution distribution;
    private int sizeInBytes;

    // TODO: we'll likely move all random to a new package so we can keep track of all seeds used to generate the data
    private Random rnd;

    public IntegerTypeGenerator(IntegerTypeConfig dtc) {
        this.defaultValue = dtc.getInt(IntegerTypeConfig.DEFAULT_VALUE);
        this.lowerBoundDomain = dtc.getInt(IntegerTypeConfig.LOWER_BOUND_DOMAIN);
        this.upperBoundDomain = dtc.getInt(IntegerTypeConfig.UPPER_BOUND_DOMAIN);
        this.distribution = (Distribution)dtc.getObject(IntegerTypeConfig.DISTRIBUTION);
        this.sizeInBytes = dtc.getInt(IntegerTypeConfig.SIZE_IN_BYTES);

        rnd = new Random();
    }

    private IntegerTypeGenerator() {
        this.lowerBoundDomain = 0;
        this.upperBoundDomain = Integer.MAX_VALUE;
        this.distribution = new UniformDistribution();
        this.sizeInBytes = Integer.SIZE;

        rnd = new Random();
    }

    public static IntegerTypeGenerator makeDefault() {
        return new IntegerTypeGenerator();
    }

    @Override
    public DataType drawWithReplacement() {
        if (defaultValue != null) { return new IntegerType(defaultValue); }

        // TODO: unless we need to know the specific distribution here, this switch should dissappear.
        switch(distribution.distributionType()) {
            case UNIFORM:
                return uniformSample();
            case GAUSSIAN:
                return gaussianSample((GaussianDistribution) distribution);
            case ZIPF:
                // TODO: to implement
                return null;
        }
        return null;
    }

    @Override
    public DataType drawWithoutReplacement() {
        if (defaultValue != null) { return new IntegerType(defaultValue); }

        // TODO: unless we need to know the specific distribution here, this switch should dissappear.
        switch(distribution.distributionType()) {
            //TODO: Make this more efficient for lots of calls or small bounds
            case UNIFORM:
                return uniformSampleWithoutReplacement();
            case GAUSSIAN:
                return gaussianSampleWithoutReplacement((GaussianDistribution) distribution);
            case ZIPF:
                // TODO: to implement
                return null;
        }
        return null;
    }

    private DataType uniformSample() {
        int value = rnd.nextInt(upperBoundDomain - lowerBoundDomain + 1) + lowerBoundDomain;
        return new IntegerType(value);
    }

    private DataType gaussianSample(GaussianDistribution gaussianDistribution) {
        float standardDeviation = gaussianDistribution.getStandardDeviation();
        float mean = gaussianDistribution.getMean();
        double value = (rnd.nextGaussian() * standardDeviation + mean);

        return new IntegerType((int) value);
    }

    private DataType uniformSampleWithoutReplacement() {
        int value = rnd.nextInt(upperBoundDomain - lowerBoundDomain + 1) + lowerBoundDomain;

        while (drawnIntegers.contains(value)) {
            value = rnd.nextInt(upperBoundDomain - lowerBoundDomain + 1) + lowerBoundDomain;
        }

        drawnIntegers.add(value);
        return new IntegerType(value);
    }

    private DataType gaussianSampleWithoutReplacement(GaussianDistribution gaussianDistribution) {
        float standardDeviation = gaussianDistribution.getStandardDeviation();
        float mean = gaussianDistribution.getMean();
        int value = (int) (rnd.nextGaussian() * standardDeviation + mean);

        while (drawnIntegers.contains( value)) {
            value = (int) (rnd.nextGaussian() * standardDeviation + mean);
        }

        return new IntegerType( value);
    }
}
