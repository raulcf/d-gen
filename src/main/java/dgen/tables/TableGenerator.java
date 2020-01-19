package dgen.tables;

import dgen.column.Column;
import dgen.column.ColumnGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * A table generator is a collection of column generators
 */
public class TableGenerator {

    private int numRecords;

    private List<ColumnGenerator> columnGeneratorList;

    public TableGenerator(List<ColumnGenerator> columnGeneratorList, int numRecords){
        this.numRecords = numRecords;
        this.columnGeneratorList = columnGeneratorList;
    }

    public Table generateTable() {

        List<Column> columns = new ArrayList<>();
        for (ColumnGenerator cg : columnGeneratorList) {
            Column c = cg.generateColumn(this.numRecords);
            columns.add(c);
        }

        Table t = new Table(columns);
        return t;
    }

}
