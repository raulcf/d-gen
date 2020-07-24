package dgen.datatypes.generators;

import dgen.coreconfig.DGException;
import dgen.datatypes.BooleanType;
import dgen.datatypes.DataType;
import dgen.datatypes.NativeType;
import dgen.datatypes.config.BooleanTypeConfig;
import dgen.datatypes.config.FloatTypeConfig;
import dgen.utils.RandomGenerator;

import java.util.Random;

public class BooleanTypeGenerator implements DataTypeGenerator {

    private float tfRatio;
    private int sizeInBytes;

    private RandomGenerator rnd;
    private BooleanTypeConfig dtc;

    public BooleanTypeGenerator(BooleanTypeConfig dtc) {
        this.dtc = dtc;
        this.tfRatio = dtc.getFloat(BooleanTypeConfig.TRUE_FALSE_RATIO);
        this.sizeInBytes = dtc.getInt(BooleanTypeConfig.SIZE_IN_BYTES);
        this.rnd = new RandomGenerator(dtc.getLong(BooleanTypeConfig.RANDOM_SEED));
    }

    private BooleanTypeGenerator() {
        this.tfRatio = (float) 0.5;
        this.sizeInBytes = Float.SIZE;

        this.rnd = new RandomGenerator(new Random().nextLong());
    }

    public static BooleanTypeGenerator makeDefault() {
        return new BooleanTypeGenerator();
    }

    @Override
    public NativeType getNativeType() { return NativeType.BOOLEAN; }

    @Override
    public BooleanTypeGenerator copy() { return new BooleanTypeGenerator(dtc); }

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
