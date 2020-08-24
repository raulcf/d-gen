package dgen.utils.serialization.config;

import dgen.coreconfig.Config;
import dgen.coreconfig.ConfigDef;
import dgen.coreconfig.ConfigKey;
import dgen.utils.parsers.specs.serializerspecs.CSVSerializerSpec;
import dgen.utils.parsers.specs.serializerspecs.Serializers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSVConfig extends Config implements SerializerConfig{

    private static final ConfigDef config;

    public static final String DELIMITER = "delimiter";
    private static final String DELIMITER_DOC = "Delimiter to use when serializing";

    static {
        config = new ConfigDef()
                .define(DELIMITER, ConfigDef.Type.STRING, ",", ConfigDef.Importance.LOW, DELIMITER_DOC)
                .define(SerializerConfig.METADATA_OUTPUT_PATH, ConfigDef.Type.STRING, ConfigDef.Importance.LOW, SerializerConfig.METADATA_OUTPUT_PATH_DOC)
                .define(SerializerConfig.PARENT_DIRECTORY, ConfigDef.Type.STRING, ConfigDef.Importance.LOW, SerializerConfig.PARENT_DIRECTORY_DOC);
    }

    public static CSVConfig specToConfig(CSVSerializerSpec serializerSpec) {
        Map<String, Object> originals = new HashMap<>();
        originals.put(CSVConfig.DELIMITER, serializerSpec.getDelimiter());
        originals.put(SerializerConfig.METADATA_OUTPUT_PATH, serializerSpec.getMetadataOutputPath());
        originals.put(SerializerConfig.PARENT_DIRECTORY, serializerSpec.getParentDirectory());

        return new CSVConfig(originals);
    }

    public static CSVConfig specToGenerator(CSVSerializerSpec csvSerializerSpec) {
        return null;
    }

    public CSVConfig(Map<? extends Object, ? extends Object> originals) {
        super(config, originals);
    }

    @Override
    public Serializers serializerType() {
        return Serializers.CSV;
    }

    public static ConfigKey getConfigKey(String name) {
        return config.getConfigKey(name);
    }

    public static List<ConfigKey> getAllConfigKey() {
        return config.getAllConfigKey();
    }

    public static void main(String[] args) {
        System.out.println(config.toHtmlTable());
    }
}
