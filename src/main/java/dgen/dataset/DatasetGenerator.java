package dgen.dataset;

import dgen.attributegenerators.AttributeNameGenerator;
import dgen.column.ColumnGenerator;
import dgen.tables.Table;
import dgen.tables.TableConfig;
import dgen.tables.TableGenerator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A DatasetGenerator is a collection of table generators
 */
public class DatasetGenerator {

    private String attributeName;
    private List<TableGenerator> tableGeneratorList;

    public DatasetGenerator(String attributeName, List<TableGenerator> tableGeneratorList) {
        this.attributeName = attributeName;
        this.tableGeneratorList = tableGeneratorList;
    }

    public DatasetGenerator(DatasetConfig datasetConfig) {
        this.attributeName = datasetConfig.getString("dataset.name");

        List<TableGenerator> tableGenerators = new ArrayList<>();
        for (TableConfig tableConfig: (List<TableConfig>) datasetConfig.getObject("table.configs")) {
            tableGenerators.add(new TableGenerator(tableConfig));
        }

        this.tableGeneratorList = tableGenerators;
    }

    public Dataset generateDataset() {

        List<Table> tables = new ArrayList<>();
        for (TableGenerator tg: tableGeneratorList) {
            Table table = tg.generateTable();
            tables.add(table);
        }

        return new Dataset(attributeName, tables);
    }

}
