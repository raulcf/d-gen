package dgen.datatypes.config;

import dgen.coreconfig.Config;
import dgen.coreconfig.ConfigDef;
import dgen.coreconfig.ConfigDef.Importance;
import dgen.coreconfig.ConfigDef.Type;
import dgen.coreconfig.ConfigKey;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StringTypeConfig extends Config {

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

    public static final String SIZE_IN_BYTES = "size.in.bytes";
    private static final String SIZE_IN_BYTES_DOC = "The native size of this type in bytes";

    static {
        config = new ConfigDef()
                // TODO: DEFAULT_VALUE and REGEX_PATTERN should be null by default but the Config class doesn't allow that
                .define(DEFAULT_VALUE, Type.STRING, Importance.LOW, DEFAULT_VALUE_DOC)
                .define(REGEX_PATTERN, Type.STRING, Importance.LOW, REGEX_PATTERN_DOC)
                .define(VALID_CHARACTERS, Type.STRING, "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvxyz", Importance.LOW, VALID_CHARACTERS_DOC)
                .define(MIN_LENGTH, Type.INT, 0, Importance.LOW, MAX_LENGTH_DOC)
                .define(MAX_LENGTH, Type.INT, Integer.MAX_VALUE, Importance.LOW, MAX_LENGTH_DOC)
                .define(SIZE_IN_BYTES, Type.INT, Byte.SIZE, Importance.LOW, SIZE_IN_BYTES_DOC);
    }

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
//        System.out.println(config.toHtmlTable());
        Map<String, Integer> x = new HashMap<>();
        x.put("min.length", 10);
        StringTypeConfig d = new StringTypeConfig(x);
        System.out.println(d.getInt(StringTypeConfig.DEFAULT_VALUE));
    }

}
