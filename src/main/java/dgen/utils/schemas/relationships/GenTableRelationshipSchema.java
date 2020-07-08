package dgen.utils.schemas.relationships;

import com.fasterxml.jackson.annotation.JsonTypeName;
import dgen.utils.schemas.relationships.dependencyFunctions.DependencyFunction;

@JsonTypeName("genTableRelationship")
public class GenTableRelationshipSchema implements TableRelationshipSchema {
    private int numRelationships = 0;
    private DependencyFunction dependencyFunction;
    private GraphSchema graphSchema;


    @Override
    public String relationshipType() {
        return "genTableRelationship";
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

    public GraphSchema getGraphSchema() {
        return graphSchema;
    }

    public void setGraphSchema(GraphSchema graphSchema) {
        this.graphSchema = graphSchema;
    }

    @Override
    public String toString() {
        return "GenTableRelationshipSchema{" +
                "numRelationships=" + numRelationships +
                ", dependencyFunction=" + dependencyFunction +
                ", graphSchema=" + graphSchema +
                '}';
    }
}
