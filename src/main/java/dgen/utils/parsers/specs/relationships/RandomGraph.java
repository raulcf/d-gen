package dgen.utils.parsers.specs.relationships;

import com.fasterxml.jackson.annotation.JsonTypeName;
import dgen.utils.parsers.RandomGenerator;
import dgen.utils.parsers.SpecificationException;
import org.javatuples.Pair;

import java.util.*;

@JsonTypeName("randomGraph")
public class RandomGraph implements GraphSpec {
    private RandomGenerator rnd;

    @Override
    public GraphType graphType() {
        return GraphType.RANDOM;
    }

    /**
     * Generates a random graph between the columnIDs of a table.
     * @param columnIDs A list of columnIDs to consider as "nodes" in the graph.
     * @param numEdges The number of random edges to generate. All edges are generated uniformly.
     * @param relationshipMap A map of the relationships between columns (to ensure there are no double edges).
     * @param usedColumns Set of columns that can no longer have relationships.
     * @return A mapping of columnIDs.
     */
    /**
     * Right now this function is very simplistic because it seems that there are very few constraints on relationships
     * between columns. You can have cyclic relationships and multiple edges from one node. This probably will need to
     * change when there are more constraints.
     */
    @Override
    public Map<Integer, Set<Integer>> generateTableGraph(List<Integer> columnIDs, int numEdges, Map<Integer, Set<Integer>> relationshipMap,
                                                         Set<Integer> usedColumns) {
        // Inefficient when numEdges approaches the max number of edges
        Map<Integer, Set<Integer>> adjacencyList = new HashMap<>();

        for (int i = 0; i < numEdges; i++) {
            List<Integer> potentialStart = new ArrayList<>(columnIDs);
            List<Integer> potentialEnd = new ArrayList<>(columnIDs);
            potentialEnd.removeAll(usedColumns);
            int start = potentialStart.get(rnd.nextInt(potentialStart.size()));
            int end = potentialEnd.get(rnd.nextInt(potentialEnd.size()));

            while ((start == end) || (relationshipMap.containsKey(start) && relationshipMap.get(start).contains(end))) {
                while (potentialEnd.isEmpty()) {
                    if (potentialStart.isEmpty()) {
                        throw new SpecificationException("Can't create more table relationships");
                    }

                    start = potentialStart.remove(rnd.nextInt(potentialStart.size()));
                    potentialEnd = new ArrayList<>(columnIDs);
                    potentialEnd.removeAll(usedColumns);
                }
                end = potentialEnd.remove(rnd.nextInt(potentialEnd.size()));
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

            usedColumns.add(end);
        }

        return adjacencyList;
    }

    /**
     * Creates a random graph between foreign keys and primary keys.
     * @param primaryKeys List of primary keys within a database.
     * @param foreignKeys List of foreign keys within a database.
     * @param numEdges Number of PK-FK relationships to create.
     * @param relationshipMap A map of existing PK-FK relationships (to ensure no duplicates).
     * @return Mapping between primary keys and a set of foreign keys.
     */
    @Override
    public Map<Pair<Integer, Integer>, Set<Pair<Integer, Integer>>> generateDatabaseGraph(List<Pair<Integer, Integer>> primaryKeys,
                                                                                          List<Pair<Integer, Integer>> foreignKeys,
                                                                                          int numEdges,
                                                                                          Map<Pair<Integer, Integer>, Set<Pair<Integer, Integer>>> relationshipMap) {
        return GraphSpec.generateDatabaseEdge(primaryKeys, foreignKeys, numEdges, relationshipMap, rnd);
    }

    public RandomGenerator getRandomGenerator() {
        return rnd;
    }

    public void setRandomGenerator(RandomGenerator randomGenerator) {
        this.rnd = randomGenerator;
    }
}
