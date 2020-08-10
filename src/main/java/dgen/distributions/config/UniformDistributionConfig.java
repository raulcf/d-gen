package dgen.distributions.config;

import dgen.coreconfig.Config;
import dgen.coreconfig.ConfigDef;
import dgen.coreconfig.ConfigKey;
import dgen.distributions.DistributionType;
import dgen.distributions.UniformDistribution;
import dgen.utils.parsers.specs.datatypespecs.distributionspecs.UniformDistributionSpec;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UniformDistributionConfig extends Config implements DistributionConfig {

    private static final ConfigDef config;

    static {
        config = new ConfigDef();
    }

    public static UniformDistributionConfig specToConfig(UniformDistributionSpec uniformDistributionSpec) {
        Map<String, Object> originals = new HashMap<>();

        return new UniformDistributionConfig(originals);
    }

    public static UniformDistribution specToDistribution(UniformDistributionSpec uniformDistributionSpec) {
        return new UniformDistribution(specToConfig(uniformDistributionSpec));
    }

    @Override
    public DistributionType distributionType() { return DistributionType.UNIFORM; }

    public UniformDistributionConfig(Map<? extends Object, ? extends Object> originals) {
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
