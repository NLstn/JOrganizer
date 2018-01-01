package com.nlstn.jmediaOrganizer.properties;

import java.io.File;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.convert.DefaultListDelimiterHandler;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Creation: 31.12.2017
 *
 * @author Niklas Lahnstein
 */
public class ConfigurationHandler {

	private static Logger			log;
	private static Configurations	configs;

	static {
		log = LogManager.getLogger(ConfigurationHandler.class);
		configs = new Configurations();
	}

	private FileBasedConfigurationBuilder<FileBasedConfiguration>	builder;
	private FileBasedConfiguration									config;

	public ConfigurationHandler(String fileName) {
		File propertiesFile = new File(System.getProperty("jmediaOrganizer.home") + "\\" + fileName);
		Parameters params = new Parameters();
		builder = configs.fileBasedBuilder(FileBasedConfiguration.class, propertiesFile).configure(params.fileBased().setListDelimiterHandler(new DefaultListDelimiterHandler(';')));
		try {
			config = builder.getConfiguration();
		}
		catch (ConfigurationException e) {
			log.error("Failed to create configuration!", e);
		}
	}

	public Configuration getConfig() {
		return config;
	}

	public void save() {
		try {
			builder.save();
		}
		catch (ConfigurationException e) {
			log.error("Failed to save configuration!", e);
		}
	}

}
