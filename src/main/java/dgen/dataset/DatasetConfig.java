package dgen.dataset;

import dgen.column.ColumnConfig;
import dgen.coreconfig.Config;
import dgen.coreconfig.ConfigDef;
import dgen.coreconfig.ConfigKey;
import dgen.tables.TableConfig;
import dgen.tables.TableGenerator;
import dgen.utils.SpecificationParser;
import dgen.utils.specs.ColumnSpec;
import dgen.utils.specs.DatabaseSpec;
import dgen.utils.specs.TableSpec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatasetConfig extends Config {

    private static final ConfigDef config;

    public static final String DATASET_NAME = "dataset.name";
    private static final String DATASET_NAME_DOC = "Indicates the name of dataset";

    public static final String TABLE_CONFIGS = "table.configs";
    private static final String TABLE_CONFIGS_DOC = "TableConfig objects describing the tables in a dataset";

    //TODO: Add table relationships

    static {
        config = new ConfigDef()
                .define(DATASET_NAME, ConfigDef.Type.STRING, null, null, ConfigDef.Importance.LOW, DATASET_NAME_DOC)
                .define(TABLE_CONFIGS, ConfigDef.Type.OBJECT, ConfigDef.Importance.LOW, TABLE_CONFIGS_DOC);
    }

    public static DatasetConfig specToConfig(DatabaseSpec databaseSpec) {
        Map<String, Object> originals = new HashMap<>();
        originals.put("dataset.name", databaseSpec.getDatabaseName());

        List<TableConfig> tableConfigs = new ArrayList<>();

        // TODO: tableSpec.GetColumnSpecs() Should only be DefColumnSpec but I can't figure out how to downcast it
        for (TableSpec tableSpec: databaseSpec.getTableSpecs()) {
            tableConfigs.add(TableConfig.specToConfig(tableSpec));
        }

        originals.put("table.configs", tableConfigs);

        return new DatasetConfig(originals);
    }

    public static DatasetGenerator specToGenerator(DatabaseSpec databaseSpec) {
        return new DatasetGenerator(specToConfig(databaseSpec));
    }

    public DatasetConfig(Map<? extends Object, ? extends Object> originals) {
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
