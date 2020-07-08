package dgen.utils.schemas.relationships;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.List;
import java.util.Map;
import java.util.Set;

@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({@JsonSubTypes.Type(value = RandomGraph.class, name = "randomGraph")})
@JsonTypeName("graphSchema")
public interface GraphSchema {
    String graphType();
    Map<Integer, Set<Integer>> generateGraph(List<Integer> columnIDs, int numEdges,
                                              Map<Integer, Set<Integer>> relationshipMap);
}
