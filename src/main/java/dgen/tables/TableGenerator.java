package dgen.tables;

import dgen.attributegenerators.AttributeNameGenerator;
import dgen.attributegenerators.DefaultAttributeNameGenerator;
import dgen.attributegenerators.RandomAttributeNameGenerator;
import dgen.attributegenerators.RegexAttributeNameGenerator;
import dgen.column.Column;
import dgen.column.ColumnConfig;
import dgen.column.ColumnGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * A table generator is a collection of column generators
 */
public class TableGenerator {

    private int tableID;
    private int numRecords;

    private AttributeNameGenerator ang;
    private List<ColumnGenerator> columnGeneratorList;

    public TableGenerator(List<ColumnGenerator> columnGeneratorList, int numRecords){
        this.numRecords = numRecords;
        this.columnGeneratorList = columnGeneratorList;
    }

    public TableGenerator(TableConfig tableConfig) {
        tableID = tableConfig.getInt("table.id");
        numRecords = tableConfig.getInt("num.rows");

        if (tableConfig.getString("table.name") != null) {
            this.ang = new DefaultAttributeNameGenerator(tableConfig.getString("column.name"));
        } else if (tableConfig.getString("regex.name") != null) {
            this.ang = new RegexAttributeNameGenerator(tableConfig.getString("regex.name"));
        } else if (tableConfig.getBoolean("random.name")) {
            this.ang = new RandomAttributeNameGenerator();
        }

        List<ColumnGenerator> columnGenerators = new ArrayList<>();
        for (ColumnConfig columnConfig: (List<ColumnConfig>) tableConfig.getObject("column.configs")) {
            columnGenerators.add(new ColumnGenerator(columnConfig));
        }

        this.columnGeneratorList = columnGenerators;
    }

    // TODO: this should be an iterator that provides either columns or rows, depending on the storage orientation

    public Table generateTable() {

        List<Column> columns = new ArrayList<>();
        for (ColumnGenerator cg : columnGeneratorList) {
            Column c = cg.generateColumn(this.numRecords);
            columns.add(c);
        }

        String attributeName = ang.generateAttributeName();
        Table t = new Table(attributeName, columns);
        return t;
    }

}
