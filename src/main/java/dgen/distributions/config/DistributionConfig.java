package dgen.distributions.config;

import dgen.distributions.Distribution;
import dgen.distributions.DistributionType;
import dgen.utils.specs.datatypespecs.distributionspecs.DistributionSpec;
import dgen.utils.specs.datatypespecs.distributionspecs.GaussianDistributionSpec;
import dgen.utils.specs.datatypespecs.distributionspecs.UniformDistributionSpec;

public interface DistributionConfig {

    static Distribution specToDistribution(DistributionSpec distributionSpec) {
        switch (distributionSpec.distributionType()) {
            case GAUSSIAN:
                return GaussianDistributionConfig.specToDistribution((GaussianDistributionSpec)distributionSpec);
            case UNIFORM:
                return UniformDistributionConfig.specToDistribution((UniformDistributionSpec)distributionSpec);
        }

        return null;
    }

    DistributionType distributionType();
}
