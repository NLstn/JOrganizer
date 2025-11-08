package com.nlstn.jmediaOrganizer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Supplier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nlstn.jmediaOrganizer.gui.Window;
import com.nlstn.jmediaOrganizer.properties.LaunchConfiguration;
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
                Path configuredHome;
                String existingProperty = System.getProperty("jmediaOrganizer.home");
                if (existingProperty != null && !existingProperty.isBlank())
                        configuredHome = Paths.get(existingProperty);
                else
                        configuredHome = Paths.get(System.getProperty("user.home"), ".jmediaOrganizer");

                Path absoluteHome = configuredHome.toAbsolutePath();
                try {
                        Files.createDirectories(absoluteHome);
                }
                catch (IOException e) {
                        throw new IllegalStateException(
                                        "Failed to initialize jmediaOrganizer home directory at " + absoluteHome, e);
                }

                System.setProperty("jmediaOrganizer.home", absoluteHome.toString());
                log = LogManager.getLogger(JMediaOrganizer.class);
        }

	/**
	 * The reference to the main window
	 */
	private static Window	window;
	private static Supplier<HeadlessHandler> headlessHandlerFactory = HeadlessHandler::new;

	/**
	 * The currently chosen input folder, or null, if no input folder was chosen
	 */
	private static Path		inputFolder	= null;

	public static void main(String[] args) {
		Settings.loadSettings();
		ProjectProperties.loadProjectProperties();
		LaunchConfiguration config = LaunchConfiguration.parse(args);
		log.info("Starting " + ProjectProperties.getName() + " v" + ProjectProperties.getVersion());
		if (config.isHeadlessModeEnabled())
			headlessHandlerFactory.get();
		else
			window = new Window();
	}

	static void setHeadlessHandlerFactory(Supplier<HeadlessHandler> factory) {
		headlessHandlerFactory = factory == null ? HeadlessHandler::new : factory;
	}

	static void resetHeadlessHandlerFactory() {
		headlessHandlerFactory = HeadlessHandler::new;
	}

	public static Window getWindow() {
		return window;
	}

	public static Path getInputFolder() {
		return inputFolder;
	}

	public static void setInputFolder(Path inputFolder) {
		JMediaOrganizer.inputFolder = inputFolder;
	}

}
