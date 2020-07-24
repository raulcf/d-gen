package dgen.attributegenerators;

import com.mifmif.common.regex.Generex;
import dgen.attributegenerators.AttributeNameGenerator;
import dgen.utils.RandomGenerator;

import java.util.Random;

public class RegexAttributeNameGenerator implements AttributeNameGenerator {

    private String regexPattern;
    private Generex generex;
    private RandomGenerator rnd;

    public RegexAttributeNameGenerator(RandomGenerator rnd, String regexPattern) {
        this.regexPattern = regexPattern;
        this.rnd = rnd;
        generex = new Generex(regexPattern, rnd);
    }

    @Override
    public String generateAttributeName() {
        return generex.random();
    }
}
