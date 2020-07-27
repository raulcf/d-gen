package dgen.tablerelationships.dependencyfunctions.jaccardsimilarity;

import dgen.column.Column;
import dgen.column.ColumnGenerator;
import dgen.coreconfig.DGException;
import dgen.datatypes.DataType;
import dgen.datatypes.IntegerType;
import dgen.datatypes.NativeType;
import dgen.datatypes.config.DataTypeConfig;
import dgen.datatypes.generators.DataTypeGenerator;
import dgen.datatypes.generators.IntegerTypeGenerator;
import dgen.utils.RandomGenerator;

import java.util.*;

public class JaccardSimilarityGenerator implements DataTypeGenerator {
    private Random rnd;
    private float similarity;
    private DataTypeGenerator dependentDtg;
    private DataTypeGenerator determinantDtg;
    private int numRecords;
    private Set<DataType> determinantValues = new HashSet<>();
    private List<DataType> dependentValues = new ArrayList<>();


    public static boolean validate(ColumnGenerator determinant, ColumnGenerator dependant,
                                   JaccardSimilarityConfig jaccardSimilarityConfig, int numRecords) {
        Column determinantColumn = determinant.generateColumn(numRecords); // Inefficient to keep recalculating
        Column dependentColumn = dependant.generateColumn(numRecords);
        Float similarity = jaccardSimilarityConfig.getFloat("similarity");

        Set<Object> determinantValues = new HashSet<>();
        for (DataType d: determinantColumn.getData()) {
            determinantValues.add(d.value());
        }

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
     * @param rnd Random object.
     * @param similarity Float representing the percentage of values between determinant and dependant to be the same.
     * @param determinantDtg Data type generator of the determinant.
     * @param numRecords Number of records in generator.
     * @param unique Whether values from determinantDtg will be drawn with or without replacement.
     */
    public JaccardSimilarityGenerator(long randomSeed, JaccardSimilarityConfig jaccardSimilarityConfig, DataTypeGenerator dependentDtg,
                                      DataTypeGenerator determinantDtg, int numRecords, boolean determinantUnique,
                                      boolean dependantUnique) {
        this.rnd = new RandomGenerator(randomSeed);
        this.similarity = jaccardSimilarityConfig.getFloat("similarity");
        this.dependentDtg = dependentDtg;
        this.determinantDtg = determinantDtg;
        this.numRecords = numRecords;

        for (int i = 0; i < numRecords; i++) {
            if (determinantUnique) {
                determinantValues.add(determinantDtg.drawWithoutReplacement());
            } else {
                determinantValues.add(determinantDtg.drawWithReplacement());
            }
        }

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
                    value = determinantDtg.drawWithoutReplacement();
                }

                dependentValuesSet.add(dependentDtg.drawWithoutReplacement());
            } else {
                dependentValuesSet.add(dependentDtg.drawWithReplacement());
            }
        }

        dependentValues.addAll(dependentValuesSet);

    }

    @Override
    public NativeType getNativeType() { return dependentDtg.getNativeType(); }

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

    public static void main(String[] args) {
        IntegerType int1 = new IntegerType(1);
        IntegerType int2= new IntegerType(1);
        Set<IntegerType> set1 = new HashSet<>();
        set1.add(int1);
        Set<IntegerType> set2 = new HashSet<>();
        set2.add(int2);

        System.out.println(set1.equals(set2));
    }

}
