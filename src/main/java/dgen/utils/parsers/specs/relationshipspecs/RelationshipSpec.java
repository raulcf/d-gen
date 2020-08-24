package dgen.utils.parsers.specs.relationshipspecs;

public interface RelationshipSpec {
    RelationshipType relationshipType();
    void validate();
}
