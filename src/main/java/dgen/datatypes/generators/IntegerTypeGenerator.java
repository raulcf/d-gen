package dgen.datatypes.generators;

import dgen.datatypes.DataType;
import dgen.datatypes.IntegerType;
import dgen.datatypes.NativeType;
import dgen.datatypes.config.IntegerTypeConfig;
import dgen.distributions.Distribution;
import dgen.distributions.GaussianDistribution;
import dgen.distributions.UniformDistribution;
import dgen.utils.parsers.RandomGenerator;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class IntegerTypeGenerator implements DataTypeGenerator {

    private Set<Integer> drawnIntegers = new HashSet<>();

    private Integer defaultValue;
    private Integer lowerBoundDomain;
    private Integer upperBoundDomain;
    private Distribution distribution;
    private int sizeInBytes;

    private RandomGenerator rnd;
    private IntegerTypeConfig dtc;

    public IntegerTypeGenerator(IntegerTypeConfig dtc) {
        this.dtc = dtc;
        this.defaultValue = dtc.getInt(IntegerTypeConfig.DEFAULT_VALUE);
        this.lowerBoundDomain = dtc.getInt(IntegerTypeConfig.LOWER_BOUND_DOMAIN);
        this.upperBoundDomain = dtc.getInt(IntegerTypeConfig.UPPER_BOUND_DOMAIN);
        this.distribution = (Distribution)dtc.getObject(IntegerTypeConfig.DISTRIBUTION);
        this.sizeInBytes = dtc.getInt(IntegerTypeConfig.SIZE_IN_BYTES);
        this.rnd = new RandomGenerator(dtc.getLong(IntegerTypeConfig.RANDOM_SEED));
    }

    private IntegerTypeGenerator() {
        this.lowerBoundDomain = 0;
        this.upperBoundDomain = Integer.MAX_VALUE;
        this.distribution = new UniformDistribution();
        this.sizeInBytes = Integer.SIZE;
        this.rnd = new RandomGenerator(new Random().nextLong());
    }

    public static IntegerTypeGenerator makeDefault() {
        return new IntegerTypeGenerator();
    }

    @Override
    public NativeType getNativeType() { return NativeType.INTEGER; }

    @Override
    public IntegerTypeGenerator copy() { return new IntegerTypeGenerator(dtc); }

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
        int value = rnd.nextInt(upperBoundDomain - lowerBoundDomain) + lowerBoundDomain;
        return new IntegerType(value);
    }

    private DataType gaussianSample(GaussianDistribution gaussianDistribution) {
        float standardDeviation = gaussianDistribution.getStandardDeviation();
        float mean = gaussianDistribution.getMean();
        double value = (rnd.nextGaussian() * standardDeviation + mean);

        return new IntegerType((int) value);
    }

    private DataType uniformSampleWithoutReplacement() {
        int value = rnd.nextInt(upperBoundDomain - lowerBoundDomain) + lowerBoundDomain;

        while (drawnIntegers.contains(value)) {
            value = rnd.nextInt(upperBoundDomain - lowerBoundDomain) + lowerBoundDomain;
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
