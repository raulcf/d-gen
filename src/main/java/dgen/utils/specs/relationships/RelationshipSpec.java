package dgen.utils.specs.relationships;

public interface RelationshipSpec {
    RelationshipType relationshipType();
    void validate();
}
