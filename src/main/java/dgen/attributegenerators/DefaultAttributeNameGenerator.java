package dgen.attributegenerators;

public class DefaultAttributeNameGenerator implements AttributeNameGenerator {

    private String defaultValue;

    public DefaultAttributeNameGenerator(String defaultValue) { this.defaultValue = defaultValue; }

    @Override
    public String generateAttributeName() {
        return defaultValue;
    }
}
