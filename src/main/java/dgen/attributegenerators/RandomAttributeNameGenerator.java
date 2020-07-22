package dgen.column;

public class RandomAttributeNameGenerator implements AttributeNameGenerator {

    private int nameLength;
    private final String alphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvxyz";

    public RandomAttributeNameGenerator() { this.nameLength = 10; } //Decide on a default or modify specification to allow this

    public RandomAttributeNameGenerator(int nameLength) {
        this.nameLength = nameLength;
    }

    @Override
    public String generateAttributeName() {
        int n = this.nameLength;
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {
            int index = (int) (alphaNumericString.length() * Math.random());
            sb.append(alphaNumericString.charAt(index));
        }
        return sb.toString();
    }
}
