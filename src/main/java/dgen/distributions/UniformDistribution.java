package dgen.distributions;

public class UniformDistribution implements Distribution {

    @Override
    public DistributionType distributionType() {
        return DistributionType.UNIFORM;
    }
}
