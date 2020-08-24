package dgen.utils.serialization.config;

import dgen.coreconfig.Config;
import dgen.coreconfig.ConfigDef;
import dgen.coreconfig.ConfigKey;
import dgen.utils.parsers.specs.serializerspecs.ParquetSerializerSpec;
import dgen.utils.parsers.specs.serializerspecs.Serializers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParquetConfig extends Config implements SerializerConfig {

    private static final ConfigDef config;

    static {
        config = new ConfigDef()
                .define(SerializerConfig.METADATA_OUTPUT_PATH, ConfigDef.Type.STRING, ConfigDef.Importance.LOW, SerializerConfig.METADATA_OUTPUT_PATH_DOC)
                .define(SerializerConfig.PARENT_DIRECTORY, ConfigDef.Type.STRING, ConfigDef.Importance.LOW, SerializerConfig.PARENT_DIRECTORY_DOC);
    }

    public static ParquetConfig specToConfig(ParquetSerializerSpec serializerSpec) {
        Map<String, Object> originals = new HashMap<>();
        originals.put(SerializerConfig.METADATA_OUTPUT_PATH, serializerSpec.getMetadataOutputPath());
        originals.put(SerializerConfig.PARENT_DIRECTORY, serializerSpec.getParentDirectory());

        return new ParquetConfig(originals);
    }

    public static ParquetConfig specToGenerator(ParquetSerializerSpec parquetSerializerSpec) {
        return null;
    }

    public ParquetConfig(Map<? extends Object, ? extends Object> originals) {
        super(config, originals);
    }

    @Override
    public Serializers serializerType() {
        return Serializers.PARQUET;
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

