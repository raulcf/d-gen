package dgen.datatypes.generators;

import com.mifmif.common.regex.Generex;
import dgen.datatypes.StringType;
import dgen.datatypes.config.FloatTypeConfig;
import dgen.datatypes.config.IntegerTypeConfig;
import dgen.datatypes.config.StringTypeConfig;
import dgen.datatypes.DataType;
import dgen.datatypes.NativeType;
import dgen.distributions.Distribution;
import dgen.distributions.GaussianDistribution;
import dgen.distributions.UniformDistribution;

import java.util.Random;

public class StringTypeGenerator implements DataTypeGenerator{

    private final NativeType nativeType = NativeType.STRING;

    private String defaultValue;
    private String regexPattern;
    private int minLength;
    private int maxLength;
    private String validChars;
    private int sizeInBytes;

    private Random rnd;

    public StringTypeGenerator(StringTypeConfig dtc) {
        this.defaultValue = dtc.getString(StringTypeConfig.DEFAULT_VALUE);
        this.regexPattern = dtc.getString(StringTypeConfig.REGEX_PATTERN);
        this.maxLength = dtc.getInt(StringTypeConfig.MAX_LENGTH);
        this.minLength = dtc.getInt(StringTypeConfig.MIN_LENGTH);
        this.validChars = dtc.getString(StringTypeConfig.VALID_CHARACTERS);
        this.sizeInBytes = dtc.getInt(StringTypeConfig.SIZE_IN_BYTES);

        rnd = new Random();
    }

    public StringTypeGenerator(int maxLength) {
        this.minLength = 0;
        this.maxLength = maxLength;

        rnd = new Random();
    }

    private StringTypeGenerator() {
        this.minLength = 0;
        this.maxLength = Integer.MAX_VALUE;

        rnd = new Random();
    }

    public static StringTypeGenerator getDefault() { return new StringTypeGenerator(); }

    @Override
    public DataType drawWithReplacement(Distribution samplingDistribution) {
        // TODO: Maybe move this to another function since it doesn't depend on samplingDistribution
        if (defaultValue != null) { return new StringType(defaultValue); }
        if (regexPattern != null) { return generateRegex(); }

        switch(samplingDistribution.distributionType()) {
            case UNIFORM:
                return uniformSample();
            case GAUSSIAN:
                return gaussianSample((GaussianDistribution) samplingDistribution);
            case ZIPF:
                // TODO: to implement
                return null;
        }
        return null;
    }

    @Override
    public DataType drawWithoutReplacement(Distribution samplingDistribution) {
        return null;
    }

    private DataType generateRegex() {
        Generex generex = new Generex(regexPattern, rnd);
        return new StringType(generex.random());
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
        int stringLength = rnd.nextInt(this.maxLength - this.minLength + 1) + this.minLength;

        return new StringType(generateString(stringLength));
    }

    private DataType gaussianSample(GaussianDistribution gaussianDistribution) {
        float standardDeviation = gaussianDistribution.getStandardDeviation();
        float mean = gaussianDistribution.getMean();
        int stringLength = (int) (rnd.nextGaussian() * standardDeviation + mean);

        return new StringType(generateString(stringLength));
    }
}
