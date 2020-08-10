package dgen.datatypes.config;

import dgen.coreconfig.Config;
import dgen.coreconfig.ConfigDef;
import dgen.coreconfig.ConfigDef.Importance;
import dgen.coreconfig.ConfigDef.Type;
import dgen.coreconfig.ConfigKey;
import dgen.datatypes.NativeType;
import dgen.datatypes.generators.BooleanTypeGenerator;
import dgen.utils.parsers.specs.datatypespecs.BooleanSpec;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BooleanTypeConfig extends Config implements DataTypeConfig {

    private static final ConfigDef config;

    public static final String TRUE_FALSE_RATIO = "true.false.ratio";
    private static final String TRUE_FALSE_RATIO_DOC = "Percentage of values of this type to be true";

    public static final String RANDOM_SEED = "random.seed";
    public static final String RANDOM_SEED_DOC = "Random seed used to generate values";

    public static final String SIZE_IN_BYTES = "size.in.bytes";
    private static final String SIZE_IN_BYTES_DOC = "The native size of this type in bytes";

    static {
        config = new ConfigDef()
                .define(TRUE_FALSE_RATIO, Type.DOUBLE, 0.5, Importance.LOW, TRUE_FALSE_RATIO_DOC)
                .define(RANDOM_SEED, Type.LONG, Importance.LOW, RANDOM_SEED_DOC)
                .define(SIZE_IN_BYTES, Type.INT, 1, Importance.LOW, SIZE_IN_BYTES_DOC); // FIXME: Technically a boolean is 1 bit

    }

    public static BooleanTypeConfig specToConfig(BooleanSpec booleanSpec) {
        Map<String, Object> originals = new HashMap<>();
        originals.put("true.false.ratio", booleanSpec.getTfRatio());
        originals.put("random.seed", booleanSpec.getRandomSeed());

        return new BooleanTypeConfig(originals);
    }

    public static BooleanTypeGenerator specToGenerator(BooleanSpec booleanSpec) {
        return new BooleanTypeGenerator(specToConfig(booleanSpec));
    }


    @Override
    public NativeType nativeType() { return NativeType.BOOLEAN; }

    public BooleanTypeConfig(Map<? extends Object, ? extends Object> originals) {
        super(config, originals);
    }

    public static ConfigKey getConfigKey(String name) {
        return config.getConfigKey(name);
    }

    public static List<ConfigKey> getAllConfigKey() {
        return config.getAllConfigKey();
    }

    public static void main(String[] args) {
        System.out.println(config.toHtmlTable());
    }

}
