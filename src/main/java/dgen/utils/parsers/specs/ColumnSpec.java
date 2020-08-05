package dgen.utils.parsers.specs;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import dgen.utils.parsers.specs.datatypespecs.DataTypeSpec;


@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({@JsonSubTypes.Type(value = GenColumnSpec.class, name = "genColumn"),
        @JsonSubTypes.Type(value = DefColumnSpec.class, name = "defColumn"),
        @JsonSubTypes.Type(value = DefForeignKeySpec.class, name = "defForeignKey"),
        @JsonSubTypes.Type(value = GenForeignKeySpec.class, name = "genForeignKey"),
        @JsonSubTypes.Type(value = PrimaryKeySpec.class, name = "primaryKey")})
@JsonTypeName("column")
public interface ColumnSpec extends Spec {
    ColumnSpec copy();
    Integer getColumnID();
    DataTypeSpec getDataTypeSpec();
    String getColumnName();
    void setColumnName(String columnName);
    String getRegexName();
    void setRegexName(String regexName);
    boolean isRandomName();
    void setRandomName(boolean randomName);
    boolean isHasNull();
    boolean isUnique();
    float getNullFrequency();
    Long getRandomSeed();
}
