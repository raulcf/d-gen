package dgen.dataset;

import dgen.coreconfig.Config;
import dgen.coreconfig.ConfigDef;
import dgen.coreconfig.ConfigKey;
import dgen.tables.TableConfig;
import dgen.utils.specs.DatabaseSpec;
import dgen.utils.specs.TableSpec;
import dgen.utils.specs.relationships.DatabaseRelationshipSpec;
import dgen.utils.specs.relationships.DefPKFKSpec;
import org.javatuples.Pair;

import java.util.*;

public class DatasetConfig extends Config {

    private static final ConfigDef config;

    public static final String DATASET_NAME = "dataset.name";
    private static final String DATASET_NAME_DOC = "Indicates the name of dataset";

    public static final String TABLE_CONFIGS = "table.configs";
    private static final String TABLE_CONFIGS_DOC = "TableConfig objects describing the tables in a dataset";

    public static final String PK_FK_MAPPINGS = "pk.fk.mappings";
    private static final String PK_FK_MAPPINGS_DOC = "Mapping between primary keys and foreign keys";

    static {
        config = new ConfigDef()
                .define(DATASET_NAME, ConfigDef.Type.STRING, null, null, ConfigDef.Importance.LOW, DATASET_NAME_DOC)
                .define(TABLE_CONFIGS, ConfigDef.Type.OBJECT, ConfigDef.Importance.LOW, TABLE_CONFIGS_DOC)
                .define(PK_FK_MAPPINGS, ConfigDef.Type.OBJECT, null, null, ConfigDef.Importance.LOW, PK_FK_MAPPINGS_DOC);
    }

    public static DatasetConfig specToConfig(DatabaseSpec databaseSpec) {
        Map<String, Object> originals = new HashMap<>();
        originals.put("dataset.name", databaseSpec.getDatabaseName());

        List<TableConfig> tableConfigs = new ArrayList<>();

        for (TableSpec tableSpec: databaseSpec.getTableSpecs()) {
            tableConfigs.add(TableConfig.specToConfig(tableSpec));
        }
        originals.put("table.configs", tableConfigs);

        // Adding all primary keys and foreign keys into a map
        Map<Pair<Integer, Integer>, Set<Pair<Integer, Integer>>> pkfkMappings = new HashMap<>();
        List<Pair<Integer, Integer>> primaryKeys = new ArrayList<>();
        List<Pair<Integer, Integer>> foreignKeys = new ArrayList<>();
        for (DatabaseRelationshipSpec databaseRelationshipSpec: databaseSpec.getDatabaseRelationships()) {
            DefPKFKSpec defPKFKSpec = (DefPKFKSpec) databaseRelationshipSpec;
            primaryKeys.addAll(defPKFKSpec.getPrimaryKeys());
            foreignKeys.addAll(defPKFKSpec.getForeignKeys());
        }

        for (int i = 0; i < primaryKeys.size(); i++) {
            Set<Pair<Integer, Integer>> mapping;
            if (pkfkMappings.containsKey(primaryKeys.get(i))) {
                mapping = pkfkMappings.get(primaryKeys.get(i));
                mapping.add(foreignKeys.get(i));
                pkfkMappings.replace(primaryKeys.get(i), mapping);
            } else {
                mapping = new HashSet<>();
                mapping.add(foreignKeys.get(i));
                pkfkMappings.put(primaryKeys.get(i), mapping);
            }
        }
        originals.put("pk.fk.mappings", pkfkMappings);

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
