package dgen.column;

import dgen.coreconfig.Config;
import dgen.coreconfig.ConfigDef;
import dgen.coreconfig.ConfigDef.Importance;
import dgen.coreconfig.ConfigDef.Type;
import dgen.coreconfig.ConfigKey;
import dgen.utils.parsers.specs.ColumnSpec;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ColumnConfig extends Config {

    // TODO: refactor this to reflect the options we need for ME

    private static final ConfigDef config;

    public static final String COLUMN_ID = "column.id";
    private static final String COLUMN_ID_DOC = "Indicates unique ID of column";

    public static final String COLUMN_TYPE = "column.type";
    private static final String COLUMN_TYPE_DOC = "Indicates spec type of column (e.g. DefForeignKey, DefColumn, etc)";

    public static final String COLUMN_NAME = "column.name";
    private static final String COLUMN_NAME_DOC = "Indicates the name of column";

    public static final String REGEX_NAME = "regex.name";
    private static final String REGEX_NAME_DOC = "Indicates the regex pattern to generate string";

    public static final String RANDOM_NAME = "random.name";
    private static final String RANDOM_NAME_DOC = "Indicates whether to generate a random name";

    public static final String DATATYPE = "datatype";
    private static final String DATATYPE_DOC = "Datatype spec of column";

    public static final String UNIQUE = "unique";
    private static final String UNIQUE_DOC = "Indicates whether column values are unique";

    public static final String HAS_NULL = "has.null";
    private static final String HAS_NULL_DOC = "Indicates whether column has null values";

    public static final String NULL_FREQUENCY = "null.frequency";
    private static final String NULL_FREQUENCY_DOC = "Indicates frequency of null values";

    public static final String RANDOM_SEED = "random.seed";
    private static final String RANDOM_SEED_DOC = "Random seed used to generate values";

    static {
        config = new ConfigDef()
                .define(COLUMN_ID, Type.INT, Importance.LOW, COLUMN_ID_DOC)
                .define(COLUMN_TYPE, Type.OBJECT, Importance.LOW, COLUMN_TYPE_DOC)
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
        originals.put(ColumnConfig.COLUMN_ID, columnSpec.getColumnID());
        originals.put(ColumnConfig.COLUMN_TYPE, columnSpec.specType());
        originals.put(ColumnConfig.COLUMN_NAME, columnSpec.getColumnName());
        originals.put(ColumnConfig.REGEX_NAME, columnSpec.getRegexName());
        originals.put(ColumnConfig.RANDOM_NAME, columnSpec.isRandomName());
        originals.put(ColumnConfig.DATATYPE, columnSpec.getDataTypeSpec());
        originals.put(ColumnConfig.UNIQUE, columnSpec.isUnique());
        originals.put(ColumnConfig.HAS_NULL, columnSpec.isHasNull());
        originals.put(ColumnConfig.NULL_FREQUENCY, columnSpec.getNullFrequency());
        originals.put(ColumnConfig.RANDOM_SEED, columnSpec.getRandomSeed());

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

