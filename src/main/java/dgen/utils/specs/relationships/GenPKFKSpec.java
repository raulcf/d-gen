package dgen.utils.specs.relationships;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("genPKFK")
public class GenPKFKSpec implements DatabaseRelationshipSpec {
    private int numRelationships;
    private GraphSpec graphSpec;

    @Override
    public RelationshipType relationshipType() { return RelationshipType.GENPKFK; }

    public int getNumRelationships() {
        return numRelationships;
    }

    public void setNumRelationships(int numRelationships) {
        this.numRelationships = numRelationships;
    }

    public GraphSpec getGraphSpec() {
        return graphSpec;
    }

    public void setGraphSpec(GraphSpec graphSpec) {
        this.graphSpec = graphSpec;
    }
}
