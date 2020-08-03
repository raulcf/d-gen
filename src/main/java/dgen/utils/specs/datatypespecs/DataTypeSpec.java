package dgen.utils.specs.datatypespecs;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({@JsonSubTypes.Type(value = IntegerSpec.class, name = "int"),
        @JsonSubTypes.Type(value = StringSpec.class, name = "string"),
        @JsonSubTypes.Type(value = FloatSpec.class, name = "float"),
        @JsonSubTypes.Type(value = BooleanSpec.class, name = "boolean")})
@JsonTypeName("dataType")
public interface DataTypeSpec {
    DataTypes type();
    public void validate();
    public Long getRandomSeed();
    public void setRandomSeed(Long randomSeed);
    DataTypeSpec copy();
}
