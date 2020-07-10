package dgen.utils.schemas.relationships;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({@JsonSubTypes.Type(value = DefPKFKSchema.class, name = "defPKFK"),
        @JsonSubTypes.Type(value = GenPKFKSchema.class, name = "genPKFK")})
@JsonTypeName("databaseRelationships")
public interface DatabaseRelationshipSchema {
    String relationshipType();
}
