package dgen.utils.specs.datatypespecs.distributionspecs;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({@JsonSubTypes.Type(value = GaussianDistributionSpec.class, name = "gaussian"),
        @JsonSubTypes.Type(value = UniformDistributionSpec.class, name = "uniform")})
@JsonTypeName("distribution")
public interface DistributionSpec {
    Distributions distributionType();
}
