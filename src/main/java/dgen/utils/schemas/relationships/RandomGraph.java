package dgen.utils.schemas.relationships;

import com.fasterxml.jackson.annotation.JsonTypeName;
import org.javatuples.Pair;

import java.util.*;
import java.util.Map;

@JsonTypeName("randomGraph")
public class RandomGraph implements GraphSchema {
    @Override
    public String graphType() {
        return "random";
    }

    /**
     * Generates a random graph between the columnIDs of a table.
     * @param columnIDs A list of columnIDs to consider as "nodes" in the graph.
     * @param numEdges The number of random edges to generate. All edges are generated uniformly.
     * @param relationshipMap A map of the relationships between columns (to ensure there are no double edges).
     * @return A mapping of columnIDs.
     */
    @Override
    public Map<Integer, Set<Integer>> generateTableGraph(List<Integer> columnIDs, int numEdges,
                                                    Map<Integer, Set<Integer>> relationshipMap) {
        Random r = new Random();
        // Inefficient when numEdges approaches the max number of edges
        Map<Integer, Set<Integer>> adjacencyList = new HashMap<>();
        for (int i = 0; i < numEdges; i++) {
            int start = columnIDs.get(r.nextInt(columnIDs.size()));
            int end = columnIDs.get(r.nextInt(columnIDs.size()));

            while ((start == end) || (adjacencyList.containsKey(start) && adjacencyList.get(start).contains(end)) ||
                    (relationshipMap.containsKey(start) && relationshipMap.get(start).contains(end))) {
                start = columnIDs.get(r.nextInt(columnIDs.size()));
                end = columnIDs.get(r.nextInt(columnIDs.size()));
            }

            if (adjacencyList.containsKey(start)) {
                Set<Integer> edges = adjacencyList.get(start);
                edges.add(end);
                adjacencyList.replace(start, edges);
            } else {
                Set<Integer> edges = new HashSet<>();
                edges.add(end);
                adjacencyList.put(start, edges);
            }
        }

        return adjacencyList;
    }

    /**
     * Creates a random graph between foreign keys and primary keys.
     * @param primaryKeys List of primary keys within a database.
     * @param foreignKeys List of foreign keys within a database.
     * @param numEdges Number of PK-FK relationships to create.
     * @param relationshipMap A map of existing PK-FK relationships (to ensure no duplicates).
     * @return
     */
    @Override
    public Map<Pair<Integer, Integer>, Set<Pair<Integer, Integer>>> generateDatabaseGraph(List<Pair<Integer, Integer>> primaryKeys,
                                                                                         List<Pair<Integer, Integer>> foreignKeys,
                                                                                         int numEdges,
                                                                                         Map<Pair<Integer, Integer>, Set<Pair<Integer, Integer>>> relationshipMap) {
        Random r = new Random();
        // Inefficient when numEdges approaches the max number of edges
        Map<Pair<Integer, Integer>, Set<Pair<Integer, Integer>>> adjacencyList = new HashMap<>();
        generateDatabaseEdge(primaryKeys, foreignKeys, numEdges, relationshipMap, r, adjacencyList);
        return adjacencyList;
    }

    static void generateDatabaseEdge(List<Pair<Integer, Integer>> primaryKeys, List<Pair<Integer, Integer>> foreignKeys, int numEdges, Map<Pair<Integer, Integer>, Set<Pair<Integer, Integer>>> relationshipMap, Random r, Map<Pair<Integer, Integer>, Set<Pair<Integer, Integer>>> adjacencyList) {
        for (int i = 0; i < numEdges; i++) {
            Pair<Integer, Integer> start = primaryKeys.get(r.nextInt(primaryKeys.size()));
            Pair<Integer, Integer> end = foreignKeys.get(r.nextInt(foreignKeys.size()));

            while ((start.getValue0().equals(end.getValue0())) || (adjacencyList.containsKey(start) && adjacencyList.get(start).contains(end)) ||
                    (relationshipMap.containsKey(start) && relationshipMap.get(start).contains(end))) {
                start = primaryKeys.get(r.nextInt(primaryKeys.size()));
                end = foreignKeys.get(r.nextInt(foreignKeys.size()));
            }

            if (adjacencyList.containsKey(start)) {
                Set<Pair<Integer, Integer>> edges = adjacencyList.get(start);
                edges.add(end);
                adjacencyList.replace(start, edges);
            } else {
                Set<Pair<Integer, Integer>> edges = new HashSet<>();
                edges.add(end);
                adjacencyList.put(start, edges);
            }
            foreignKeys.remove(end);
        }
    }

}
