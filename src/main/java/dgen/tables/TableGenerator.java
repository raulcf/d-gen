package dgen.generators;

import java.util.List;

/**
 * A table generator is a collection of column generators
 */
public class TableGenerator {

    private List<ColumnGenerator> columnGeneratorList;

    public TableGenerator(List<ColumnGenerator> columnGeneratorList){
        this.columnGeneratorList = columnGeneratorList;
    }

}
