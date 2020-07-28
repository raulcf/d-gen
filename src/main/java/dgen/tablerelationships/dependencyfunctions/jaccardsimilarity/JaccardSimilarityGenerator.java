package dgen.tablerelationships.dependencyfunctions.jaccardsimilarity;

import dgen.column.Column;
import dgen.column.ColumnGenerator;
import dgen.coreconfig.DGException;
import dgen.datatypes.DataType;
import dgen.datatypes.IntegerType;
import dgen.datatypes.NativeType;
import dgen.datatypes.generators.DataTypeGenerator;
import dgen.datatypes.generators.IntegerTypeGenerator;
import dgen.utils.RandomGenerator;

import java.util.*;

public class JaccardSimilarityGenerator implements DataTypeGenerator {
    private final Random rnd;
    private final List<DataType> dependentValues = new ArrayList<>();

    /**
     * Checks whether data from two columns satisfies a jaccard similarity relationship.
     * @param determinantData Datatype values from determinant column.
     * @param dependant ColumnGenerator of dependant column.
     * @param jaccardSimilarityConfig Config object of jaccard similarity to validate.
     * @param numRecords Number of records to generate from dependant.
     * @return Whether the data from dependant and determinantData satisfies a jaccard similarity relationship.
     */
    public static boolean validate(List<DataType> determinantData, ColumnGenerator dependant,
                                   JaccardSimilarityConfig jaccardSimilarityConfig, int numRecords) {
        Column dependentColumn = dependant.generateColumn(numRecords);
        Float similarity = jaccardSimilarityConfig.getFloat("similarity");
        Set<Object> determinantValues = new HashSet<>(determinantData);

        Set<Object> dependentValues = new HashSet<>();
        for (DataType d: dependentColumn.getData()) {
            dependentValues.add(d.value());
        }

        Set<Object> intersection = new HashSet<>(determinantValues);
        intersection.retainAll(dependentValues);

        return !(intersection.size() < unionSize(similarity, numRecords));
    }

    private static int unionSize(float similarity, int numRecords) { return (int) (2 * numRecords * similarity / (1 + similarity)); }

    /**
     * Creates a data generator for the dependent of a jaccard similarity based on the determinant's data type generator.
     * @param randomSeed Random seed to use.
     * @param jaccardSimilarityConfig Config object of jaccard similarity.
     * @param dependentDtg Datatype generator of dependent column.
     * @param determinantData Datatypes generated from determinant column.
     * @param numRecords Number of records in generator.
     * @param dependantUnique Whether the dependant column has unique values.
     */
    public JaccardSimilarityGenerator(long randomSeed, JaccardSimilarityConfig jaccardSimilarityConfig, DataTypeGenerator dependentDtg,
                                      List<DataType> determinantData, int numRecords, boolean dependantUnique) {
        this.rnd = new RandomGenerator(randomSeed);

        Set<DataType> determinantValues = new HashSet<>(determinantData);
        float similarity = jaccardSimilarityConfig.getFloat("similarity");

        if (determinantValues.size() < (int)(similarity * numRecords)) {
            throw new DGException("Cannot create Jaccard similarity");
        }

        List<DataType> determinantValuesList = new ArrayList<>(determinantValues);
        Set<DataType> dependentValuesSet = new HashSet<>();
        for (int i = 0; i < unionSize(similarity, numRecords); i++) {
            dependentValuesSet.add(determinantValuesList.remove(rnd.nextInt(determinantValuesList.size())));
        }

        for (int i = 0; i < numRecords - unionSize(similarity, numRecords); i++) {
            if (dependantUnique) {
                DataType value = dependentDtg.drawWithoutReplacement();

                while (dependentValuesSet.contains(value)) {
                    value = dependentDtg.drawWithoutReplacement();
                }

                dependentValuesSet.add(dependentDtg.drawWithoutReplacement());
            } else {
                dependentValuesSet.add(dependentDtg.drawWithReplacement());
            }
        }

        dependentValues.addAll(dependentValuesSet);
    }

    @Override
    public NativeType getNativeType() { return dependentValues.get(0).nativeType(); }

    @Override
    public IntegerTypeGenerator copy() { return null; }

    @Override
    public DataType drawWithReplacement() {
        return dependentValues.get(rnd.nextInt(dependentValues.size()));
    }

    @Override
    public DataType drawWithoutReplacement() {
        if (dependentValues.isEmpty()) {
            throw new DGException("No more values");
        } else {
            return dependentValues.remove(rnd.nextInt(dependentValues.size()));
        }
    }

}
