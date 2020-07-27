package dgen.pkfk;

import dgen.coreconfig.DGException;
import dgen.datatypes.DataType;
import dgen.datatypes.NativeType;
import dgen.datatypes.generators.DataTypeGenerator;
import dgen.utils.RandomGenerator;

import java.util.*;

public class FKGenerator implements DataTypeGenerator {

    private DataTypeGenerator pkDataTypeGenerator;
    private int numRecords;
    private RandomGenerator rnd;
    private List<DataType> pkValues = new ArrayList<>();

    /**
     * Data type generator that generates values based on an existing data type generators from a primary key.
     * @param pkDataTypeGenerator Existing data type generator from primary key.
     * @param numRecords Number of records generated from primary key.
     */
    public FKGenerator(DataTypeGenerator pkDataTypeGenerator, int numRecords, RandomGenerator rnd) {
        this.pkDataTypeGenerator = pkDataTypeGenerator;
        this.numRecords = numRecords;
        this.rnd = rnd;

        for (int i = 0; i < numRecords; i++) { // Repetitive when a pk has a lot of fks
            pkValues.add(pkDataTypeGenerator.drawWithoutReplacement());
        }
    }

    public static void validate(DataTypeGenerator pkDtg, DataTypeGenerator fkDtg, int pkNumRecords, int fkNumRecords,
                                boolean fkUnique) {
        Set<Object> pkValues = new HashSet<>();
        for (int i = 0; i < pkNumRecords; i++) {
            pkValues.add(pkDtg.drawWithoutReplacement().value());
        }

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

    @Override
    public NativeType getNativeType() { return pkDataTypeGenerator.getNativeType(); }

    @Override
    public FKGenerator copy() {
        return null; // TODO: Perhaps implement this later
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

    public List<DataType> getPkValues() {
        return pkValues;
    }
}

