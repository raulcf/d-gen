package dgen.column;

public class RandomAttributeNameGenerator implements AttributeNameGenerator {

    private int nameLength;
    private final String alphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvxyz";

    public RandomAttributeNameGenerator(int nameLength) {
        this.nameLength = nameLength;
    }

    @Override
    public String generateAttributeName() {
        int n = this.nameLength;
        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index = (int) (alphaNumericString.length() * Math.random());
            // add Character one by one in end of sb
            sb.append(alphaNumericString.charAt(index));
        }
        return sb.toString();
    }
}
