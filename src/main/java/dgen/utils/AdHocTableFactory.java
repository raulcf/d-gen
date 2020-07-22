package dgen.utils;

import dgen.column.ColumnGenerator;
import dgen.attributegenerators.RandomAttributeNameGenerator;
import dgen.datatypes.generators.IntegerTypeGenerator;
import dgen.tables.Table;
import dgen.tables.TableGenerator;

import java.util.ArrayList;
import java.util.List;

public class AdHocTableFactory {

    public static Table makeAllIntegerTable(int numColumns, int  numRecords) {

        // 1. Configure generators
        IntegerTypeGenerator itg = IntegerTypeGenerator.makeDefault();

        List<ColumnGenerator> cgens = new ArrayList<>();
        for (int i = 0; i < numColumns; i++) {
            ColumnGenerator cg = new ColumnGenerator(itg, new RandomAttributeNameGenerator(6));
            cgens.add(cg);
        }

        TableGenerator tg = new TableGenerator(cgens, numRecords);

        // 2. Materialize data
        Table toReturn = tg.generateTable();

        return toReturn;
    }

}
