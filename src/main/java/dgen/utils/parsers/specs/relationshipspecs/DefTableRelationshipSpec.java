package dgen.utils.parsers.specs.relationshipspecs;

import com.fasterxml.jackson.annotation.JsonTypeName;
import dgen.utils.parsers.SpecificationException;
import dgen.utils.parsers.specs.relationshipspecs.dependencyFunctions.DependencyFunction;

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

    @Override
    public void validate() {
        if (dependencyFunction == null) {
            throw new SpecificationException("dependencyFunction can't be null");
        }
        if (dependencyMap == null) {
            throw new SpecificationException("dependencyMap can't be null");
        }
    }

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
