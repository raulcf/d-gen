package dgen.datatypes.generators;

import dgen.coreconfig.DGException;
import dgen.datatypes.BooleanType;
import dgen.datatypes.DataType;
import dgen.datatypes.NativeType;
import dgen.datatypes.config.BooleanTypeConfig;
import dgen.datatypes.config.FloatTypeConfig;

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
    public DataType drawWithReplacement() {
        if (rnd.nextFloat() > tfRatio) {
            return new BooleanType(false);
        } else {
            return new BooleanType(true);
        }
    }

    @Override
    public DataType drawWithoutReplacement() {
        throw new DGException("Cannot draw booleans without replacement");
    }
}
