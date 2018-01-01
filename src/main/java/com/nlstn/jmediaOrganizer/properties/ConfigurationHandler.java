package com.nlstn.jmediaOrganizer.properties;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.XMLConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.convert.DefaultListDelimiterHandler;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.io.IOUtils;
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

	private FileBasedConfigurationBuilder<XMLConfiguration>	builder;
	private FileBasedConfiguration							config;

	public ConfigurationHandler(String fileName) {
		File propertiesFile = new File(System.getProperty("jmediaOrganizer.home") + "\\" + fileName);
		if (!propertiesFile.exists()) {
			copySampleFile(fileName, propertiesFile);
		}
		Parameters params = new Parameters();
		builder = configs.fileBasedBuilder(XMLConfiguration.class, propertiesFile).configure(params.fileBased().setThrowExceptionOnMissing(false).setFile(propertiesFile).setListDelimiterHandler(new DefaultListDelimiterHandler(';')));
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

	private static void copySampleFile(String fileName, File propertiesFile) {
		FileOutputStream stream = null;
		InputStream exampleFile = null;
		try {
			stream = new FileOutputStream(propertiesFile);
			exampleFile = ConfigurationHandler.class.getResourceAsStream("/" + fileName);
			IOUtils.copy(exampleFile, stream);
		}
		catch (IOException e) {
			log.error("Failed to copy sample settings file!", e);
		}
		finally {
			try {
				stream.close();
				exampleFile.close();
			}
			catch (IOException e) {
				log.error("Failed to copy sample settings file!", e);
			}
		}
	}

}
