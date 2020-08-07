package dgen.datatypes.config;

import dgen.coreconfig.Config;
import dgen.coreconfig.ConfigDef;
import dgen.coreconfig.ConfigDef.Importance;
import dgen.coreconfig.ConfigDef.Type;
import dgen.coreconfig.ConfigKey;
import dgen.datatypes.NativeType;
import dgen.datatypes.generators.FloatTypeGenerator;
import dgen.distributions.config.DistributionConfig;
import dgen.utils.parsers.specs.datatypespecs.FloatSpec;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FloatTypeConfig extends Config implements DataTypeConfig {

    private static final ConfigDef config;

    public static final String DEFAULT_VALUE = "default.value";
    private static final String DEFAULT_VALUE_DOC = "Indicates the default value of a type";

    public static final String LOWER_BOUND_DOMAIN = "lower.bound.domain";
    private static final String LOWER_BOUND_DOMAIN_DOC = "Indicates the lower bound of a type's domain";

    public static final String UPPER_BOUND_DOMAIN = "upper.bound.domain";
    private static final String UPPER_BOUND_DOMAIN_DOC = "Indicates the upper bound of a type's domain";

    public static final String DISTRIBUTION = "distribution";
    public static final String DISTRIBUTION_DOC = "Indicates distribution of type";

    public static final String RANDOM_SEED = "random.seed";
    public static final String RANDOM_SEED_DOC = "Random seed used to generate values";

    public static final String SIZE_IN_BYTES = "size.in.bytes";
    private static final String SIZE_IN_BYTES_DOC = "The native size of this type in bytes";

    static {
        config = new ConfigDef()
                .define(DEFAULT_VALUE, Type.FLOAT, null, null, Importance.LOW, DEFAULT_VALUE_DOC)
                .define(LOWER_BOUND_DOMAIN, Type.FLOAT, Float.MIN_VALUE, Importance.LOW, LOWER_BOUND_DOMAIN_DOC)
                .define(UPPER_BOUND_DOMAIN, Type.FLOAT, Float.MAX_VALUE, Importance.LOW, UPPER_BOUND_DOMAIN_DOC)
                .define(DISTRIBUTION, Type.OBJECT, null, null, Importance.LOW, DISTRIBUTION_DOC)
                .define(RANDOM_SEED, Type.LONG, Importance.LOW, RANDOM_SEED_DOC)
                .define(SIZE_IN_BYTES, Type.INT, Float.BYTES, Importance.LOW, SIZE_IN_BYTES_DOC);
    }

    public static FloatTypeConfig specToConfig(FloatSpec floatSpec) {
        Map<String, Object> originals = new HashMap<>();
        originals.put("default.value", floatSpec.getDefaultValue());
        originals.put("lower.bound.domain", floatSpec.getMinValue());
        originals.put("upper.bound.domain", floatSpec.getMaxValue());
        originals.put("distribution", DistributionConfig.specToDistribution(floatSpec.getDistribution()));
        originals.put("random.seed", floatSpec.getRandomSeed());

        return new FloatTypeConfig(originals);
    }

    public static FloatTypeGenerator specToGenerator(FloatSpec floatSpec) {
        return new FloatTypeGenerator(specToConfig(floatSpec));
    }

    @Override
    public NativeType nativeType() {return NativeType.FLOAT; }

    public FloatTypeConfig(Map<? extends Object, ? extends Object> originals) {
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
