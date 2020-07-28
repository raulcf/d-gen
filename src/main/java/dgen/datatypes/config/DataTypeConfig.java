package dgen.datatypes.config;

import dgen.datatypes.NativeType;
import dgen.datatypes.generators.*;
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

    static DataTypeConfig specToConfig(DataTypeSpec dataTypeSpec) {
        switch (dataTypeSpec.type()) {
            case INT:
                return IntegerTypeConfig.specToConfig((IntegerSpec) dataTypeSpec);
            case FLOAT:
                return FloatTypeConfig.specToConfig((FloatSpec) dataTypeSpec);
            case STRING:
                return StringTypeConfig.specToConfig((StringSpec) dataTypeSpec);
            case BOOLEAN:
                return BooleanTypeConfig.specToConfig((BooleanSpec) dataTypeSpec);
        }

        return null;
    }

    static DataTypeGenerator configToGenerator(DataTypeConfig dataTypeConfig) {
        switch (dataTypeConfig.nativeType()) {
            case INTEGER:
                return new IntegerTypeGenerator((IntegerTypeConfig) dataTypeConfig);
            case FLOAT:
                return new FloatTypeGenerator((FloatTypeConfig) dataTypeConfig);
            case STRING:
                return new StringTypeGenerator((StringTypeConfig) dataTypeConfig);
            case BOOLEAN:
                return new BooleanTypeGenerator((BooleanTypeConfig) dataTypeConfig);
        }

        return null;
    }

    NativeType nativeType();
}
