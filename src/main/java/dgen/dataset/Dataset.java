package dgen.dataset;

import dgen.column.Column;
import dgen.tables.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Dataset {

    private DatasetConfig datasetConfig;
    private String attributeName;
    private Map<Integer, Table> tables;

    public Dataset(DatasetConfig datasetConfig, String attributeName, Map<Integer, Table> tables) {
        this.datasetConfig = datasetConfig;
        this.attributeName = attributeName;
        this.tables = tables;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Dataset name: ");
        sb.append(attributeName).append("\n\n");

        for (Table table: tables.values()) {
            sb.append("Table ").append(table.getAttributeName()).append("\n");
            sb.append(table.toString());
        }

        return sb.toString();
    }

    public String getAttributeName() {
        return attributeName;
    }

    public List<Table> getTables() {
        return new ArrayList<>(tables.values());
    }

    public Table getTable(int tableID) { return tables.get(tableID); }

    public Column getColumn(int tableID, int columnID) { return getTable(tableID).getColumn(columnID); }

    public DatasetConfig getDatasetConfig() {
        return datasetConfig;
    }
}
