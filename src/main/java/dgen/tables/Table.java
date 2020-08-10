package dgen.tables;

import dgen.column.Column;
import dgen.datatypes.DataType;

import java.util.*;

public class Table {

    private Map<Integer, Column> columns;
    private String attributeName;
    private int tableID;

    public Table(int tableID, String attributeName, Map<Integer, Column> columns) {
        this.tableID = tableID;
        this.attributeName = attributeName;
        this.columns = columns;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();

        // header
        List<String> attributeNames = new ArrayList<>();
        List<Iterator<DataType>> columnIterators = new ArrayList<>();
        for (Column c : columns.values()) {
            // attribute data for header
            String attrName = c.getAttributeName();
            attributeNames.add(attrName);

            // the rest of the records
            Iterator<DataType> it = c.getData().iterator();
            columnIterators.add(it);
        }

        String header = String.join(",", attributeNames);
        sb.append(header + '\n');

        // record
        boolean moreData = true;
        while(moreData) {
            List<String> recordValues = new ArrayList<>();
            for (Iterator<DataType> it : columnIterators) {
                if (! it.hasNext()) {
                    moreData = false;
                    continue;
                }
                DataType dt = it.next();
                String value = dt.value().toString();
                recordValues.add(value);
            }
            String record = String.join(",", recordValues);
            sb.append(record + '\n');
        }

        return sb.toString();
    }


    public int getTableID() {
        return tableID;
    }

    public List<Column> getColumns() {
        return new ArrayList<>(columns.values());
    }

    public Column getColumn(int columnID) { return columns.get(columnID); }

    public String getAttributeName() {
        return attributeName;
    }
}
