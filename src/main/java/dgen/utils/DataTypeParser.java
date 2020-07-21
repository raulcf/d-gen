package dgen.utils;

import dgen.utils.schemas.datatypeschemas.BooleanSchema;
import dgen.utils.schemas.datatypeschemas.DataTypeSchema;
import dgen.utils.schemas.datatypeschemas.FloatSchema;
import dgen.utils.schemas.datatypeschemas.IntegerSchema;
import dgen.utils.schemas.datatypeschemas.StringSchema;

public class DataTypeParser {
    private DataTypeSchema dataTypeSchema;

    public DataTypeSchema getDataTypeSchema() {
        return dataTypeSchema;
    }
    public void setDataTypeSchema(DataTypeSchema dataTypeSchema) {
        this.dataTypeSchema = dataTypeSchema;
    }

    public void parse(DataTypeSchema d) {
        switch (d.type()) {
            case INT:
                IntegerSchema integerType = (IntegerSchema) d;
                parseInt(integerType);
                break;
            case STRING:
                StringSchema stringType = (StringSchema) d;
                parseString(stringType);
                break;
            case FLOAT:
                FloatSchema floatType = (FloatSchema) d;
                parseFloat(floatType);
                break;
            case BOOLEAN:
                BooleanSchema booleanType = (BooleanSchema) d;
                parseBoolean(booleanType);
                break;
        }

    }

    private void parseInt(IntegerSchema integerType) {
        if (integerType.getDefaultValue() != null) {
            integerType.setMinValue(null);
            integerType.setMaxValue(null);
            integerType.setDistribution(null);
        }
        dataTypeSchema = integerType;
    }

    private void parseString(StringSchema stringType) {
        if (stringType.getDefaultValue() != null) {
            stringType.setMinLength(null);
            stringType.setMaxLength(null);
            stringType.setDistribution(null);
            stringType.setRegexPattern(null);
        } else if (stringType.getRegexPattern() != null) {
            stringType.setMinLength(null);
            stringType.setMaxLength(null);
            stringType.setDistribution(null);
        }

        dataTypeSchema = stringType;
    }

    private void parseFloat(FloatSchema floatType) {
        if (floatType.getDefaultValue() != null) {
            floatType.setMinValue(null);
            floatType.setMaxValue(null);
            floatType.setDistribution(null);
        }

        dataTypeSchema = floatType;
    }

    private void parseBoolean(BooleanSchema booleanType) {
        dataTypeSchema = booleanType;
    }
}
