package dgen.utils.schemas.relationships;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.javatuples.Pair;


import java.util.List;
import java.util.Map;
import java.util.Set;

@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({@JsonSubTypes.Type(value = RandomGraph.class, name = "randomGraph"),
        @JsonSubTypes.Type(value = ConcentratedGraph.class, name = "concentratedGraph")})
@JsonTypeName("graphSchema")
public interface GraphSchema {
    String graphType();
    /*
     Currently there is a constraint that you can't have multiple edges from nodes in the same direction. This means
     two columns can't have multiple relationships. May want to change this later
     */
    Map<Integer, Set<Integer>> generateTableGraph(List<Integer> columnIDs, int numEdges,
                                                  Map<Integer, Set<Integer>> relationshipMap);
    // relationshipMap parameter would be better if we mapped a two-tuple to list of two-tuples
    Map<Pair<Integer, Integer>, Set<Pair<Integer, Integer>>> generateDatabaseGraph(List<Pair<Integer, Integer>> primaryKeys,
                                                                                   List<Pair<Integer, Integer>> foreignKeys,
                                                                                   int numEdges,
                                                                                   Map<Pair<Integer, Integer>, Set<Pair<Integer, Integer>>> relationshipMap);
}
