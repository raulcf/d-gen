package dgen.tables;

import dgen.column.Column;
import dgen.datatypes.DataType;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Table {

    private List<Column> columns;

    public Table(List<Column> columns) {
        this.columns = columns;
    }

    // FIXME: serialization functions, such as toCSV, should not be part of Table. Table should just provide
    // methods to deliver data, then, different serdes will use that data differently.
    public String toCSV() {
        StringBuffer sb = new StringBuffer();

        // header
        List<String> attributeNames = new ArrayList<>();
        List<Iterator<DataType>> columnIterators = new ArrayList<>();
        for (Column c : columns) {
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
}
