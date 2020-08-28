package dgen.utils.serialization;

import dgen.column.ColumnConfig;
import dgen.coreconfig.DGException;
import dgen.dataset.DatasetConfig;
import dgen.datatypes.DataType;
import dgen.utils.parsers.specs.serializerspecs.Serializers;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * The Serializer interface splits up the dataset serialization process into
 * multiple generalized steps. Steps don't have to strictly be followed and
 * should be adapted in whatever ways necessary.
 */
public interface Serializer {

    /**
     * Writes metadata into a .json about the dataset object that can't be inferred from the low-level spec
     * used to create the database.
     * @param metadataOutputPath File to write to.
     * @param tableNames Mapping between table ID and table name.
     * @param columnNames Mapping between tableID and mapping of columnID to column name.
     */
    static void outputMetadata(String metadataOutputPath, Map<Integer, String> tableNames,
                               Map<Integer, Map<Integer, String>> columnNames) throws IOException {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("tableNames", tableNames);
        metadata.put("columnNames", columnNames);

        FileWriter metadataWriter;

        try {
            metadataWriter = new FileWriter(metadataOutputPath);
        } catch (Exception e) {
            throw new DGException(metadataOutputPath + " already exists");
        }

        metadataWriter.write(new ObjectMapper().writeValueAsString(metadata));

        metadataWriter.flush();
        metadataWriter.close();
    }

    Serializers serializerType();

    /**
     * Creates a directory to host serialized files.
     * @param directoryName Name of directory to make.
     */
    void directorySetup(String directoryName);

    /**
     * Creates a file to serialize the dataset into.
     * @param fileName Name of file to make.
     * @throws IOException
     */
    void fileSetup(String fileName) throws IOException;

    /**
     * Setup (created file writers, storing column name info, etc.) necessary before serializing a table.
     * @param tableName Name of table to be serialized.
     * @param columnNames Name of columns within table.
     * @param columnConfigs Configuration objects for each column.
     * @throws IOException
     */
    void serializationSetup(String tableName, LinkedHashMap<Integer, String> columnNames, List<ColumnConfig> columnConfigs) throws IOException;

    /**
     * Serializes a row/column of a table within the dataset (depending on serialization format).
     * @param data Datatypes to be serialized.
     * @param tableName Name of table being serialized.
     * @throws Exception
     */
    void serialize(List<DataType> data, String tableName) throws Exception;

    /**
     * Cleans up after serializing a table (creating file writers, flushing file writers, etc.).
     * @throws IOException
     */
    void postSerialization() throws IOException;

    /**
     * Final steps that couldn't be done during the table serialization process (e.g. creating pk-fk constraints).
     * @param datasetConfig Configuration file of dataset.
     * @param tableNames Mapping between table ID and table name.
     * @param columnNames Mapping between tableID and mapping of columnID to column name.
     * @throws IOException
     */
    void cleanup(DatasetConfig datasetConfig, Map<Integer, String> tableNames,
                 Map<Integer, Map<Integer, String>> columnNames) throws IOException;
}
