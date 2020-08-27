package dgen.datatypes.config;

import dgen.coreconfig.Config;
import dgen.coreconfig.ConfigDef;
import dgen.coreconfig.ConfigDef.Importance;
import dgen.coreconfig.ConfigDef.Type;
import dgen.coreconfig.ConfigKey;
import dgen.datatypes.NativeType;
import dgen.datatypes.generators.IntegerTypeGenerator;
import dgen.distributions.config.DistributionConfig;
import dgen.utils.parsers.specs.datatypespecs.IntegerSpec;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IntegerTypeConfig extends Config implements DataTypeConfig {

    // TODO: refactor this to reflect the options we need for ME

    private static final ConfigDef config;

    public static final String DEFAULT_VALUE = "default.value";
    private static final String DEFAULT_VALUE_DOC = "Indicates the default value of a type";

    public static final String LOWER_BOUND_DOMAIN = "lower.bound.domain";
    private static final String LOWER_BOUND_DOMAIN_DOC = "Indicates the lower bound of a type's domain";

    public static final String UPPER_BOUND_DOMAIN = "upper.bound.domain";
    private static final String UPPER_BOUND_DOMAIN_DOC = "Indicates the upper bound of a type's domain";

    public static final String DISTRIBUTION = "distribution";
    private static final String DISTRIBUTION_DOC = "Indicates distribution of type";

    public static final String RANDOM_SEED = "random.seed";
    private static final String RANDOM_SEED_DOC = "Random seed used to generate values";

    public static final String SIZE_IN_BYTES = "size.in.bytes";
    private static final String SIZE_IN_BYTES_DOC = "The native size of this type in bytes";

    static {
        config = new ConfigDef()
                .define(DEFAULT_VALUE, Type.INT, null, null, Importance.LOW, DEFAULT_VALUE_DOC)
                .define(LOWER_BOUND_DOMAIN, Type.INT, Integer.MIN_VALUE, Importance.LOW, LOWER_BOUND_DOMAIN_DOC)
                .define(UPPER_BOUND_DOMAIN, Type.INT, Integer.MAX_VALUE, Importance.LOW, UPPER_BOUND_DOMAIN_DOC)
                .define(DISTRIBUTION, Type.OBJECT, null, null, Importance.LOW, DISTRIBUTION_DOC)
                .define(RANDOM_SEED, Type.LONG, Importance.LOW, RANDOM_SEED_DOC)
                .define(SIZE_IN_BYTES, Type.INT, Integer.BYTES, Importance.LOW, SIZE_IN_BYTES_DOC);
    }

    public static IntegerTypeConfig specToConfig(IntegerSpec integerSpec) {
        Map<String, Object> originals = new HashMap<>();
        originals.put(IntegerTypeConfig.DEFAULT_VALUE, integerSpec.getDefaultValue());
        originals.put(IntegerTypeConfig.LOWER_BOUND_DOMAIN, integerSpec.getMinValue());
        originals.put(IntegerTypeConfig.UPPER_BOUND_DOMAIN, integerSpec.getMaxValue());
        originals.put(IntegerTypeConfig.DISTRIBUTION, DistributionConfig.specToDistribution(integerSpec.getDistribution()));
        originals.put(IntegerTypeConfig.RANDOM_SEED, integerSpec.getRandomSeed());

        return new IntegerTypeConfig(originals);
    }

    public static IntegerTypeGenerator specToGenerator(IntegerSpec integerSpec) {
        return new IntegerTypeGenerator(specToConfig(integerSpec));
    }

    @Override
    public NativeType nativeType() {
        return NativeType.INTEGER;
    }

    public IntegerTypeConfig(Map<? extends Object, ? extends Object> originals) {
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

