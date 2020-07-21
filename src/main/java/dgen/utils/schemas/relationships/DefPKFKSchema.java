package dgen.utils.schemas.relationships;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;
import dgen.utils.SpecificationException;
import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonTypeName("defPKFK")
public class DefPKFKSchema implements DatabaseRelationshipSchema {
    /*
    TODO: Rethink how to better store tableID and columnID information
    Right now in the YAML people say "1:1": "2:2" to signify that table 1 column 1 maps to table 2 column 2.
    You can't have tuples in YAML but this syntax is ugly to parse.
     */
    private List<Map<String, String>> pkfkMappings = new ArrayList<>();
    /* Primary key */
    @JsonIgnore
    private List<Pair<Integer, Integer>> primaryKeys = new ArrayList<>();
    /* Foreign key*/
    @JsonIgnore
    private List<Pair<Integer, Integer>> foreignKeys = new ArrayList<>();

    @Override
    public RelationshipType relationshipType() { return RelationshipType.DEFPKFK; }

    public void validate() {
        for (int i = 0; i < primaryKeys.size(); i++) {
            Pair<Integer, Integer> primaryKey = primaryKeys.get(i);
            Pair<Integer, Integer> foreignKey = foreignKeys.get(i);

            if (primaryKey.getValue0() == foreignKey.getValue0()) {
                throw new SpecificationException("Can't have PK linked to FK within same table");
            }
        }
    }

    public void parseMapping() {
        if (pkfkMappings != null) {
            for (Map<String, String> pkfkMapping: pkfkMappings) {
                String pkString = (String) pkfkMapping.keySet().toArray()[0];
                String fkString = pkfkMapping.get(pkString);

                primaryKeys.add(Pair.with(Integer.parseInt(pkString.split(":")[0]),
                        Integer.parseInt(pkString.split(":")[1])));
                foreignKeys.add(Pair.with(Integer.parseInt(fkString.split(":")[0]),
                        Integer.parseInt(fkString.split(":")[1])));
            }
        }

    }

    public void unparseMapping() {
        for (int i = 0; i < primaryKeys.size(); i++) {
            Pair<Integer, Integer> primaryKey = primaryKeys.get(i);
            Pair<Integer, Integer> foreignKey = foreignKeys.get(i);

            String pkString = primaryKey.getValue0() + ":" + primaryKey.getValue1();
            String fkString = foreignKey.getValue0() + ":" + foreignKey.getValue1();
            Map<String, String> pkfkMapping = new HashMap<>();
            pkfkMapping.put(pkString, fkString);

            pkfkMappings.add(pkfkMapping);
        }
    }

    public List<Map<String, String>> getPkfkMappings() {
        return pkfkMappings;
    }

    public void setPkfkMappings(List<Map<String, String>> pkfkMappings) {
        this.pkfkMappings = pkfkMappings;
    }

    public List<Pair<Integer, Integer>> getPrimaryKeys() {
        return primaryKeys;
    }

    public void setPrimaryKeys(List<Pair<Integer, Integer>> primaryKeys) {
        this.primaryKeys = primaryKeys;
    }

    public List<Pair<Integer, Integer>> getForeignKeys() {
        return foreignKeys;
    }

    public void setForeignKeys(List<Pair<Integer, Integer>> foreignKeys) {
        this.foreignKeys = foreignKeys;
    }

    @Override
    public String toString() {
        return "DefPKFKSchema{" +
                "pkfkMappings=" + pkfkMappings +
                ", primaryKeys=" + primaryKeys +
                ", foreignKeys=" + foreignKeys +
                '}';
    }
}
