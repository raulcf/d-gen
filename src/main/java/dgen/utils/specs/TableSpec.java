package dgen.utils.specs;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({@JsonSubTypes.Type(value = GenTableSpec.class, name = "genTable"),
        @JsonSubTypes.Type(value = DefTableSpec.class, name = "defTable")})
@JsonTypeName("table")
public interface TableSpec extends Spec {
    String getTableName();
    void setTableName(String tableName);
    String getRegexName();
    void setRegexName(String regexName);
    boolean isRandomName();
    void setRandomName(boolean randomName);
}
