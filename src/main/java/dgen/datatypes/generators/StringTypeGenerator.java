package dgen.datatypes.generators;

import com.mifmif.common.regex.Generex;
import dgen.datatypes.IntegerType;
import dgen.datatypes.StringType;
import dgen.datatypes.config.FloatTypeConfig;
import dgen.datatypes.config.IntegerTypeConfig;
import dgen.datatypes.config.StringTypeConfig;
import dgen.datatypes.DataType;
import dgen.datatypes.NativeType;
import dgen.distributions.Distribution;
import dgen.distributions.GaussianDistribution;
import dgen.distributions.UniformDistribution;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class StringTypeGenerator implements DataTypeGenerator{

    private final NativeType nativeType = NativeType.STRING;

    private Set<String> drawnStrings = new HashSet<>();

    private String defaultValue;
    private String regexPattern;
    private int minLength;
    private int maxLength;
    private Distribution distribution;
    private String validChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvxyz";
    private int sizeInBytes;

    private Random rnd;

    public StringTypeGenerator(StringTypeConfig dtc) {
        this.defaultValue = dtc.getString(StringTypeConfig.DEFAULT_VALUE);
        this.regexPattern = dtc.getString(StringTypeConfig.REGEX_PATTERN);
        this.maxLength = dtc.getInt(StringTypeConfig.MAX_LENGTH);
        this.minLength = dtc.getInt(StringTypeConfig.MIN_LENGTH);
        this.distribution = (Distribution)dtc.getObject(StringTypeConfig.DISTRIBUTION);
        this.validChars = dtc.getString(StringTypeConfig.VALID_CHARACTERS);
        this.sizeInBytes = dtc.getInt(StringTypeConfig.SIZE_IN_BYTES);

        rnd = new Random();
    }

    public StringTypeGenerator(int maxLength) {
        this.minLength = 0;
        this.maxLength = maxLength;
        this.distribution = new UniformDistribution();

        rnd = new Random();
    }

    private StringTypeGenerator() {
        this.minLength = 0;
        this.maxLength = Integer.MAX_VALUE;
        this.distribution = new UniformDistribution();

        rnd = new Random();
    }

    public static StringTypeGenerator getDefault() { return new StringTypeGenerator(); }

    @Override
    public DataType drawWithReplacement() {
        if (defaultValue != null) { return new StringType(defaultValue); }
        if (regexPattern != null) { return generateRegex() ;}

        // TODO: unless we need to know the specific distribution here, this switch should dissappear.
        switch(distribution.distributionType()) {
            case UNIFORM:
                return uniformSample();
            case GAUSSIAN:
                return gaussianSample((GaussianDistribution) distribution);
            case ZIPF:
                // TODO: to implement
                return null;
        }
        return null;
    }

    @Override
    public DataType drawWithoutReplacement() {
        if (defaultValue != null) { return new StringType(defaultValue); }
        if (regexPattern != null) { return generateRegexWithoutReplacement() ;}

        // TODO: unless we need to know the specific distribution here, this switch should dissappear.
        switch(distribution.distributionType()) {
            //TODO: Make this more efficient for lots of calls or small bounds
            case UNIFORM:
                return uniformSampleWithoutReplacement();
            case GAUSSIAN:
                return gaussianSampleWithoutReplacement((GaussianDistribution) distribution);
            case ZIPF:
                // TODO: to implement
                return null;
        }
        return null;
    }

    private DataType generateRegex() {
        Generex generex = new Generex(regexPattern, rnd);
        return new StringType(generex.random());
    }

    private DataType generateRegexWithoutReplacement() {
        Generex generex = new Generex(regexPattern, rnd);
        String generatedString = generex.random();

        while (drawnStrings.contains(generatedString)) {
            generatedString = generex.random();
        }

        drawnStrings.add(generatedString);
        return new StringType(generatedString);
    }

    private String generateString(int stringLength) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < stringLength; i++) {
            int index = (int)(validChars.length() * rnd.nextFloat());

            sb.append(validChars.charAt(index));
        }

        return sb.toString();
    }

    private DataType uniformSample() {
        int stringLength = rnd.nextInt(this.maxLength - this.minLength) + this.minLength;

        return new StringType(generateString(stringLength));
    }

    private DataType gaussianSample(GaussianDistribution gaussianDistribution) {
        float standardDeviation = gaussianDistribution.getStandardDeviation();
        float mean = gaussianDistribution.getMean();
        int stringLength = (int) (rnd.nextGaussian() * standardDeviation + mean);

        return new StringType(generateString(stringLength));
    }

    private DataType uniformSampleWithoutReplacement() {
        int stringLength = rnd.nextInt(this.maxLength - this.minLength) + this.minLength;
        String generatedString = generateString(stringLength);

        while (drawnStrings.contains(generatedString)) {
            generatedString = generateString(stringLength);
        }

        drawnStrings.add(generatedString);
        return new StringType(generatedString);
    }

    private DataType gaussianSampleWithoutReplacement(GaussianDistribution gaussianDistribution) {
        float standardDeviation = gaussianDistribution.getStandardDeviation();
        float mean = gaussianDistribution.getMean();
        int stringLength = (int) (rnd.nextGaussian() * standardDeviation + mean);
        String generatedString = generateString(stringLength);

        while (drawnStrings.contains(generatedString)) {
            generatedString = generateString(stringLength);
        }

        drawnStrings.add(generatedString);
        return new StringType(generatedString);
    }
}
