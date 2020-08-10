package dgen.utils.parsers.specs.relationships;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({@JsonSubTypes.Type(value = DefPKFKSpec.class, name = "defPKFK"),
        @JsonSubTypes.Type(value = GenPKFKSpec.class, name = "genPKFK")})
@JsonTypeName("databaseRelationships")
public interface DatabaseRelationshipSpec extends RelationshipSpec { }
