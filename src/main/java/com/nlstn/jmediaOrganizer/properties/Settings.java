package com.nlstn.jmediaOrganizer.properties;

import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nlstn.jmediaOrganizer.processing.Pattern;

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
	 * The actual properties
	 */
	private static ConfigurationHandler config;

	/**
	 * Tries to find the file AppData\Roaming\JMediaOrganizer\settings.config.<br>
	 * If the file was found, the settings in it are being loaded.<br>
	 * If not, a new file will be created and filled with default values.
	 */
	public static void loadSettings() {
		log.info("Loading settings...");
		config = new ConfigurationHandler("settings.config");
	}

	/**
	 * Saves all settings to the settings file
	 */
	public static void save() {
		config.save();
		log.info("Settings saved");
	}

	public static List<String> getInvalidTypes() {
		List<String> list = Arrays.asList(config.getConfig().getStringArray("invalidTypes"));
		return list;
	}

	public static void setInvalidTypes(List<String> invalidTypes) {
		String[] typesArray = invalidTypes.stream().toArray(String[]::new);
		config.getConfig().setProperty("invalidTypes", typesArray);
	}

	public static boolean getID3ToNameEnabled() {
		return config.getConfig().getBoolean("id3ToNameEnabled", false);
	}

	public static void setID3ToNameEnabled(boolean enabled) {
		config.getConfig().setProperty("id3ToNameEnabled", enabled);
	}

	public static Pattern getID3ToNamePattern() {
		return new Pattern(config.getConfig().getString("id3ToNamePattern", ""));
	}

	public static void setID3ToNamePattern(Pattern pattern) {
		config.getConfig().setProperty("id3ToNamePattern", pattern);
	}

	public static String getOutputFolder() {
		return config.getConfig().getString("outputFolder", "");
	}

	public static void setOutputFolder(String outputFolder) {
		config.getConfig().setProperty("outputFolder", outputFolder);
	}

	public static int getThreadCount() {
		return config.getConfig().getInt("threadCount", 1);
	}

	public static void setThreadCount(int threadCount) {
		config.getConfig().setProperty("threadCount", threadCount);
	}

	public static void setStandardDirectoryChooserFolder(String folder) {
		config.getConfig().setProperty("standardDirectoryChooserFolder", folder);
	}

	public static String getStandardDirectoryChooserFolder() {
		return config.getConfig().getString("standardDirectoryChooserFolder", "");
	}

	public static boolean getDeleteRootFolder() {
		return config.getConfig().getBoolean("deleteRootFolder", false);
	}

	public static void setDeleteRootFolder(boolean value) {
		config.getConfig().setProperty("deleteRootFolder", String.valueOf(value));
	}
}