package dgen.utils.serialization;

import dgen.column.Column;
import dgen.dataset.Dataset;
import dgen.dataset.DatasetConfig;
import dgen.dataset.DatasetGenerator;
import dgen.datatypes.DataType;
import dgen.tables.Table;
import dgen.utils.parsers.SpecificationParser;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CSVSerializer {

    Dataset dataset;
    String delimiter;

    public CSVSerializer(Dataset dataset) {
        this.dataset = dataset;
        this.delimiter = ",";
    }

    public CSVSerializer(Dataset dataset, String delimiter) {
        this.dataset = dataset;
        this.delimiter = delimiter;
    }

    /**
     * Writes a Dataset object to CSV files. Each table within the dataset will
     * be a .csv file and all .csv files will be in a folder named after the database.
     * @param parentDir Parent directory to place Dataset directory in.
     */
    public void serialize(String parentDir) throws Exception {
        FileWriter csvWriter;

        String datasetDirPath = parentDir + "/" + dataset.getAttributeName();
        File datasetDir = new File(datasetDirPath);

        if (datasetDir.exists()) {
            throw new Exception("Directory for " + datasetDirPath + " already exists");
        }
        datasetDir.mkdir();

        for (Table table: dataset.getTables()) {
            File csvFile = new File(datasetDirPath + "/" + table.getAttributeName() + ".csv");

            if (csvFile.exists()) {
                throw new Exception("File " + csvFile + " already exists");
            }

            csvWriter = new FileWriter(csvFile);
            csvWriter.write(tableToCSV(table));

            csvWriter.flush();
            csvWriter.close();
        }
    }

    public String tableToCSV(Table table) {
        StringBuffer sb = new StringBuffer();
        List<Column> columns = table.getColumns();

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

        String header = String.join(delimiter, attributeNames);
        sb.append(header).append('\n');

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
            sb.append(record).append('\n');
        }

        return sb.toString();
    }

    public static void main(String[] args) throws Exception {
        SpecificationParser specificationParser = new SpecificationParser();

        specificationParser.parseYAML("test.yaml");
        specificationParser.write("test_output.json");
        DatasetGenerator datasetGenerator = DatasetConfig.specToGenerator(specificationParser.getDatabase());

//        CSVSerializer csvSerializer = new CSVSerializer(datasetGenerator.generateDataset(), ",");
//        csvSerializer.serialize("/Users/ryan/Downloads/");
    }
}
