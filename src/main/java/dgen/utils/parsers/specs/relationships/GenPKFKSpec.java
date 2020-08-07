package dgen.utils.parsers.specs.relationships;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import dgen.utils.parsers.SpecificationException;

@JsonTypeName("genPKFK")
public class GenPKFKSpec implements DatabaseRelationshipSpec {
    private int numRelationships;
    @JsonProperty("graph")
    private GraphSpec graphSpec;

    @Override
    public RelationshipType relationshipType() { return RelationshipType.GENPKFK; }

    @Override
    public void validate() {
        if (numRelationships < 0) {
            throw new SpecificationException("numRelationships can't be less than zero");
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

    public GraphSpec getGraphSpec() {
        return graphSpec;
    }

    public void setGraphSpec(GraphSpec graphSpec) {
        this.graphSpec = graphSpec;
    }
}
