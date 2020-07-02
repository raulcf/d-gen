package dgen.utils;

import dgen.utils.schemas.datatypes.DataType;
import dgen.utils.schemas.datatypes.Distributions;
import dgen.utils.schemas.datatypes.Int;

import java.util.HashMap;
import java.util.Map;

public class DataTypeParser {
    DataType dataType;

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public void parse(Object d) {
        if (d instanceof String) {
            ;
        } else if (d instanceof Map<?, ?>) {
            Map<String, Object> dParams = (HashMap<String, Object>) d;
            String typeName = dParams.keySet().iterator().next(); // Ugly

            switch (typeName) {
                case "int":
                    parseInt(dParams.get("int"));
                    break;
                case "float":
                    ;
                    break;
                case "string":
                    ;
                    break;
            }

        }

    }

    public void parseInt(Object dParams) {
        Int intType = new Int();
        Map<String, Object> intParams = new HashMap<>();

        if ((dParams instanceof String) && "default".equalsIgnoreCase((String) dParams)) {
            dataType = intType;
            return;
        } else if (dParams instanceof Map<?, ?>) {
            intParams = (HashMap<String, Object>) dParams;
        } else {
            // Throw error
        }

        if (intParams.containsKey("defaultValue")) {
            int defaultValue = (int) intParams.get("defaultValue");
            intType.setDefaultValue(defaultValue);
            intType.setMinValue(null);
            intType.setMaxValue(null);
            intType.setDistribution(null);
        } else if (intParams.containsKey("minValue") || intParams.containsKey("maxValue")) {
            if (intParams.containsKey("minValue")) {
                int minValue = (int) intParams.get("minValue");
                intType.setMinValue(minValue);
            }
            if (intParams.containsKey("maxValue")) {
                int maxValue = (int) intParams.get("maxValue");
                intType.setMaxValue(maxValue);
            }

            assert intType.getMinValue() <= intType.getMaxValue();

        }

        if (intParams.containsKey("distribution")) {
            String distribution = (String) intParams.get("distribution");
            distribution = distribution.toUpperCase();

            try {
                intType.setDistribution(Enum.valueOf(Distributions.class, distribution));
            } catch (IllegalArgumentException e) {
                //TODO: Throw error
            }
        }

        dataType = intType;
    }


}
