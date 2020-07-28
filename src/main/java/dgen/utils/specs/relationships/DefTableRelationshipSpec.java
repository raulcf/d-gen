package dgen.utils.specs.relationships;

import com.fasterxml.jackson.annotation.JsonTypeName;
import dgen.utils.specs.relationships.dependencyFunctions.DependencyFunction;

import java.util.Map;
import java.util.Set;

@JsonTypeName("defTableRelationship")
public class DefTableRelationshipSpec implements TableRelationshipSpec {
    /* Mapping of columnID from the determinant to the dependant */
    private Map<Integer, Set<Integer>> dependencyMap;
    private DependencyFunction dependencyFunction;

    private Long randomSeed;

    @Override
    public RelationshipType relationshipType() { return RelationshipType.DEFTABLE; }

    public Map<Integer, Set<Integer>> getDependencyMap() {
        return dependencyMap;
    }

    public void setDependencyMap(Map<Integer, Set<Integer>> dependencyMap) {
        this.dependencyMap = dependencyMap;
    }

    public DependencyFunction getDependencyFunction() {
        return dependencyFunction;
    }

    public void setDependencyFunction(DependencyFunction dependencyFunction) {
        this.dependencyFunction = dependencyFunction;
    }

    public Long getRandomSeed() {
        return randomSeed;
    }

    public void setRandomSeed(Long randomSeed) {
        this.randomSeed = randomSeed;
    }

    @Override
    public String toString() {
        return "DefTableRelationshipSchema{" +
                "dependencyMap=" + dependencyMap +
                ", dependencyFunction=" + dependencyFunction +
                '}';
    }
}
