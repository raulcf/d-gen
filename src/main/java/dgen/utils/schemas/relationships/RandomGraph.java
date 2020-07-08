package dgen.utils.schemas.relationships;

import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.*;
import java.util.Map;

@JsonTypeName("randomGraph")
public class RandomGraph implements GraphSchema {
    private float density = (float) 0.5;

    @Override
    public String graphType() {
        return null;
    }

    /**
     * Generates a random graph between the columnIDs of a table.
     * @param columnIDs A list of columnIDs to consider as "nodes" in the graph.
     * @param numEdges The number of random edges to generate. All edges are generated uniformly.
     * @param relationshipMap A map of the relationships between columns (to ensure there are no double edges).
     * @return A mapping of columnIDs.
     */
    @Override
    public Map<Integer, Set<Integer>> generateGraph(List<Integer> columnIDs, int numEdges,
                                                    Map<Integer, Set<Integer>> relationshipMap) {
        Random r = new Random();
        // Inefficient when numEdges approaches the max number of edges
        Map<Integer, Set<Integer>> adjacencyList = new HashMap<>();
        for (int i = 0; i < numEdges; i++) {
            System.out.println();
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

    public float getDensity() {
        return density;
    }

    public void setDensity(float density) {
        this.density = density;
    }
}
