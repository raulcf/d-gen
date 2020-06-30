package dgen.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import me.ingestion.sources.*;
import me.ingestion.sources.SourceConfig;
import me.ingestion.sources.connectors.config.ConnectionType;
import me.ingestion.sources.connectors.config.JDBCConnectorConfig;
import me.ingestion.sources.connectors.config.LocalFSConnectorConfig;
import me.ingestion.sources.connectors.config.S3ConnectorConfig;
import me.ingestion.sources.serdes.config.CSVSerdeConfig;
import me.ingestion.sources.serdes.config.ParquetSerdeConfig;
import me.ingestion.sources.serdes.config.PostgresSerdeConfig;
import me.ingestion.sources.serdes.config.SerdeType;
import me.ingestion.sources.util.DataModel;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class YAMLParser {

	final private static Logger LOG = LoggerFactory.getLogger(IngestionYAMLParser.class.getName());

	public static List<Source> createSourceObjs(String path) {
		String syspath = FilenameUtils.separatorsToSystem(path);
		File file = new File(syspath);
		List<Source> res = new ArrayList<>();
		MultipleSourceDetails srcs = null;
		ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
		try {
		    srcs = mapper.readValue(file, MultipleSourceDetails.class);
		} catch (Exception e) {
		    LOG.error("While parsing {} file to read sources", file.toPath());
		    e.printStackTrace();
		    System.exit(0);
		}
		//We'll come back to the below two lines once we're sure what YAML attributes we want
		//int apiVersion = srcs.getApi_version();
		//assert (apiVersion == 0); // to support api evolution

		// Parse every source in the file
		List<SourceDetails> srcDets = srcs.getSourceDetails();
		for (SourceDetails src : srcDets) {
		    String name = src.getName();
		    SerdeType stype = src.getSerdeType();
		    ConnectionType ctype = src.getConnectionType();
		    DataModel dm = src.getDataModel();
		    JsonNode props = src.getRawConfig();
			SourceConfig sc = mapToSourceConfig(stype, ctype, props, mapper);
		    sc.setSourceName(name);
		    sc.setDataModel(dm);
		    Source newS = new GenericSource(sc);
		    res.add(newS);
		}

		return res;
	}

	public static SourceConfig mapToSourceConfig(SerdeType stype, ConnectionType ctype, JsonNode props, ObjectMapper mapper) {
		SourceConfig sc = new SourceConfig();
		JsonNode serdeConfig = props.get("serde");
		switch(stype) {
			case CSV:
				if(serdeConfig == null || serdeConfig.size() == 0){
					sc.setSerdeConfig(new CSVSerdeConfig());
				}else{
					sc.setSerdeConfig(mapper.convertValue(serdeConfig, CSVSerdeConfig.class));
				}
				LOG.info("Generated CSV config");
				break;
			case PARQUET:
				if(serdeConfig == null || serdeConfig.size() == 0){
					sc.setSerdeConfig(new ParquetSerdeConfig());
				}else{
					sc.setSerdeConfig(mapper.convertValue(serdeConfig, ParquetSerdeConfig.class));
				}
				LOG.info("Generated Parquet config");
				break;
			case POSTGRES:
				if(serdeConfig == null || serdeConfig.size() == 0){
					sc.setSerdeConfig(new PostgresSerdeConfig());
				}else{
					sc.setSerdeConfig(mapper.convertValue(serdeConfig, PostgresSerdeConfig.class));
				}
				break;
		}
		JsonNode connectorConfig = props.get("connector");
		switch(ctype) {
			case LocalFS:
				sc.setConnectorConfig(mapper.convertValue(connectorConfig, LocalFSConnectorConfig.class));
				break;
			case JDBC:
				sc.setConnectorConfig(mapper.convertValue(connectorConfig, JDBCConnectorConfig.class));
				break;
			case HDFS:
				break;
			case S3Bucket:
				sc.setConnectorConfig(mapper.convertValue(connectorConfig, S3ConnectorConfig.class));
				break;
		}

		return sc;
	}
}
