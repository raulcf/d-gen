package dgen.datatypes.config;

import dgen.coreconfig.Config;
import dgen.coreconfig.ConfigDef;
import dgen.coreconfig.ConfigDef.Importance;
import dgen.coreconfig.ConfigDef.Type;
import dgen.coreconfig.ConfigKey;
import dgen.datatypes.NativeType;
import dgen.datatypes.generators.StringTypeGenerator;
import dgen.distributions.config.DistributionConfig;
import dgen.utils.specs.datatypespecs.StringSpec;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StringTypeConfig extends Config implements DataTypeConfig{

    private static final ConfigDef config;

    public static final String DEFAULT_VALUE = "default.value";
    private static final String DEFAULT_VALUE_DOC = "Indicates the default value of a type";

    public static final String REGEX_PATTERN = "regex.pattern";
    private static final String REGEX_PATTERN_DOC = "Indicates the regex pattern to generate values";

    public static final String VALID_CHARACTERS = "valid.characters";
    private static final String VALID_CHARACTERS_DOC = "Indicates the characters to use when generating values";

    public static final String MIN_LENGTH = "min.length";
    private static final String MIN_LENGTH_DOC = "Indicates the minimum length of this type";

    public static final String MAX_LENGTH = "max.length";
    private static final String MAX_LENGTH_DOC = "Indicates the maximum length of this type";

    public static final String DISTRIBUTION = "distribution";
    public static final String DISTRIBUTION_DOC = "Indicates distribution of type";

    public static final String RANDOM_SEED = "random.seed";
    public static final String RANDOM_SEED_DOC = "Random seed used to generate values";

    public static final String SIZE_IN_BYTES = "size.in.bytes";
    private static final String SIZE_IN_BYTES_DOC = "The native size of this type in bytes";

    static {
        config = new ConfigDef()
                .define(DEFAULT_VALUE, Type.STRING, null, null, Importance.LOW, DEFAULT_VALUE_DOC)
                .define(REGEX_PATTERN, Type.STRING, null, null, Importance.LOW, REGEX_PATTERN_DOC)
                .define(VALID_CHARACTERS, Type.STRING, "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvxyz", Importance.LOW, VALID_CHARACTERS_DOC)
                .define(MIN_LENGTH, Type.INT, 0, Importance.LOW, MAX_LENGTH_DOC)
                .define(MAX_LENGTH, Type.INT, Integer.MAX_VALUE, Importance.LOW, MAX_LENGTH_DOC)
                .define(DISTRIBUTION, Type.OBJECT, null, null, Importance.LOW, DISTRIBUTION_DOC)
                .define(RANDOM_SEED, Type.LONG, Importance.LOW, RANDOM_SEED_DOC)
                .define(SIZE_IN_BYTES, Type.INT, Character.BYTES, Importance.LOW, SIZE_IN_BYTES_DOC);
    }

    public static StringTypeConfig specToConfig(StringSpec stringSpec) {
        Map<String, Object> originals = new HashMap<>();
        originals.put("default.value", stringSpec.getDefaultValue());
        originals.put("regex.pattern", stringSpec.getRegexPattern());
        originals.put("valid.characters", stringSpec.getValidChars());
        originals.put("min.length", stringSpec.getMinLength());
        originals.put("max.length", stringSpec.getMaxLength());
        originals.put("distribution", DistributionConfig.specToDistribution(stringSpec.getDistribution()));
        originals.put("random.seed", stringSpec.getRandomSeed());

        return new StringTypeConfig(originals);
    }

    public static StringTypeGenerator specToGenerator(StringSpec stringSpec) {
        return new StringTypeGenerator(specToConfig(stringSpec));
    }

    @Override
    public NativeType nativeType() { return NativeType.STRING; }

    public StringTypeConfig(Map<? extends Object, ? extends Object> originals) {
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
