package dgen.distributions;

public class GaussianDistribution implements Distribution{

    private float standardDeviation = 1;
    private float mean = 0;

    @Override
    public DistributionType distributionType() {
        return DistributionType.GAUSSIAN;
    }

    public GaussianDistribution(float standardDeviation, float mean) {
        this.standardDeviation = standardDeviation;
        this.mean = mean;
    }

    public float getStandardDeviation() {
        return standardDeviation;
    }

    public float getMean() {
        return mean;
    }
}
