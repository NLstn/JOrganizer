package com.nlstn.jmediaOrganizer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nlstn.jmediaOrganizer.processing.FileProcessor;

/**
 * Creation: 29.12.2017
 *
 * @author Niklas Lahnstein
 */
public class HeadlessHandler {

        private static final Logger LOGGER = LogManager.getLogger(HeadlessHandler.class);

	public HeadlessHandler() {
                LOGGER.info("Launching Headless Mode");
		run();
	}

	public void run() {
		FileProcessor.loadAllFiles();
		FileProcessor.convertFiles();
		FileProcessor.cleanInputFolder();
	}

}
