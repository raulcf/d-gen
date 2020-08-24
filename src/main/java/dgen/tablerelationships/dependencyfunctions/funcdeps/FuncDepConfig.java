package dgen.tablerelationships.dependencyfunctions.funcdeps;

import dgen.coreconfig.Config;
import dgen.coreconfig.ConfigDef;
import dgen.coreconfig.ConfigKey;
import dgen.tablerelationships.dependencyfunctions.DependencyFunctionConfig;
import dgen.utils.parsers.specs.relationshipspecs.dependencyFunctions.FunctionType;
import dgen.utils.parsers.specs.relationshipspecs.dependencyFunctions.FunctionalDependency;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FuncDepConfig extends Config implements DependencyFunctionConfig {

    private static final ConfigDef config;

    static {
        config = new ConfigDef();
    }

    public static FuncDepConfig specToConfig(FunctionalDependency functionalDependencySpec) {
        Map<String, Object> originals = new HashMap<>();

        return new FuncDepConfig(originals);
    }

    public FuncDepConfig(Map<? extends Object, ? extends Object> originals) {
        super(config, originals);
    }

    @Override
    public FunctionType dependencyName() { return FunctionType.FUNCTIONAL_DEPENDENCY; }

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
