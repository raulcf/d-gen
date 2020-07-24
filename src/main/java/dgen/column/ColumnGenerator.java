package dgen.column;

import dgen.attributegenerators.AttributeNameGenerator;
import dgen.attributegenerators.DefaultAttributeNameGenerator;
import dgen.attributegenerators.RandomAttributeNameGenerator;
import dgen.attributegenerators.RegexAttributeNameGenerator;
import dgen.coreconfig.DGException;
import dgen.datatypes.DataType;
import dgen.datatypes.config.DataTypeConfig;
import dgen.datatypes.generators.DataTypeGenerator;
import dgen.datatypes.generators.NullValueGenerator;
import dgen.distributions.UniformDistribution;
import dgen.utils.RandomGenerator;
import dgen.utils.specs.datatypespecs.DataTypeSpec;

import java.util.ArrayList;
import java.util.List;

/**
 * A column generator is a combination of a datatype generator and a null value generator (TODO: and others)
 */
public class ColumnGenerator {

    private DataTypeGenerator dtg;
    private NullValueGenerator cng;
    private AttributeNameGenerator ang;

    private int columnID;
    private RandomGenerator rnd;
    private boolean unique = false;

    public ColumnGenerator(DataTypeGenerator dtg, AttributeNameGenerator ang) {
        this.dtg = dtg;
        this.ang = ang;
    }

    public ColumnGenerator(ColumnConfig columnConfig) {
        columnID = columnConfig.getInt("column.id");
        rnd = new RandomGenerator(columnConfig.getLong("random.seed"));

        if (columnConfig.getString("column.name") != null) {
            this.ang = new DefaultAttributeNameGenerator(columnConfig.getString("column.name"));
        } else if (columnConfig.getString("regex.name") != null) {
            this.ang = new RegexAttributeNameGenerator(rnd, columnConfig.getString("regex.name"));
        } else if (columnConfig.getBoolean("random.name")) {
            this.ang = new RandomAttributeNameGenerator(rnd);
        }

        if (columnConfig.getObject("datatype") == null) {
            this.dtg = null;
        } else {
            this.dtg = DataTypeConfig.specToGenerator((DataTypeSpec)columnConfig.getObject("datatype"));
        }

        //TODO: Add logic for nulls
        this.unique = columnConfig.getBoolean("unique");
    }

    public Column generateColumn(int numRecords) {
        if (dtg == null) {
            throw new DGException("Missing data generator");
        }

        List<DataType> values = new ArrayList<>();
        for(int i = 0; i < numRecords; i++) {
            DataType dt;

            if (unique) {
                dt = this.dtg.drawWithoutReplacement();
            } else {
                dt = this.dtg.drawWithReplacement();
            }
            values.add(dt);
        }

        String attributeName = ang.generateAttributeName();
        Column c = new Column(attributeName, values);

        return c;
    }

    public int getColumnID() {
        return columnID;
    }

    public boolean isUnique() {
        return unique;
    }

    public RandomGenerator getRandomGenerator() { return rnd; }

    public DataTypeGenerator getDtg() {
        return dtg;
    }

    public void setDtg(DataTypeGenerator dtg) {
        this.dtg = dtg;
    }

    @Override
    public String toString() {
        return "ColumnGenerator{" +
                "dtg=" + dtg +
                ", cng=" + cng +
                ", ang=" + ang +
                ", columnID=" + columnID +
                ", unique=" + unique +
                '}';
    }
}
