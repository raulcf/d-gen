package dgen.distributions;

import dgen.distributions.config.UniformDistributionConfig;

public class UniformDistribution implements Distribution {

    @Override
    public DistributionType distributionType() {
        return DistributionType.UNIFORM;
    }

    public UniformDistribution() {}

    public UniformDistribution(UniformDistributionConfig dtc) {

    }
}
