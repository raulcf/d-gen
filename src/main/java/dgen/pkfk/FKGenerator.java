package dgen.pkfk;

import dgen.coreconfig.DGException;
import dgen.datatypes.DataType;
import dgen.datatypes.NativeType;
import dgen.datatypes.generators.DataTypeGenerator;
import dgen.utils.RandomGenerator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FKGenerator implements DataTypeGenerator {

    private NativeType nativeType;
    private DataTypeGenerator pkDataTypeGenerator;
    private int numRecords;
    private RandomGenerator rnd;
    private List<DataType> pkValues = new ArrayList<>();

    /**
     * Checks whether a pair of datatype generators satisfies a PK-FK relationship.
     * @param pkValues Datatype values from the primary key datatype generator.
     * @param fkDtg Datatype generator to act as the foreign key datatype generator.
     * @param fkNumRecords Number of rows to generate from fkDtg.
     * @param fkUnique Whether fkDtg is producing unique or non-unique values.
     */
    public static void validate(List<DataType> pkValues, DataTypeGenerator fkDtg, int fkNumRecords,
                                boolean fkUnique) {

        Set<Object> fkValues = new HashSet<>();
        for (int i = 0; i < fkNumRecords; i++) {
            if (fkUnique) {
                fkValues.add(fkDtg.drawWithoutReplacement().value());
            } else {
                fkValues.add(fkDtg.drawWithReplacement().value());
            }
        }

        if (!pkValues.containsAll(fkValues)) {
            throw new DGException("FKGenerator not compatible with PKGenerator");
        }
    }

    /**
     * Data type generator that generates values based on an existing data type generator from a primary key.
     * @param pkDataTypeGenerator Existing data type generator from primary key.
     * @param numRecords Number of records generated from primary key.
     */
    public FKGenerator(DataTypeGenerator pkDataTypeGenerator, int numRecords, RandomGenerator rnd) {
        this.pkDataTypeGenerator = pkDataTypeGenerator;
        this.numRecords = numRecords;
        this.rnd = rnd;
        this.nativeType = pkDataTypeGenerator.getNativeType();

        for (int i = 0; i < numRecords; i++) {
            pkValues.add(pkDataTypeGenerator.drawWithoutReplacement());
        }
    }

    public FKGenerator(List<DataType> pkValues, RandomGenerator rnd) {
        this.pkValues = pkValues;
        this.rnd = rnd;
    }

    @Override
    public NativeType getNativeType() { return nativeType; }

    @Override
    public FKGenerator copy() {
        return new FKGenerator(pkValues, new RandomGenerator(rnd.getSeed()));
    }

    public DataType drawWithReplacement() {
        return pkValues.get(rnd.nextInt(pkValues.size()));
    }

    public DataType drawWithoutReplacement() {
        if (pkValues.isEmpty()) {
            throw new DGException("No more primary key values remaining");
        } else {
            return pkValues.remove(rnd.nextInt(pkValues.size()));
        }
    }

    public DataTypeGenerator getPkDataTypeGenerator() {
        return pkDataTypeGenerator;
    }

    public void setPkDataTypeGenerator(DataTypeGenerator pkDataTypeGenerator) {
        this.pkDataTypeGenerator = pkDataTypeGenerator;
    }

    public int getNumRecords() {
        return numRecords;
    }

    public void setNumRecords(int numRecords) {
        this.numRecords = numRecords;
    }

    public RandomGenerator getRnd() {
        return rnd;
    }

    public void setRnd(RandomGenerator rnd) {
        this.rnd = rnd;
    }

    public List<DataType> getPkValues() {
        return pkValues;
    }

    public void setPkValues(List<DataType> pkValues) {
        this.pkValues = pkValues;
    }
}

