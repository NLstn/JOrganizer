package com.nlstn.jmediaOrganizer;

import java.io.IOException;
import java.util.Properties;

import javax.swing.JOptionPane;

public class ProjectProperties {

	private static Properties properties;

	public static void loadProjectProperties() {
		properties = new Properties();
		try {
			properties.load(ProjectProperties.class.getResourceAsStream("/project.properties"));
		}
		catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(JMediaOrganizer.getWindow().getFrame(), "Error", "Failed to load properties file!", JOptionPane.ERROR_MESSAGE);
		}

	}

	public static String getVersion() {
		return properties.getProperty("version");
	}

}
