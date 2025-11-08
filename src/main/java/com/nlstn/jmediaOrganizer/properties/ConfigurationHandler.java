package com.nlstn.jmediaOrganizer.properties;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.XMLConfiguration;
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

	private static final Logger LOGGER = LogManager.getLogger(ConfigurationHandler.class);
	private static final Configurations configs = new Configurations();

	private FileBasedConfigurationBuilder<XMLConfiguration>	builder;
	private FileBasedConfiguration							config;

        public ConfigurationHandler(String fileName) {
                String homeProperty = System.getProperty("jmediaOrganizer.home");
                if (homeProperty == null || homeProperty.isBlank())
                        throw new IllegalStateException("System property 'jmediaOrganizer.home' must be set.");

                Path configurationDirectory = Paths.get(homeProperty).toAbsolutePath();
                try {
                        Files.createDirectories(configurationDirectory);
                }
                catch (IOException e) {
                        LOGGER.error("Failed to create configuration directory {}", configurationDirectory, e);
                        throw new IllegalStateException(
                                        "Unable to create configuration directory " + configurationDirectory, e);
                }

                Path propertiesPath = configurationDirectory.resolve(fileName);
                File propertiesFile = propertiesPath.toFile();
                if (Files.notExists(propertiesPath)) {
                        copySampleFile(fileName, propertiesPath);
                }
                Parameters params = new Parameters();
                builder = configs.fileBasedBuilder(XMLConfiguration.class, propertiesFile).configure(params.fileBased().setThrowExceptionOnMissing(false).setFile(propertiesFile).setListDelimiterHandler(new DefaultListDelimiterHandler(';')));
		try {
			config = builder.getConfiguration();
		}
		catch (ConfigurationException e) {
			LOGGER.error("Failed to create configuration!", e);
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
			LOGGER.error("Failed to save configuration!", e);
		}
	}

        private static void copySampleFile(String fileName, Path propertiesPath) {
                try (InputStream exampleFile = ConfigurationHandler.class.getResourceAsStream("/" + fileName)) {
                        if (exampleFile == null)
                                throw new IllegalStateException(
                                                "Missing sample configuration file on classpath: " + fileName);

                        Files.createDirectories(propertiesPath.getParent());
                        Files.copy(exampleFile, propertiesPath);
                }
                catch (IOException e) {
                        LOGGER.error("Failed to copy sample settings file!", e);
                        throw new IllegalStateException("Unable to copy sample settings file to " + propertiesPath, e);
                }
        }

}
