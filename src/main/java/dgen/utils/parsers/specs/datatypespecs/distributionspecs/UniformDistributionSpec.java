package dgen.utils.parsers.specs.datatypespecs.distributionspecs;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("uniform")
public class UniformDistributionSpec implements DistributionSpec {

    @Override
    public Distributions distributionType() {
        return Distributions.UNIFORM;
    }
}
