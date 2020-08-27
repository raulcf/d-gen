package dgen.dataset;

import dgen.tables.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Deprecated
public class Dataset {

    private DatasetConfig datasetConfig;
    private String attributeName;
    private Map<Integer, Table> tables;


    public Dataset(DatasetConfig datasetConfig, String attributeName, Map<Integer, Table> tables) {
        this.datasetConfig = datasetConfig;
        this.attributeName = attributeName;
        this.tables = tables;
    }

    public String getAttributeName() { return attributeName; }

    public List<Table> getTables() {
        return new ArrayList<>(tables.values());
    }

    public Table getTable(int tableID) { return tables.get(tableID); }

    public String getTableName(int tableID) { return tables.get(tableID).getAttributeName(); }

    public String getColumnName(int tableID, int columnID) { return tables.get(tableID).getColumnNames().get(columnID); }

    public DatasetConfig getDatasetConfig() {
        return datasetConfig;
    }
}
