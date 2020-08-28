package dgen.utils.parsers.specs.relationshipspecs;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import dgen.utils.parsers.RandomGenerator;
import dgen.utils.parsers.SpecificationException;
import org.javatuples.Pair;

import java.util.*;

@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({@JsonSubTypes.Type(value = RandomGraph.class, name = "randomGraph"),
        @JsonSubTypes.Type(value = ConcentratedGraph.class, name = "concentratedGraph")})
@JsonTypeName("graph")
public interface GraphSpec {
    GraphType graphType();
    /*
     Currently there is a constraint that you can't have multiple edges from nodes in the same direction. This means
     two columns can't have multiple relationships. May want to change this later
     */
    Map<Integer, Set<Integer>> generateTableGraph(List<Integer> columnIDs, int numEdges,
                                                  Map<Integer, Set<Integer>> relationshipMap, Set<Integer> usedColumns);
    Map<Pair<Integer, Integer>, Set<Pair<Integer, Integer>>> generateDatabaseGraph(List<Pair<Integer, Integer>> primaryKeys,
                                                                                   List<Pair<Integer, Integer>> foreignKeys,
                                                                                   int numEdges,
                                                                                   Map<Pair<Integer, Integer>, Set<Pair<Integer, Integer>>> relationshipMap);

    RandomGenerator getRandomGenerator();
    void setRandomGenerator(RandomGenerator randomGenerator);

    /**
     * Generates a random list of valid mappings between primary keys and foreign keys.
     * @param primaryKeys List of primary keys within a table.
     * @param foreignKeys List of foreign keys within a table.
     * @param numEdges Number of random edges to generate
     * @param relationshipMap Mapping of existing PK-FK relationships within a table.
     * @rnd Random object to use to generate random numbers.
     *
     * @return Mapping between primary keys and a set of foreign keys.
     */
    static Map<Pair<Integer, Integer>, Set<Pair<Integer, Integer>>> generateDatabaseEdge(List<Pair<Integer, Integer>> primaryKeys,
                                                                                         List<Pair<Integer, Integer>> foreignKeys,
                                                                                         int numEdges,
                                                                                         Map<Pair<Integer, Integer>, Set<Pair<Integer, Integer>>> relationshipMap,
                                                                                         RandomGenerator rnd) {
        Map<Pair<Integer, Integer>, Set<Pair<Integer, Integer>>> adjacencyList = new HashMap<>();

        for (int i = 0; i < numEdges; i++) {
            List<Pair<Integer, Integer>> potentialPrimaryKeys = new ArrayList<>(primaryKeys);
            List<Pair<Integer, Integer>> potentialForeignKeys = new ArrayList<>(foreignKeys);
            Pair<Integer, Integer> primaryKey = potentialPrimaryKeys.remove(rnd.nextInt(potentialPrimaryKeys.size()));
            Pair<Integer, Integer> foreignKey = potentialForeignKeys.remove(rnd.nextInt(potentialForeignKeys.size()));

            while (primaryKey.getValue0().equals(foreignKey.getValue0()) ||
                    (relationshipMap.containsKey(primaryKey) && relationshipMap.get(primaryKey).contains(foreignKey))) {
                while (potentialForeignKeys.isEmpty()) {
                    if (potentialPrimaryKeys.isEmpty()) {
                        throw new SpecificationException("Can't create more PK-FK relationships");
                    }

                    primaryKeys.remove(primaryKey);
                    primaryKey = potentialPrimaryKeys.remove(rnd.nextInt(potentialPrimaryKeys.size()));
                    potentialForeignKeys = new ArrayList<>(foreignKeys);
                }
                foreignKey = potentialForeignKeys.remove(rnd.nextInt(potentialForeignKeys.size()));
            }

            if (adjacencyList.containsKey(primaryKey)) {
                Set<Pair<Integer, Integer>> edges = adjacencyList.get(primaryKey);
                edges.add(foreignKey);
                adjacencyList.replace(primaryKey, edges);
            } else {
                Set<Pair<Integer, Integer>> edges = new HashSet<>();
                edges.add(foreignKey);
                adjacencyList.put(primaryKey, edges);
            }
            foreignKeys.remove(foreignKey);
        }
        return adjacencyList;
    }
}
