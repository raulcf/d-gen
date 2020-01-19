package dgen.column;

import dgen.datatypes.DataType;
import dgen.datatypes.generators.DataTypeGenerator;
import dgen.datatypes.generators.NullValueGenerator;
import dgen.distributions.UniformDistribution;

import java.util.ArrayList;
import java.util.List;

/**
 * A column generator is a combination of a datatype generator and a null value generator (TODO: and others)
 */
public class ColumnGenerator {

    private DataTypeGenerator dtg;
    private NullValueGenerator cng;
    private AttributeNameGenerator ang;

    public ColumnGenerator(DataTypeGenerator dtg, AttributeNameGenerator ang) {
        this.dtg = dtg;
        this.ang = ang;
    }

    public Column generateColumn(int numRecords) {
        List<DataType> values = new ArrayList<>();
        for(int i = 0; i < numRecords; i++) {
            DataType dt = this.dtg.drawWithReplacement(new UniformDistribution());
            values.add(dt);
        }

        String attributeName = ang.generateAttributeName();
        Column c = new Column(attributeName, values);

        return c;
    }

}
