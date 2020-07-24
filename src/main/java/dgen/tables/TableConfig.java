package dgen.tables;

import dgen.column.ColumnConfig;
import dgen.coreconfig.Config;
import dgen.coreconfig.ConfigDef;
import dgen.coreconfig.ConfigKey;
import dgen.utils.specs.ColumnSpec;
import dgen.utils.specs.TableSpec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableConfig extends Config {

    private static final ConfigDef config;

    public static final String TABLE_ID = "table.id";
    private static final String TABLE_ID_DOC = "Indicates the unique table ID identifying a table";

    public static final String NUM_ROWS = "num.rows";
    private static final String NUM_ROWS_DOC = "Indicates the number of rows in a table";

    public static final String COLUMN_CONFIGS = "column.configs";
    private static final String COLUMN_CONFIGS_DOC = "ColumnConfig objects describing the columns in a table";

    public static final String TABLE_NAME = "table.name";
    private static final String TABLE_NAME_DOC = "Indicates the name of table";

    public static final String REGEX_NAME = "regex.name";
    private static final String REGEX_NAME_DOC = "Indicates the regex pattern to generate string";

    public static final String RANDOM_NAME = "random.name";
    private static final String RANDOM_NAME_DOC = "Indicates whether to generate a random name";

    public static final String TABLE_RELATIONSHIPS = "table.relationships";
    public static final String TABLE_RELATIONSHIPS_DOC = "List of relationships between columns";

    public static final String RANDOM_SEED = "random.seed";
    public static final String RANDOM_SEED_DOC = "Random seed used to generate values";

    //TODO: Add table relationships

    static {
        config = new ConfigDef()
                .define(TABLE_ID, ConfigDef.Type.INT, ConfigDef.Importance.LOW, TABLE_ID_DOC)
                .define(NUM_ROWS, ConfigDef.Type.INT, ConfigDef.Importance.LOW, NUM_ROWS_DOC)
                .define(COLUMN_CONFIGS, ConfigDef.Type.OBJECT, ConfigDef.Importance.LOW, COLUMN_CONFIGS_DOC)
                .define(TABLE_NAME, ConfigDef.Type.STRING, null, null, ConfigDef.Importance.LOW, TABLE_NAME_DOC)
                .define(REGEX_NAME, ConfigDef.Type.STRING, null, null, ConfigDef.Importance.LOW, REGEX_NAME_DOC)
                .define(RANDOM_NAME, ConfigDef.Type.BOOLEAN, true, ConfigDef.Importance.LOW, RANDOM_NAME_DOC)
                .define(TABLE_RELATIONSHIPS, ConfigDef.Type.OBJECT, null, null, ConfigDef.Importance.LOW, TABLE_RELATIONSHIPS_DOC)
                .define(RANDOM_SEED, ConfigDef.Type.LONG, ConfigDef.Importance.LOW, RANDOM_SEED_DOC);
    }

    public static TableConfig specToConfig(TableSpec tableSpec) {
        Map<String, Object> originals = new HashMap<>();
        originals.put("table.id", tableSpec.getTableID());
        originals.put("num.rows", tableSpec.getNumRows());
        originals.put("table.name", tableSpec.getTableName());
        originals.put("regex.name", tableSpec.getRegexName());
        originals.put("random.name", tableSpec.isRandomName());
        originals.put("random.seed", tableSpec.getRandomSeed());

        List<ColumnConfig> columnConfigs = new ArrayList<>();

        // TODO: tableSpec.GetColumnSpecs() Should only be DefColumnSpec but I can't figure out how to downcast it
        for (ColumnSpec columnSpec: tableSpec.getColumnSpecs()) {
            columnConfigs.add(ColumnConfig.specToConfig(columnSpec));
        }
        originals.put("column.configs", columnConfigs);

        return new TableConfig(originals);
    }

    public static TableGenerator specToGenerator(TableSpec tableSpec) {
        return new TableGenerator(specToConfig(tableSpec));
    }

    public TableConfig(Map<? extends Object, ? extends Object> originals) {
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
