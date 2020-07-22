package dgen.attributegenerators;

import com.mifmif.common.regex.Generex;
import dgen.attributegenerators.AttributeNameGenerator;

import java.util.Random;

public class RegexAttributeNameGenerator implements AttributeNameGenerator {

    private String regexPattern;
    private Generex generex;
    private Random rnd;

    public RegexAttributeNameGenerator(String regexPattern) {
        this.regexPattern = regexPattern;
        rnd = new Random();
        generex = new Generex(regexPattern, rnd);
    }

    @Override
    public String generateAttributeName() {
        return generex.random();
    }
}
