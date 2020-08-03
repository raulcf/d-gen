package dgen.dataset;

import dgen.tables.Table;

import java.util.List;

public class Dataset {

    private String attributeName;
    private List<Table> tables;

    public Dataset(String attributeName, List<Table> tables) {
        this.attributeName = attributeName;
        this.tables = tables;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Dataset name: ");
        sb.append(attributeName).append("\n\n");

        for (int i = 0; i < tables.size(); i++) {
            Table table = tables.get(i);
            sb.append("Table ").append(i).append(":\n");
            sb.append(table.toCSV());
        }

        return sb.toString();
    }

    public String getAttributeName() {
        return attributeName;
    }

    public List<Table> getTables() {
        return tables;
    }
}
