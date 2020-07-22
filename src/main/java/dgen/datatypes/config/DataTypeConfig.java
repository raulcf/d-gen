package dgen.datatypes.config;

import dgen.datatypes.NativeType;
import dgen.datatypes.generators.DataTypeGenerator;
import dgen.utils.specs.datatypespecs.*;

public interface DataTypeConfig {

    static DataTypeGenerator specToGenerator(DataTypeSpec dataTypeSpec) {
        switch (dataTypeSpec.type()) {
            case INT:
                return IntegerTypeConfig.specToGenerator((IntegerSpec) dataTypeSpec);
            case FLOAT:
                return FloatTypeConfig.specToGenerator((FloatSpec) dataTypeSpec);
            case STRING:
                return StringTypeConfig.specToGenerator((StringSpec) dataTypeSpec);
            case BOOLEAN:
                return BooleanTypeConfig.specToGenerator((BooleanSpec) dataTypeSpec);
        }

        return null;
    }

    NativeType nativeType();
}
