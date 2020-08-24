package dgen.utils.serialization;

import dgen.column.Column;
import dgen.column.ColumnConfig;
import dgen.dataset.Dataset;
import dgen.dataset.DatasetConfig;
import dgen.datatypes.DataType;
import dgen.tables.Table;
import dgen.utils.parsers.specs.serializerspecs.Serializers;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public interface Serializer {

    /**
     * Writes metadata into a .json about a Dataset object that can't be inferred from the low-level spec
     * used to create the database.
     * @param path File to write to.
     * @param dataset Dataset object to write metadata for.
     */
    static void outputMetadata(String path, Dataset dataset) throws Exception {
//        Map<String, Object> metadata = new HashMap<>();
//
//        Map<Integer, String> tableNames = new HashMap<>();
//        for (Table table: dataset.getTables()) {
//            tableNames.put(table.getTableID(), table.getAttributeName());
//        }
//        metadata.put("tableNames", tableNames);
//
//        Map<Integer, Object> tableMapping = new HashMap<>();
//        for (Table table: dataset.getTables()) {
//            Map<Integer, String> columnNames = new HashMap<>();
//            for (Column column: table.getColumns()) {
//                columnNames.put(column.getColumnID(), column.getAttributeName());
//            }
//            tableMapping.put(table.getTableID(), columnNames);
//        }
//        metadata.put("columnNames", tableMapping);
//
//
//        File metadataFile = new File(path);
//        if (metadataFile.exists()) {
//            throw new Exception("File " + metadataFile + " already exists");
//        }
//
//        FileWriter metadataWriter = new FileWriter(metadataFile);
//        metadataWriter.write(new ObjectMapper().writeValueAsString(metadata));
//
//        metadataWriter.flush();
//        metadataWriter.close();
    }

    Serializers serializerType();

    void directorySetup(String directoryName);

    void fileSetup(String fileName) throws IOException;

    void serializationSetup(String tableName, LinkedHashMap<Integer, String> columnNames, List<ColumnConfig> columnConfigs) throws IOException;

    void serialize(List<DataType> data, String tableName) throws Exception;

    void postSerialization() throws IOException;

    void cleanup(DatasetConfig datasetConfig, Map<Integer, String> tableNames,
                 Map<Integer, Map<Integer, String>> columnNames) throws IOException;

//    void writeMetadata() throws Exception;

//    void serialize(String outPath, String metadataPath) throws Exception;
}
