package dgen.distributions.config;

import dgen.coreconfig.Config;
import dgen.coreconfig.ConfigDef;
import dgen.coreconfig.ConfigKey;
import dgen.distributions.DistributionType;
import dgen.distributions.GaussianDistribution;
import dgen.utils.specs.datatypespecs.distributionspecs.GaussianDistributionSpec;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GaussianDistributionConfig extends Config implements DistributionConfig{

    private static final ConfigDef config;

    public static final String STANDARD_DEVIATION = "standard.deviation";
    private static final String STANDARD_DEVIATION_DOC = "Indicates the standard deviation of this distribution";

    public static final String MEAN = "mean";
    private static final String MEAN_DOC = "Indicates the mean of this distribution";

    static {
        config = new ConfigDef()
                .define(STANDARD_DEVIATION, ConfigDef.Type.FLOAT, 1, ConfigDef.Importance.LOW, STANDARD_DEVIATION_DOC)
                .define(MEAN, ConfigDef.Type.FLOAT, 0, ConfigDef.Importance.LOW, MEAN_DOC);
    }

    public static GaussianDistributionConfig parseSpec(GaussianDistributionSpec gaussianDistributionSpec) {
        Map<String, Object> originals = new HashMap<>();
        originals.put("standard.deviation", gaussianDistributionSpec.getStandardDeviation());
        originals.put("mean", gaussianDistributionSpec.getMean());

        return new GaussianDistributionConfig(originals);
    }

    public static GaussianDistribution specToDistribution(GaussianDistributionSpec gaussianDistributionSpec) {
        return new GaussianDistribution(parseSpec(gaussianDistributionSpec));
    }

    @Override
    public DistributionType distributionType() {
        return DistributionType.GAUSSIAN;
    }

    public GaussianDistributionConfig(Map<? extends Object, ? extends Object> originals) {
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
