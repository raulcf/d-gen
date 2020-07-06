package dgen.utils;

import dgen.utils.schemas.datatypes.Boolean;
import dgen.utils.schemas.datatypes.DataType;
import dgen.utils.schemas.datatypes.Float;
import dgen.utils.schemas.datatypes.Int;
import dgen.utils.schemas.datatypes.String;

public class DataTypeParser {
    private DataType dataType;

    public DataType getDataType() {
        return dataType;
    }
    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public void parse(DataType d) {
        switch (d.type()) {
            case INT:
                Int intType = (Int) d;
                parseInt(intType);
                break;
            case STRING:
                String stringType = (String) d;
                parseString(stringType);
                break;
            case FLOAT:
                Float floatType = (Float) d;
                parseFloat(floatType);
                break;
            case BOOLEAN:
                Boolean booleanType = (Boolean) d;
                parseBoolean(booleanType);
                break;
        }

    }

    private void parseInt(Int intType) {
        if (intType.getDefaultValue() != null) {
            intType.setMinValue(null);
            intType.setMaxValue(null);
            intType.setDistribution(null);
        }
        dataType = intType;
    }

    private void parseString(String stringType) {
        if (stringType.getDefaultValue() != null) {
            stringType.setMinLength(null);
            stringType.setMaxLength(null);
            stringType.setDistribution(null);
            stringType.setRegexName(null);
        } else if (stringType.getRegexName() != null) {
            stringType.setMinLength(null);
            stringType.setMaxLength(null);
            stringType.setDistribution(null);
        } else { assert stringType.getMinLength() <= stringType.getMaxLength(); }

        dataType = stringType;
    }

    private void parseFloat(Float floatType) {
        if (floatType.getDefaultValue() != null) {
            floatType.setMinValue(null);
            floatType.setMaxValue(null);
            floatType.setDistribution(null);
        } else { assert floatType.getMinValue() <= floatType.getMaxValue(); }

        dataType = floatType;
    }

    private void parseBoolean(Boolean booleanType) {
        assert booleanType.getTfRatio() <= 1;
    }
}
