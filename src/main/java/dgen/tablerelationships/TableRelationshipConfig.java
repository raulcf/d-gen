package dgen.tablerelationships;

import dgen.coreconfig.Config;
import dgen.coreconfig.ConfigDef;
import dgen.coreconfig.ConfigKey;
import dgen.tablerelationships.dependencyfunctions.DependencyFunctionConfig;
import dgen.utils.parsers.specs.relationshipspecs.DefTableRelationshipSpec;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableRelationshipConfig extends Config {

    public static final String MAPPINGS = "mappings";
    private static final String MAPPINGS_DOC = "Mapping between columnIDs";

    public static final String DEPENDENCY_FUNCTION_CONFIG = "dependency.function.config";
    private static final String DEPENDENCY_FUNCTION_CONFIG_DOC = "Config file of dependency function";

    public static final String RANDOM_SEED = "random.seed";
    private static final String RANDOM_SEED_DOC = "Random seed used to generate values";

    private static final ConfigDef config;

    static {
        config = new ConfigDef()
                .define(DEPENDENCY_FUNCTION_CONFIG, ConfigDef.Type.OBJECT, ConfigDef.Importance.LOW, DEPENDENCY_FUNCTION_CONFIG_DOC)
                .define(MAPPINGS, ConfigDef.Type.OBJECT, ConfigDef.Importance.LOW, MAPPINGS_DOC)
                .define(RANDOM_SEED, ConfigDef.Type.LONG, ConfigDef.Importance.LOW, RANDOM_SEED_DOC);
    }

    public static TableRelationshipConfig specToConfig(DefTableRelationshipSpec tableRelationshipSpec) {
        Map<String, Object> originals = new HashMap<>();
        originals.put(TableRelationshipConfig.MAPPINGS, tableRelationshipSpec.getDependencyMap());
        originals.put(TableRelationshipConfig.DEPENDENCY_FUNCTION_CONFIG,
                DependencyFunctionConfig.specToConfig(tableRelationshipSpec.getDependencyFunction()));
        originals.put(TableRelationshipConfig.RANDOM_SEED, tableRelationshipSpec.getRandomSeed());

        return new TableRelationshipConfig(originals);
    }

    public TableRelationshipConfig(Map<? extends Object, ? extends Object> originals) {
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
