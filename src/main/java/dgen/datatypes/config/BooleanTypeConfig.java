package dgen.datatypes.config;

import java.util.List;
import java.util.Map;

import dgen.coreconfig.Config;
import dgen.coreconfig.ConfigDef;
import dgen.coreconfig.ConfigDef.Importance;
import dgen.coreconfig.ConfigDef.Type;
import dgen.coreconfig.ConfigKey;

public class BooleanTypeConfig extends Config {

    private static final ConfigDef config;

    public static final String TRUE_FALSE_RATIO = "true.false.ratio";
    private static final String TRUE_FALSE_RATIO_DOC = "Percentage of values of this type to be true";

    public static final String SIZE_IN_BYTES = "size.in.bytes";
    private static final String SIZE_IN_BYTES_DOC = "The native size of this type in bytes";

    static {
        config = new ConfigDef()
                .define(TRUE_FALSE_RATIO, Type.DOUBLE, 0.5, Importance.LOW, TRUE_FALSE_RATIO_DOC)
                .define(SIZE_IN_BYTES, Type.INT, Byte.SIZE, Importance.LOW, SIZE_IN_BYTES_DOC);
    }

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
