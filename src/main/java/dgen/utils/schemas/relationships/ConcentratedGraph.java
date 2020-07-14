package dgen.utils.schemas.relationships;

import com.fasterxml.jackson.annotation.JsonTypeName;
import dgen.utils.SpecificationException;
import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;


/* A concentrated graph (there might be a better name) is a graph where a certain number of nodes (numNodes) has a
* percentage (nodeConcentration) of edges within the graph.
* */
@JsonTypeName("concentratedGraph")
public class ConcentratedGraph implements GraphSchema {
    private Integer numNodes;
    private Integer minNodes;
    private Integer maxNodes;
    private Float nodeConcentration;
    private Float minNodeConcentration;
    private Float maxNodeConcentration;

    @Override
    public GraphType graphType() {
        return GraphType.CONCENTRATED;
    }

    private void parse() {
        Random r = new Random();

        if (numNodes != null) {
            minNodes = null;
            maxNodes = null;
        } else if (minNodes != null && maxNodes != null) {
            numNodes = r.nextInt(maxNodes - minNodes + 1) + minNodes;
            minNodes = null;
            maxNodes = null;
        } else {
            throw new SpecificationException("numNodes or maxNodes and minNodes must have a value");
        }

        if (nodeConcentration != null) {
            if (nodeConcentration > 1) {
                throw new SpecificationException("nodeConcentration must be less than 1");
            }
            minNodeConcentration = null;
            maxNodeConcentration = null;
        } else if (minNodeConcentration != null && maxNodeConcentration != null) {
            if (minNodeConcentration < 0 || maxNodeConcentration > 1 || minNodeConcentration > maxNodeConcentration) {
                throw new SpecificationException("minNodeConcentration and maxNodeConcentration must be within [0,1]");
            }
            nodeConcentration = (maxNodeConcentration - minNodeConcentration) * r.nextFloat() + minNodeConcentration;
            minNodes = null;
            maxNodes = null;
        } else {
            throw new SpecificationException("nodeConcentration or maxNodeConcentration and minNodeConcentration must have a value");
        }
    }

    @Override
    public Map<Integer, Set<Integer>> generateTableGraph(List<Integer> columnIDs, int numEdges, Map<Integer, Set<Integer>> relationshipMap) {
        return null;
    }


    /**
     * Generates an adjacency list with a certain number of edges concentrated around a certain number of nodes. All non-concentrated
     * nodes are added randomly.
     * @param primaryKeys List of primary keys in a table.
     * @param foreignKeys List of foreign keys in a table.
     * @param numEdges Number of edges to generate (nodeConcentration * numEdges edges will be concentrated)
     * @param relationshipMap A map of existing PK-FK relationships (to ensure no duplicates).
     * @return Mapping between primary keys and a set of foreign keys.
     */
    @Override
    public Map<Pair<Integer, Integer>, Set<Pair<Integer, Integer>>> generateDatabaseGraph(List<Pair<Integer, Integer>> primaryKeys,
                                                                                          List<Pair<Integer, Integer>> foreignKeys,
                                                                                          int numEdges,
                                                                                          Map<Pair<Integer, Integer>, Set<Pair<Integer, Integer>>> relationshipMap) {
        this.parse();

        Random r = new Random();
        int numConcentratedEdges = (int) (numEdges * nodeConcentration);

        /**
         * TODO: This needs a check to make sure that the nodes in concentration can form enough relationships to satisfy
         * numConcentrationEdges. This is very similar to the 0-1 knapsack problem but the weight (number of possible relationships)
         * of a node (primary key) changes depending on what's in the knapsack
         */
        List<Pair<Integer, Integer>> concentration = new ArrayList<>(); //Concentrated primary keys
        for (int i = 0; i < numNodes; i++) {
            concentration.add(primaryKeys.remove(r.nextInt(primaryKeys.size())));
        }


        Map<Pair<Integer, Integer>, Set<Pair<Integer, Integer>>> adjacencyList = new HashMap<>(GraphSchema.generateDatabaseEdge(concentration, foreignKeys, numConcentratedEdges, relationshipMap, r));
        adjacencyList.putAll(GraphSchema.generateDatabaseEdge(primaryKeys, foreignKeys, numEdges - numConcentratedEdges, relationshipMap, r));

        return adjacencyList;
    }

    public Integer getNumNodes() {
        return numNodes;
    }

    public void setNumNodes(Integer numNodes) {
        this.numNodes = numNodes;
    }

    public Integer getMinNodes() {
        return minNodes;
    }

    public void setMinNodes(Integer minNodes) {
        this.minNodes = minNodes;
    }

    public Integer getMaxNodes() {
        return maxNodes;
    }

    public void setMaxNodes(Integer maxNodes) {
        this.maxNodes = maxNodes;
    }

    public Float getNodeConcentration() {
        return nodeConcentration;
    }

    public void setNodeConcentration(Float nodeConcentration) {
        this.nodeConcentration = nodeConcentration;
    }

    public Float getMinNodeConcentration() {
        return minNodeConcentration;
    }

    public void setMinNodeConcentration(Float minNodeConcentration) {
        this.minNodeConcentration = minNodeConcentration;
    }

    public Float getMaxNodeConcentration() {
        return maxNodeConcentration;
    }

    public void setMaxNodeConcentration(Float maxNodeConcentration) {
        this.maxNodeConcentration = maxNodeConcentration;
    }
}
