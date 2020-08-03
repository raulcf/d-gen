package dgen.dataset;

import dgen.column.ColumnGenerator;
import dgen.datatypes.DataType;
import dgen.datatypes.generators.DataTypeGenerator;
import dgen.pkfk.FKGenerator;
import dgen.tables.Table;
import dgen.tables.TableConfig;
import dgen.tables.TableGenerator;
import dgen.utils.RandomGenerator;
import org.javatuples.Pair;

import java.util.*;

/**
 * A DatasetGenerator is a collection of table generators
 */
public class DatasetGenerator {

    private String attributeName;
    private Map<Integer, TableGenerator> tableGeneratorMap;

    public DatasetGenerator(String attributeName, Map<Integer, TableGenerator> tableGeneratorMap) {
        this.attributeName = attributeName;
        this.tableGeneratorMap = tableGeneratorMap;
    }

    public DatasetGenerator(DatasetConfig datasetConfig) {
        this.attributeName = datasetConfig.getString("dataset.name");

        Map<Integer, TableGenerator> tableGenerators = new HashMap<>();
        for (TableConfig tableConfig : (List<TableConfig>) datasetConfig.getObject("table.configs")) {
            TableGenerator tableGenerator = new TableGenerator(tableConfig);
            tableGenerators.put(tableGenerator.getTableID(), tableGenerator);
        }
        this.tableGeneratorMap = tableGenerators;

        // Handling PK-FK
        Map<Pair<Integer, Integer>, Set<Pair<Integer, Integer>>> pkfkMappings = (Map<Pair<Integer, Integer>, Set<Pair<Integer, Integer>>>) datasetConfig.getObject("pk.fk.mappings");
        for (Pair<Integer, Integer> pk : pkfkMappings.keySet()) {
            int numRecords = getTableGenerator(pk.getValue0()).getNumRecords();
            ColumnGenerator pkColumnGenerator = getColumnGenerator(pk.getValue0(), pk.getValue1());
            List<DataType> pkValues = pkColumnGenerator.copy().generateColumn(numRecords).getData();

            for (Pair<Integer, Integer> fk : pkfkMappings.get(pk)) {
                if (getColumnGenerator(fk.getValue0(), fk.getValue1()).getDtg() != null) {
                    ColumnGenerator fkColumnGenerator = getColumnGenerator(fk.getValue0(), fk.getValue1());
                    int fkNumRecords = getTableGenerator(fk.getValue0()).getNumRecords();

                    FKGenerator.validate(pkValues, fkColumnGenerator.copy(), fkNumRecords);

                } else {
                    ColumnGenerator fkColumnGenerator = getColumnGenerator(fk.getValue0(), fk.getValue1());
                    // TODO: Foreign key data generators have no random seed so I'm using the column's random object. Should be fine but may want to change later
                    fkColumnGenerator.setDtg(new FKGenerator(pkValues, fkColumnGenerator.getRandomGenerator()));

                    setColumnGenerator(fk.getValue0(), fk.getValue1(), fkColumnGenerator);
                }
            }
        }

    }

    public Dataset generateDataset() {

        List<Table> tables = new ArrayList<>();
        for (TableGenerator tg: tableGeneratorMap.values()) {
            Table table = tg.generateTable();
            tables.add(table);
        }

        return new Dataset(attributeName, tables);
    }

    public TableGenerator getTableGenerator(int tableID) {
        return tableGeneratorMap.get(tableID);
    }

    public void setTableGenerator(int tableID, TableGenerator tableGenerator) {
        this.tableGeneratorMap.replace(tableID, tableGenerator);
    }

    public ColumnGenerator getColumnGenerator(int tableID, int columnID) {
        return getTableGenerator(tableID).getColumnGenerator(columnID);
    }

    public void setColumnGenerator(int tableID, int columnID, ColumnGenerator columnGenerator) {
        TableGenerator tableGenerator = getTableGenerator(tableID);
        tableGenerator.setColumnGenerator(columnID, columnGenerator);
        setTableGenerator(tableID, tableGenerator);
    }

}
