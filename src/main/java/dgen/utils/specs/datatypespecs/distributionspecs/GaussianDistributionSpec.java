package dgen.utils.specs.datatypespecs.distributionspecs;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("gaussian")
public class GaussianDistributionSpec implements DistributionSpec {

    private float standardDeviation = 1;
    private float mean = 0;

    @Override
    public Distributions distributionType() {
        return Distributions.GAUSSIAN;
    }

    public float getStandardDeviation() {
        return standardDeviation;
    }

    public void setStandardDeviation(float standardDeviation) {
        this.standardDeviation = standardDeviation;
    }

    public float getMean() {
        return mean;
    }

    public void setMean(float mean) {
        this.mean = mean;
    }
}
