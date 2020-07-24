package dgen.column;

import dgen.coreconfig.Config;
import dgen.coreconfig.ConfigDef;
import dgen.coreconfig.ConfigDef.Importance;
import dgen.coreconfig.ConfigDef.Type;
import dgen.coreconfig.ConfigKey;
import dgen.utils.specs.ColumnSpec;
import dgen.utils.specs.DefColumnSpec;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ColumnConfig extends Config {

    // TODO: refactor this to reflect the options we need for ME

    private static final ConfigDef config;

    public static final String COLUMN_ID = "column.id";
    private static final String COLUMN_ID_DOC = "Indicates unique ID of column";

    public static final String COLUMN_NAME = "column.name";
    private static final String COLUMN_NAME_DOC = "Indicates the name of column";

    public static final String REGEX_NAME = "regex.name";
    private static final String REGEX_NAME_DOC = "Indicates the regex pattern to generate string";

    public static final String RANDOM_NAME = "random.name";
    private static final String RANDOM_NAME_DOC = "Indicates whether to generate a random name";

    public static final String DATATYPE = "datatype";
    private static final String DATATYPE_DOC = "Indicates the datatype of column";

    public static final String UNIQUE = "unique";
    private static final String UNIQUE_DOC = "Indicates whether column values are unique";

    public static final String HAS_NULL = "has.null";
    private static final String HAS_NULL_DOC = "Indicates whether column has null values";

    public static final String NULL_FREQUENCY = "null.frequency";
    private static final String NULL_FREQUENCY_DOC = "Indicates frequency of null values";

    public static final String RANDOM_SEED = "random.seed";
    public static final String RANDOM_SEED_DOC = "Random seed used to generate values";

    static {
        config = new ConfigDef()
                .define(COLUMN_ID, Type.INT, Importance.LOW, COLUMN_ID_DOC)
                .define(COLUMN_NAME, Type.STRING, null, null, Importance.LOW, COLUMN_NAME_DOC)
                .define(REGEX_NAME, Type.STRING, null, null, Importance.LOW, REGEX_NAME_DOC)
                .define(RANDOM_NAME, Type.BOOLEAN, true, Importance.LOW, RANDOM_NAME_DOC)
                .define(DATATYPE, Type.OBJECT, null, null, Importance.LOW, DATATYPE_DOC)
                .define(UNIQUE, Type.BOOLEAN, false, Importance.LOW, UNIQUE_DOC)
                .define(HAS_NULL, Type.BOOLEAN, false, Importance.LOW, HAS_NULL_DOC)
                .define(NULL_FREQUENCY, Type.FLOAT, null, null, Importance.LOW, NULL_FREQUENCY_DOC)
                .define(RANDOM_SEED, Type.LONG, Importance.LOW, RANDOM_SEED_DOC);
    }

    public static ColumnConfig specToConfig(ColumnSpec columnSpec) {
        Map<String, Object> originals = new HashMap<>();
        originals.put("column.id", columnSpec.getColumnID());
        originals.put("column.name", columnSpec.getColumnName());
        originals.put("regex.name", columnSpec.getRegexName());
        originals.put("random.name", columnSpec.isRandomName());
        originals.put("datatype", columnSpec.getDataTypeSpec());
        originals.put("unique", columnSpec.isUnique());
        originals.put("has.null", columnSpec.isHasNull());
        originals.put("null.frequency", columnSpec.getNullFrequency());
        originals.put("random.seed", columnSpec.getRandomSeed());

        return new ColumnConfig(originals);
    }

    public static ColumnGenerator specToGenerator(ColumnSpec columnSpec) {
        return new ColumnGenerator(specToConfig(columnSpec));
    }

    public ColumnConfig(Map<? extends Object, ? extends Object> originals) {
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

