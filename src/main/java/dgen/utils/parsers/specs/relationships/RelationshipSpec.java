package dgen.utils.parsers.specs.relationships;

public interface RelationshipSpec {
    RelationshipType relationshipType();
    void validate();
}
