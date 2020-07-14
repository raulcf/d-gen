package dgen.utils.schemas.relationships;

import com.fasterxml.jackson.annotation.JsonTypeName;
import dgen.utils.schemas.relationships.dependencyFunctions.DependencyFunction;

import java.util.Map;
import java.util.Set;

@JsonTypeName("defTableRelationship")
public class DefTableRelationshipSchema implements TableRelationshipSchema {
    /* Mapping of columnID from the determinant to the dependant */
    private Map<Integer, Set<Integer>> dependencyMap;
    private DependencyFunction dependencyFunction;

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

    @Override
    public String toString() {
        return "DefTableRelationshipSchema{" +
                "dependencyMap=" + dependencyMap +
                ", dependencyFunction=" + dependencyFunction +
                '}';
    }
}
