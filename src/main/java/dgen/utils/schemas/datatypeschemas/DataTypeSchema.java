package dgen.utils.schemas.datatypeschemas;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({@JsonSubTypes.Type(value = IntegerSchema.class, name = "int"),
        @JsonSubTypes.Type(value = StringSchema.class, name = "string"),
        @JsonSubTypes.Type(value = FloatSchema.class, name = "float"),
        @JsonSubTypes.Type(value = BooleanSchema.class, name = "boolean")})
@JsonTypeName("dataType")
public interface DataTypeSchema {
    Type type();
    public void validate();
}
