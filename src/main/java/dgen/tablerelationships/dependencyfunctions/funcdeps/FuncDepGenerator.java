package dgen.tablerelationships.dependencyfunctions.funcdeps;

import dgen.column.ColumnGenerator;
import dgen.coreconfig.DGException;
import dgen.datatypes.DataType;
import dgen.datatypes.IntegerType;
import dgen.datatypes.NativeType;
import dgen.datatypes.generators.DataTypeGenerator;
import dgen.utils.RandomGenerator;

import java.util.*;
import java.util.stream.Collectors;

public class FuncDepGenerator implements DataTypeGenerator {

    private RandomGenerator rnd;
    private List<DataType> dependentValues = new ArrayList<>();

    /**
     * Checks whether data from two columns satisfies a functional dependency relationship.
     * @param determinantData Datatype values from determinant column.
     * @param dependant ColumnGenerator of dependant column.
     * @param funcDepConfig Config object of functional dependency to validate.
     * @param numRecords Number of records to generate from dependant.
     * @return Whether the data from dependant and determinantData satisfies a functional dependency relationship.
     */
    public static boolean validate(List<DataType> determinantData, ColumnGenerator dependant,
                                   FuncDepConfig funcDepConfig, int numRecords) {
        List<DataType> dependentValues = dependant.generateColumn(numRecords).getData();

        Map<Object, Object> valueMap = new HashMap<>();
        for (int i = 0; i < determinantData.size(); i++) {
            Object determinantValue = determinantData.get(i).value();
            Object dependentValue = dependentValues.get(i).value();

            if (valueMap.containsKey(determinantValue)) {
                if (!valueMap.get(determinantValue).equals(dependentValue)) {
                    return false;
                }
            } else {
                valueMap.put(determinantValue, dependentValue);
            }
        }


        return true;
    }

    public FuncDepGenerator(List<DataType> dependentValues) {
        this.dependentValues = dependentValues;
    }

    /**
     * Creates a datatype generator that has a functional dependency on another generator.
     * @param randomSeed Random seed to use when drawing values.
     * @param funcDepConfig Config object of functional dependency.
     * @param dependentDtg Datatype generator of dependent column (used to generate values that fit column constraints).
     * @param determinantData Data generated from determinant column.
     * @param numRecords Number of records to create in datatype generator.
     */
    public FuncDepGenerator(long randomSeed, FuncDepConfig funcDepConfig, DataTypeGenerator dependentDtg,
                            List<DataType> determinantData, int numRecords) {
        this.rnd = new RandomGenerator(randomSeed);

        HashMap determinantMap = new HashMap();
        for (int i = 0; i < numRecords; i++) {
            DataType d = determinantData.get(i);
            if (!determinantMap.containsKey(d.value())) {
                determinantMap.put(d.value(), dependentDtg.drawWithoutReplacement());
            }
            dependentValues.add((DataType) determinantMap.get(d.value()));
        }
    }

    @Override
    public NativeType getNativeType() { return dependentValues.get(0).nativeType(); }

    @Override
    public DataTypeGenerator copy() {
        return new FuncDepGenerator(new ArrayList<>(dependentValues));
    }

    @Override
    public DataType drawWithReplacement() {
        return drawWithoutReplacement();
    }

    @Override
    public DataType drawWithoutReplacement() {

        if (dependentValues.isEmpty()) {
            throw new DGException("No more values");
        } else {
            return dependentValues.remove(0);
        }
    }

    public List<DataType> getDependentValues() {
        return dependentValues;
    }

    public void setDependentValues(List<DataType> dependentValues) {
        this.dependentValues = dependentValues;
    }
}
