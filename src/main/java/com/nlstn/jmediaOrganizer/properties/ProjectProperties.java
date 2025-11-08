package com.nlstn.jmediaOrganizer.properties;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.swing.JOptionPane;

import com.nlstn.jmediaOrganizer.JMediaOrganizer;
import com.nlstn.jmediaOrganizer.gui.Window;

/**
 * This class is used to access information in project.properties, which have previously been written to it by maven.<br>
 * An example for this is the project version (${project.version}):<br>
 * As the project.properties file has been added to the maven build resources and filtering has been enabled, ${project.version} will be replaced by the projects version.<br>
 * <br>
 * The file structure will be manually created and extended, so that i can be read easily via the Java {@link Properties}.<br>
 * <br>
 * Creation: 22.12.2017
 *
 * @author Niklas Lahnstein
 */
public class ProjectProperties {

	private static Properties properties;

	public static void loadProjectProperties() {
		properties = new Properties();
		try (InputStream stream = ProjectProperties.class.getResourceAsStream("/project.properties")) {
			properties.load(stream);
		}
		catch (IOException e) {
			e.printStackTrace();
			Window window = JMediaOrganizer.getWindow();
			JOptionPane.showMessageDialog(window == null ? null : window.getFrame(), "Error", "Failed to load properties file!", JOptionPane.ERROR_MESSAGE);
		}

	}

	/**
	 * @return The maven project version, which is also the program version
	 */
	public static String getVersion() {
		return properties.getProperty("version");
	}
	
	/**
	 * @return The maven project name
	 */
	public static String getName() {
		return properties.getProperty("name");
	}

}
