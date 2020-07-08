package dgen.utils.schemas.datatypes;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({@JsonSubTypes.Type(value = Int.class, name = "int"),
        @JsonSubTypes.Type(value = String.class, name = "string"),
        @JsonSubTypes.Type(value = Float.class, name = "float"),
        @JsonSubTypes.Type(value = Boolean.class, name = "boolean")})
@JsonTypeName("dataType")
public interface DataType {
    Type type();
    public void validate();
}
