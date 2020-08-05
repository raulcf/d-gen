package dgen.attributegenerators;

import com.mifmif.common.regex.Generex;
import dgen.utils.parsers.RandomGenerator;

public class RegexAttributeNameGenerator implements AttributeNameGenerator {

    private Generex generex;

    /**
     * Creates a generator that creates strings that match regex patterns.
     * @param rnd Random object to use when generating strings.
     * @param regexPattern Regex pattern to follow when generating strings.
     */
    public RegexAttributeNameGenerator(RandomGenerator rnd, String regexPattern) {
        generex = new Generex(regexPattern, rnd);
    }

    @Override
    public String generateAttributeName() {
        return generex.random();
    }
}
