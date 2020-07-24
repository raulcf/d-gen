package dgen.tables;

import dgen.attributegenerators.AttributeNameGenerator;
import dgen.attributegenerators.DefaultAttributeNameGenerator;
import dgen.attributegenerators.RandomAttributeNameGenerator;
import dgen.attributegenerators.RegexAttributeNameGenerator;
import dgen.column.Column;
import dgen.column.ColumnConfig;
import dgen.column.ColumnGenerator;
import dgen.utils.RandomGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A table generator is a collection of column generators
 */
public class TableGenerator {

    private AttributeNameGenerator ang;
    private Map<Integer, ColumnGenerator> columnGeneratorMap;

    private int tableID;
    private int numRecords;
    private RandomGenerator rnd;

    public TableGenerator(AttributeNameGenerator ang, Map<Integer, ColumnGenerator> columnGeneratorMap, int numRecords){
        this.ang = ang;
        this.numRecords = numRecords;
        this.columnGeneratorMap = columnGeneratorMap;
    }

    public TableGenerator(TableConfig tableConfig) {
        tableID = tableConfig.getInt("table.id");
        numRecords = tableConfig.getInt("num.rows");
        rnd = new RandomGenerator(tableConfig.getLong("random.seed"));

        if (tableConfig.getString("table.name") != null) {
            this.ang = new DefaultAttributeNameGenerator(tableConfig.getString("column.name"));
        } else if (tableConfig.getString("regex.name") != null) {
            this.ang = new RegexAttributeNameGenerator(rnd, tableConfig.getString("regex.name"));
        } else if (tableConfig.getBoolean("random.name")) {
            this.ang = new RandomAttributeNameGenerator(rnd);
        }

        Map<Integer, ColumnGenerator> columnGenerators = new HashMap<>();
        for (ColumnConfig columnConfig: (List<ColumnConfig>) tableConfig.getObject("column.configs")) {
            ColumnGenerator columnGenerator = new ColumnGenerator(columnConfig);
            columnGenerators.put(columnGenerator.getColumnID(), columnGenerator);
        }

        this.columnGeneratorMap = columnGenerators;
    }

    // TODO: this should be an iterator that provides either columns or rows, depending on the storage orientation

    public Table generateTable() {

        List<Column> columns = new ArrayList<>();
        for (ColumnGenerator cg : columnGeneratorMap.values()) {
            Column c = cg.generateColumn(this.numRecords);
            columns.add(c);
        }

        String attributeName = ang.generateAttributeName();
        Table t = new Table(attributeName, columns);
        return t;
    }

    public ColumnGenerator getColumnGenerator(int columnID) {
        return columnGeneratorMap.get(columnID);
    }

    public void setColumnGenerator(int columnID, ColumnGenerator columnGenerator) {
        this.columnGeneratorMap.replace(columnID, columnGenerator);
    }

    public int getTableID() {
        return tableID;
    }

    public int getNumRecords() {
        return numRecords;
    }

    public AttributeNameGenerator getAng() {
        return ang;
    }

    public void setAng(AttributeNameGenerator ang) {
        this.ang = ang;
    }

    public Map<Integer, ColumnGenerator> getColumnGeneratorMap() {
        return columnGeneratorMap;
    }

    public void setColumnGeneratorMap(Map<Integer, ColumnGenerator> columnGeneratorMap) {
        this.columnGeneratorMap = columnGeneratorMap;
    }

    @Override
    public String toString() {
        return "TableGenerator{" +
                "tableID=" + tableID +
                ", numRecords=" + numRecords +
                ", ang=" + ang +
                ", columnGeneratorMap=" + columnGeneratorMap +
                '}';
    }
}
