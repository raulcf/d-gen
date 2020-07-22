package dgen.utils;

import dgen.utils.specs.datatypespecs.*;
import dgen.utils.specs.datatypespecs.DataTypeSpec;

public class DataTypeParser {
    private DataTypeSpec dataTypeSpec;

    public DataTypeSpec getDataTypeSpec() {
        return dataTypeSpec;
    }
    public void setDataTypeSpec(DataTypeSpec dataTypeSpec) {
        this.dataTypeSpec = dataTypeSpec;
    }

    public void parse(DataTypeSpec d) {
        switch (d.type()) {
            case INT:
                IntegerSpec integerType = (IntegerSpec) d;
                parseInt(integerType);
                break;
            case STRING:
                StringSpec stringType = (StringSpec) d;
                parseString(stringType);
                break;
            case FLOAT:
                FloatSpec floatType = (FloatSpec) d;
                parseFloat(floatType);
                break;
            case BOOLEAN:
                BooleanSpec booleanType = (BooleanSpec) d;
                parseBoolean(booleanType);
                break;
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
