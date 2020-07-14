package dgen.utils.schemas.relationships;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;
import dgen.utils.SpecificationException;
import org.javatuples.Pair;

import java.util.*;

@JsonTypeName("defPKFK")
public class DefPKFKSchema implements DatabaseRelationshipSchema {
    /*
    TODO: Rethink how to better store tableID and columnID information
    Right now in the YAML people say "1:1": "2:2" to signify that table 1 column 1 maps to table 2 column 2.
    You can't have tuples in YAML but this syntax is ugly to parse.
     */
    private Map<String, String> pkfkMapping;
    /* Primary key */
    @JsonIgnore
    private Pair<Integer, Integer> primaryKey;
    /* Foreign key*/
    @JsonIgnore
    private Pair<Integer, Integer> foreignKey;

    @Override
    public RelationshipType relationshipType() { return RelationshipType.DEFPKFK; }

    public void validate() {
        if (primaryKey.getValue0() == foreignKey.getValue0()) {
            throw new SpecificationException("Can't have PK linked to FK within same table");
        }
    }

    public void parseMapping() {
        if (pkfkMapping != null) {
            String startString = (String) pkfkMapping.keySet().toArray()[0];
            String endString = pkfkMapping.get(startString);

            primaryKey = Pair.with(Integer.parseInt(startString.split(":")[0]),
                    Integer.parseInt(startString.split(":")[1]));
            foreignKey = Pair.with(Integer.parseInt(endString.split(":")[0]),
                    Integer.parseInt(endString.split(":")[1]));
        }

    }

    public void unparseMapping() {
        String startString = primaryKey.getValue0() + ":" + primaryKey.getValue1();
        String endString = foreignKey.getValue0() + ":" + foreignKey.getValue1();
        pkfkMapping = new HashMap<>();
        pkfkMapping.put(startString, endString);
    }

    public Map<String, String> getPkfkMapping() {
        return pkfkMapping;
    }

    public void setPkfkMapping(Map<String, String> pkfkMapping) {
        this.pkfkMapping = pkfkMapping;
    }

    public Pair<Integer, Integer> getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(Pair<Integer, Integer> primaryKey) {
        this.primaryKey = primaryKey;
    }

    public Pair<Integer, Integer> getForeignKey() {
        return foreignKey;
    }

    public void setForeignKey(Pair<Integer, Integer> foreignKey) {
        this.foreignKey = foreignKey;
    }

    @Override
    public String toString() {
        return "DefPKFKSchema{" +
                "pkfkMapping=" + pkfkMapping +
                ", start=" + primaryKey +
                ", end=" + foreignKey +
                '}';
    }
}
