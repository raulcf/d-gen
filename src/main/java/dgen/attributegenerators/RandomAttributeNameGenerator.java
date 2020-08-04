package dgen.attributegenerators;

import dgen.utils.RandomGenerator;

public class RandomAttributeNameGenerator implements AttributeNameGenerator {

    private int nameLength;
    private final String alphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvxyz";
    private RandomGenerator rnd;

    /**
     * Creates a generator that generates strings randomly.
     * @param rnd Random object to use when generating strings.
     */
    public RandomAttributeNameGenerator(RandomGenerator rnd) {
        this.nameLength = 10; //Decide on a default or modify specification to allow this
        this.rnd = rnd;
    }

    public RandomAttributeNameGenerator(RandomGenerator rnd, int nameLength) {
        this.rnd = rnd;
        this.nameLength = nameLength;
    }

    @Override
    public String generateAttributeName() {
        int n = this.nameLength;
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {
            int index = (int) (alphaNumericString.length() * rnd.nextFloat());
            sb.append(alphaNumericString.charAt(index));
        }
        return sb.toString();
    }
}
