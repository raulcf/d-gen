package dgen.utils.parsers;

import dgen.utils.parsers.specs.datatypespecs.*;

public class DataTypeParser {
    private DataTypeSpec dataTypeSpec;
    private RandomGenerator rnd;

    public DataTypeParser(DataTypeSpec dataTypeSpec, RandomGenerator rnd) {
        this.dataTypeSpec = dataTypeSpec;
        this.rnd = rnd;
    }

    public DataTypeSpec getDataTypeSpec() {
        return dataTypeSpec;
    }
    public void setDataTypeSpec(DataTypeSpec dataTypeSpec) {
        this.dataTypeSpec = dataTypeSpec;
    }

    public void parse() {
        parseDataType();

        switch (dataTypeSpec.type()) {
            case INT:
                IntegerSpec integerType = (IntegerSpec) dataTypeSpec;
                parseInt(integerType);
                break;
            case STRING:
                StringSpec stringType = (StringSpec) dataTypeSpec;
                parseString(stringType);
                break;
            case FLOAT:
                FloatSpec floatType = (FloatSpec) dataTypeSpec;
                parseFloat(floatType);
                break;
            case BOOLEAN:
                BooleanSpec booleanType = (BooleanSpec) dataTypeSpec;
                parseBoolean(booleanType);
                break;
        }

    }

    private void parseDataType() {
        if (dataTypeSpec.getRandomSeed() == null) {
            dataTypeSpec.setRandomSeed(rnd.nextLong());
        }
    }

    private void parseInt(IntegerSpec integerType) {
        if (integerType.getDefaultValue() != null) {
            integerType.setMinValue(null);
            integerType.setMaxValue(null);
        }
        dataTypeSpec = integerType;
    }

    private void parseString(StringSpec stringType) {
        if (stringType.getDefaultValue() != null) {
            stringType.setMinLength(null);
            stringType.setMaxLength(null);
            stringType.setRegexPattern(null);
        } else if (stringType.getRegexPattern() != null) {
            stringType.setMinLength(null);
            stringType.setMaxLength(null);
        }

        dataTypeSpec = stringType;
    }

    private void parseFloat(FloatSpec floatType) {
        if (floatType.getDefaultValue() != null) {
            floatType.setMinValue(null);
            floatType.setMaxValue(null);
        }

        dataTypeSpec = floatType;
    }

    private void parseBoolean(BooleanSpec booleanType) {
        dataTypeSpec = booleanType;
    }
}
