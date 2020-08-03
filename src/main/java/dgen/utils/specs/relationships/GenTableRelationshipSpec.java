package dgen.utils.specs.relationships;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import dgen.utils.SpecificationException;
import dgen.utils.specs.relationships.dependencyFunctions.DependencyFunction;

@JsonTypeName("genTableRelationship")
public class GenTableRelationshipSpec implements TableRelationshipSpec {
    private int numRelationships = 0;
    private DependencyFunction dependencyFunction;
    private GraphSpec graphSpec;


    @Override
    public RelationshipType relationshipType() {
        return RelationshipType.GENTABLE;
    }

    @Override
    public void validate() {
        if (numRelationships < 0) {
            throw new SpecificationException("numRelationships can't be less than zero");
        }
        if (dependencyFunction == null) {
            throw new SpecificationException("dependencyFunction can't be null");
        }
        if (graphSpec == null) {
            throw new SpecificationException("graphSpec can't be null");
        }
    }

    public int getNumRelationships() {
        return numRelationships;
    }

    public void setNumRelationships(int numRelationships) {
        this.numRelationships = numRelationships;
    }

    public DependencyFunction getDependencyFunction() {
        return dependencyFunction;
    }

    public void setDependencyFunction(DependencyFunction dependencyFunction) {
        this.dependencyFunction = dependencyFunction;
    }

    public GraphSpec getGraphSpec() {
        return graphSpec;
    }

    public void setGraphSpec(GraphSpec graphSpec) {
        this.graphSpec = graphSpec;
    }

    @Override
    public String toString() {
        return "GenTableRelationshipSchema{" +
                "numRelationships=" + numRelationships +
                ", dependencyFunction=" + dependencyFunction +
                ", graphSchema=" + graphSpec +
                '}';
    }
}
