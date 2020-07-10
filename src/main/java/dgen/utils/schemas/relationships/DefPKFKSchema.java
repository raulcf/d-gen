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
    private Pair<Integer, Integer> start;
    /* Foreign key*/
    @JsonIgnore
    private Pair<Integer, Integer> end;

    public void validate() {
        if (start.getValue0() == end.getValue0()) {
            throw new SpecificationException("Can't have PK linked to FK within same table");
        }
    }

    public void parseMapping() {
        if (pkfkMapping != null) {
            String startString = (String) pkfkMapping.keySet().toArray()[0];
            String endString = pkfkMapping.get(startString);

            start = Pair.with(Integer.parseInt(startString.split(":")[0]),
                    Integer.parseInt(startString.split(":")[1]));
            end = Pair.with(Integer.parseInt(endString.split(":")[0]),
                    Integer.parseInt(endString.split(":")[1]));
        }

    }

    public void unparseMapping() {
        String startString = start.getValue0() + ":" + start.getValue1();
        String endString = end.getValue0() + ":" + end.getValue1();
        pkfkMapping = new HashMap<>();
        pkfkMapping.put(startString, endString);
    }

    @Override
    public String relationshipType() { return "defPKFK"; }

    public Map<String, String> getPkfkMapping() {
        return pkfkMapping;
    }

    public void setPkfkMapping(Map<String, String> pkfkMapping) {
        this.pkfkMapping = pkfkMapping;
    }

    public Pair<Integer, Integer> getStart() {
        return start;
    }

    public void setStart(Pair<Integer, Integer> start) {
        this.start = start;
    }

    public Pair<Integer, Integer> getEnd() {
        return end;
    }

    public void setEnd(Pair<Integer, Integer> end) {
        this.end = end;
    }

    @Override
    public String toString() {
        return "DefPKFKSchema{" +
                "pkfkMapping=" + pkfkMapping +
                ", start=" + start +
                ", end=" + end +
                '}';
    }
}
