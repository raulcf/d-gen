package dgen.tablerelationships.dependencyfunctions.funcdeps;

import dgen.column.ColumnGenerator;
import dgen.coreconfig.DGException;
import dgen.datatypes.DataType;
import dgen.datatypes.IntegerType;
import dgen.datatypes.NativeType;
import dgen.datatypes.generators.DataTypeGenerator;
import dgen.utils.RandomGenerator;

import javax.xml.crypto.Data;
import java.util.*;

public class FuncDepGenerator implements DataTypeGenerator {

    private final Random rnd;
    private final List<DataType> dependentValues = new ArrayList<>();

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

    public FuncDepGenerator(long randomSeed, FuncDepConfig funcDepConfig, DataTypeGenerator dependentDtg,
                            List<DataType> determinantData, int numRecords, boolean dependantUnique) {
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
        return null;
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

    public static void main(String[] args) {
        Map<DataType, Integer> x = new HashMap<>();
        x.put(new IntegerType(2), 2);

        System.out.println(x.containsKey(new IntegerType(2)));
    }
}
