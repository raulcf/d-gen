package dgen.distributions;

import dgen.distributions.config.GaussianDistributionConfig;

public class GaussianDistribution implements Distribution{

    private float standardDeviation;
    private float mean;

    @Override
    public DistributionType distributionType() {
        return DistributionType.GAUSSIAN;
    }

    public GaussianDistribution(float standardDeviation, float mean) {
        this.standardDeviation = standardDeviation;
        this.mean = mean;
    }

    public GaussianDistribution(GaussianDistributionConfig dtc) {
        this.standardDeviation = dtc.getFloat(GaussianDistributionConfig.STANDARD_DEVIATION);
        this.mean = dtc.getFloat(GaussianDistributionConfig.MEAN);
    }

    public float getStandardDeviation() {
        return standardDeviation;
    }

    public float getMean() {
        return mean;
    }
}
