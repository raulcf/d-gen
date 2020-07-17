package dgen.datatypes.generators;

import dgen.datatypes.*;
import dgen.datatypes.config.BooleanTypeConfig;
import dgen.datatypes.config.FloatTypeConfig;
import dgen.datatypes.config.IntegerTypeConfig;
import dgen.distributions.Distribution;

import java.util.Random;

public class BooleanTypeGenerator implements DataTypeGenerator {

    private final NativeType nativeType = NativeType.BOOLEAN;

    private float tfRatio;
    private int sizeInBytes;

    // TODO: we'll likely move all random to a new package so we can keep track of all seeds used to generate the data
    private Random rnd;

    public BooleanTypeGenerator(BooleanTypeConfig dtc) {
        this.tfRatio = dtc.getFloat(BooleanTypeConfig.TRUE_FALSE_RATIO);
        this.sizeInBytes = dtc.getInt(FloatTypeConfig.SIZE_IN_BYTES);

        rnd = new Random();
    }

    private BooleanTypeGenerator() {
        this.tfRatio = (float) 0.5;
        this.sizeInBytes = Float.SIZE;

        rnd = new Random();
    }

    public static BooleanTypeGenerator makeDefault() {
        return new BooleanTypeGenerator();
    }

    @Override
    public DataType drawWithReplacement(Distribution samplingDistribution) {
        // TODO: unless we need to know the specific distribution here, this switch should dissappear.
        switch(samplingDistribution.distributionType()) {
            case UNIFORM:
                return uniformSample();
            case GAUSSIAN:
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
        if (rnd.nextFloat() > tfRatio) {
            return new BooleanType(false);
        } else {
            return new BooleanType(true);
        }
    }
}
