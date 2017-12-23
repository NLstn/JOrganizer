package com.nlstn.jmediaOrganizer.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.swing.JOptionPane;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nlstn.jmediaOrganizer.JMediaOrganizer;

/**
 * This class is used to control settings. Settings are being saved to a file under C:\Users\User\AppData\Roaming\JMediaOrganizer.<br>
 * <br>
 * Creation: 09.12.2017<br>
 *
 * @author Niklas Lahnstein
 */
public class Settings {

	private static Logger log;

	static {
		log = LogManager.getLogger(Settings.class);
	}

	/**
	 * The path, where the config file is.
	 */
	private static String		propertiesPath;
	/**
	 * The File representation of the properties path
	 */
	private static File			propertiesFile;

	/**
	 * The actual properties
	 */
	private static Properties	properties;

	/**
	 * Tries to find the file AppData\Roaming\JMediaOrganizer\settings.config.<br>
	 * If the file was found, the settings in it are being loaded.<br>
	 * If not, a new file will be created and filled with default values.
	 */
	public static void loadSettings() {
		propertiesPath = System.getenv("APPDATA") + "\\JMediaOrganizer\\settings.config";
		propertiesFile = new File(propertiesPath);
		properties = new Properties();
		if (!propertiesFile.exists()) {
			propertiesFile.getParentFile().mkdirs();
			try {
				propertiesFile.createNewFile();
			}
			catch (IOException e) {
				log.error("Failed to load settings!", e);
				JOptionPane.showMessageDialog(JMediaOrganizer.getWindow().getFrame(), "Error", "Failed to create properties file!", JOptionPane.ERROR_MESSAGE);
			}
		}
		else {
			try {
				properties.load(new FileInputStream(propertiesFile));
			}
			catch (IOException e) {
				log.error("Failed to parse settings!", e);
				JOptionPane.showMessageDialog(JMediaOrganizer.getWindow().getFrame(), "Error", "Failed to load properties file!", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	/**
	 * Saves all settings to the settings file
	 */
	public static void save() {
		try {
			OutputStream out = new FileOutputStream(propertiesFile);
			properties.store(out, "User Properties");
		}
		catch (IOException e) {
			log.error("Failed to save settings!", e);
			JOptionPane.showMessageDialog(JMediaOrganizer.getWindow().getFrame(), "Error", "Failed to save properties file!", JOptionPane.ERROR_MESSAGE);
		}
	}

	public static List<String> getInvalidTypes() {
		String setting = properties.getProperty("invalidTypes", ".png;.jpg;.jpeg;.gif");
		List<String> invalidTypes = Arrays.asList(setting.split(";"));
		return invalidTypes;
	}

	public static void setInvalidTypes(List<String> invalidTypes) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < invalidTypes.size() - 1; i++) {
			builder.append(invalidTypes.get(i)).append(";");
		}
		builder.append(invalidTypes.get(invalidTypes.size() - 1));
		properties.setProperty("invalidTypes", builder.toString());
	}

	public static boolean getID3ToNameEnabled() {
		return Boolean.valueOf(properties.getProperty("id3ToNameEnabled", "false"));
	}

	public static void setID3ToNameEnabled(boolean enabled) {
		properties.setProperty("id3ToNameEnabled", String.valueOf(enabled));
	}

	public static String getID3ToNamePattern() {
		return properties.getProperty("id3ToNamePattern");
	}

	public static void setID3ToNamePattern(String pattern) {
		properties.setProperty("id3ToNamePattern", pattern);
	}

	public static String getOutputFolder() {
		return properties.getProperty("outputFolder", "");
	}

	public static void setOutputFolder(String outputFolder) {
		properties.setProperty("outputFolder", outputFolder);
	}

	public static int getThreadCount() {
		return Integer.parseInt(properties.getProperty("threadCount", "1"));
	}

	public static void setThreadCount(int threadCount) {
		properties.setProperty("threadCount", String.valueOf(threadCount));
	}
}