package dgen.utils.schemas;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;


@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({@JsonSubTypes.Type(value = GenColumnSchema.class, name = "genColumn"),
        @JsonSubTypes.Type(value = DefColumnSchema.class, name = "defColumn"),
        @JsonSubTypes.Type(value = DefForeignKeySchema.class, name = "defForeignKey"),
        @JsonSubTypes.Type(value = GenForeignKeySchema.class, name = "genForeignKey"),
        @JsonSubTypes.Type(value = PrimaryKeySchema.class, name = "primaryKey")})
@JsonTypeName("column")
public interface ColumnSchema {
    String schemaType();
}
