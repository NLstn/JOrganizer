package com.nlstn.jmediaOrganizer;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nlstn.jmediaOrganizer.gui.Window;
import com.nlstn.jmediaOrganizer.properties.ProjectProperties;
import com.nlstn.jmediaOrganizer.properties.Settings;

/**
 * Main class, handles initialization.<br>
 * <br>
 * Creation: 09.12.2017<br>
 *
 * @author Niklas Lahnstein
 */
public class JMediaOrganizer {

	private static Logger log;

	static {
		log = LogManager.getLogger(JMediaOrganizer.class);
	}

	/**
	 * The reference to the main window
	 */
	private static Window	window;

	/**
	 * The currently chosen input folder, or null, if no input folder was chosen
	 */
	private static File		inputFolder	= null;

	public static void main(String[] args) {
		LaunchConfiguration config = LaunchConfiguration.parse(args);
//		ProjectProperties.loadProjectProperties();
//		log.info("Starting JMediaOrganizer v" + ProjectProperties.getVersion());
//		Settings.loadSettings();
//		window = new Window();
	}

	public static Window getWindow() {
		return window;
	}

	public static File getInputFolder() {
		return inputFolder;
	}

	public static void setInputFolder(File inputFolder) {
		JMediaOrganizer.inputFolder = inputFolder;
	}

}
