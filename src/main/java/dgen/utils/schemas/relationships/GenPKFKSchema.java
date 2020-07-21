package dgen.utils.schemas.relationships;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("genPKFK")
public class GenPKFKSchema implements DatabaseRelationshipSchema {
    private int numRelationships;
    private GraphSchema graphSchema;

    @Override
    public RelationshipType relationshipType() { return RelationshipType.GENPKFK; }

    public int getNumRelationships() {
        return numRelationships;
    }

    public void setNumRelationships(int numRelationships) {
        this.numRelationships = numRelationships;
    }

    public GraphSchema getGraphSchema() {
        return graphSchema;
    }

    public void setGraphSchema(GraphSchema graphSchema) {
        this.graphSchema = graphSchema;
    }
}
