package dgen.datatypes.config;

import java.util.List;
import java.util.Map;

import dgen.coreconfig.Config;
import dgen.coreconfig.ConfigDef;
import dgen.coreconfig.ConfigDef.Importance;
import dgen.coreconfig.ConfigDef.Type;
import dgen.coreconfig.ConfigKey;

public class FloatTypeConfig extends Config {

    private static final ConfigDef config;

    public static final String DEFAULT_VALUE = "default.value";
    private static final String DEFAULT_VALUE_DOC = "Indicates the default value of a type";

    public static final String LOWER_BOUND_DOMAIN = "lower.bound.domain";
    private static final String LOWER_BOUND_DOMAIN_DOC = "Indicates the lower bound of a type's domain";

    public static final String UPPER_BOUND_DOMAIN = "upper.bound.domain";
    private static final String UPPER_BOUND_DOMAIN_DOC = "Indicates the upper bound of a type's domain";

    public static final String SIZE_IN_BYTES = "size.in.bytes";
    private static final String SIZE_IN_BYTES_DOC = "The native size of this type in bytes";

    static {
        config = new ConfigDef()
                // TODO: DEFAULT_VALUE should be null by default but the Config class doesn't allow that
                .define(DEFAULT_VALUE, Type.FLOAT, Importance.LOW, DEFAULT_VALUE_DOC)
                .define(LOWER_BOUND_DOMAIN, Type.INT, Float.MIN_VALUE, Importance.LOW, LOWER_BOUND_DOMAIN_DOC)
                .define(UPPER_BOUND_DOMAIN, Type.INT, Float.MAX_VALUE, Importance.LOW, UPPER_BOUND_DOMAIN_DOC)
                .define(SIZE_IN_BYTES, Type.INT, Byte.SIZE, Importance.LOW, SIZE_IN_BYTES_DOC);
    }

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
