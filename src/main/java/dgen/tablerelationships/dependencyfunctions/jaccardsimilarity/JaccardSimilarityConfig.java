package dgen.tablerelationships.dependencyfunctions.jaccardsimilarity;

import dgen.coreconfig.Config;
import dgen.coreconfig.ConfigDef;
import dgen.coreconfig.ConfigKey;
import dgen.tablerelationships.dependencyfunctions.DependencyFunctionConfig;
import dgen.utils.parsers.specs.relationshipspecs.dependencyFunctions.FunctionType;
import dgen.utils.parsers.specs.relationshipspecs.dependencyFunctions.JaccardSimilarity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JaccardSimilarityConfig extends Config implements DependencyFunctionConfig {

    private static final ConfigDef config;

    public static final String SIMILARITY = "similarity";
    public static final String SIMILARITY_DOC = "Jaccard similarity index";


    static {
        config = new ConfigDef()
                .define(SIMILARITY, ConfigDef.Type.FLOAT, ConfigDef.Importance.LOW, SIMILARITY_DOC);
    }

    public static JaccardSimilarityConfig specToConfig(JaccardSimilarity jaccardSimilarity) {
        Map<String, Object> originals = new HashMap<>();

        originals.put("similarity", jaccardSimilarity.getSimilarity());

        return new JaccardSimilarityConfig(originals);
    }

    public JaccardSimilarityConfig(Map<? extends Object, ? extends Object> originals) {
        super(config, originals);
    }

    @Override
    public FunctionType dependencyName() { return FunctionType.JACCARD_SIMILARITY; }

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
