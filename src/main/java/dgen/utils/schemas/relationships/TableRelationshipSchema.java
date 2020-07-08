package dgen.utils.schemas.relationships;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import dgen.utils.schemas.*;

@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({@JsonSubTypes.Type(value = DefTableRelationshipSchema.class, name = "defTableRelationship"),
        @JsonSubTypes.Type(value = GenTableRelationshipSchema.class, name = "genTableRelationship")})
@JsonTypeName("tableRelationships")
public interface TableRelationshipSchema {
    String relationshipType();
}
