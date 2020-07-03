package dgen.utils;

import dgen.utils.schemas.datatypes.DataType;
import dgen.utils.schemas.datatypes.Int;

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
        }

    }

    public void parseInt(Int intType) {
        if (intType.getDefaultValue() != null) {
            intType.setMinValue(null);
            intType.setMaxValue(null);
            intType.setDistribution(null);
        } else {
            assert intType.getMinValue() <= intType.getMaxValue();
        }

        dataType = intType;
    }


}
