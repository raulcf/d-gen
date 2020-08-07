package dgen.utils.serialization;

import dgen.column.Column;
import dgen.dataset.Dataset;
import dgen.tables.Table;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

public interface Serializer {

    /**
     * Writes metadata into a .json about a Dataset object that can't be inferred from the low-level spec
     * used to create the database.
     * @param path File to write to.
     * @param dataset Dataset object to write metadata for.
     */
    static void outputMetadata(String path, Dataset dataset) throws Exception {
        Map<String, Object> metadata = new HashMap<>();

        Map<Integer, String> tableNames = new HashMap<>();
        for (Table table: dataset.getTables()) {
            tableNames.put(table.getTableID(), table.getAttributeName());
        }
        metadata.put("tableNames", tableNames);

        Map<Integer, Object> tableMapping = new HashMap<>();
        for (Table table: dataset.getTables()) {
            Map<Integer, String> columnNames = new HashMap<>();
            for (Column column: table.getColumns()) {
                columnNames.put(column.getColumnID(), column.getAttributeName());
            }
            tableMapping.put(table.getTableID(), columnNames);
        }
        metadata.put("columnNames", tableMapping);


        File metadataFile = new File(path);
        if (metadataFile.exists()) {
            throw new Exception("File " + metadataFile + " already exists");
        }

        FileWriter metadataWriter = new FileWriter(metadataFile);
        metadataWriter.write(new ObjectMapper().writeValueAsString(metadata));

        metadataWriter.flush();
        metadataWriter.close();
    }

    void serialize(String outPath, String metadataPath) throws Exception;
}
