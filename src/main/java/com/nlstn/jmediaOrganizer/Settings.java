package com.nlstn.jmediaOrganizer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

public class Settings {

	private static String		propertiesPath;
	private static File			propertiesFile;

	private static Properties	properties;

	public static void loadSettings() {
		propertiesPath = System.getenv("APPDATA") + "\\MusicProcessor\\user.properties";
		propertiesFile = new File(propertiesPath);
		properties = new Properties();
		if (!propertiesFile.exists()) {
			// TODO: catch failed to create properties file
			propertiesFile.getParentFile().mkdirs();
			try {
				propertiesFile.createNewFile();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		else {
			try {
				properties.load(new FileInputStream(propertiesFile));
			}
			catch (IOException e) {
				// TODO catch failed loading
				e.printStackTrace();
			}
		}
	}

	public static void save() {
		try {
			OutputStream out = new FileOutputStream(propertiesFile);
			properties.store(out, "User Properties");
		}
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		return properties.getProperty("outputFolder");
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