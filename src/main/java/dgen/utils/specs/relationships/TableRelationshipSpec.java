package dgen.utils.specs.relationships;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({@JsonSubTypes.Type(value = DefTableRelationshipSpec.class, name = "defTableRelationship"),
        @JsonSubTypes.Type(value = GenTableRelationshipSpec.class, name = "genTableRelationship")})
@JsonTypeName("tableRelationships")
public interface TableRelationshipSpec extends RelationshipSpec { }
