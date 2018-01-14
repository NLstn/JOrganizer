package com.nlstn.jmediaOrganizer.processing.callable;

import java.io.File;
import java.util.List;
import java.util.concurrent.Callable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nlstn.jmediaOrganizer.MP3File;
import com.nlstn.jmediaOrganizer.processing.Converter;

/**
 * Creation: 6 Jan 2018
 *
 * @author Niklas Lahnstein
 */
public class ConversionCallable implements Callable<Boolean> {

	private static Logger log;

	static {
		log = LogManager.getLogger(ConversionCallable.class);
	}

	private List<File> files;

	public ConversionCallable(List<File> files) {
		this.files = files;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.concurrent.Callable#call()
	 */
	public Boolean call() throws Exception {
		boolean success = true;
		for (File file : files) {
			MP3File mp3File = new MP3File(file);
			if (mp3File.deleteIfOfType(ConversionPreviewCallable.invalidTypes)) {
				continue;
			}
			if (mp3File.loadMp3Data()) {
				if (mp3File.moveToLocation(Converter.getNewPath(mp3File))) {
					if (!file.delete())
						log.warn("Failed to delete relocated file: " + file.getAbsolutePath());
				}
				else
					success = false;
			}
		}
		return Boolean.valueOf(success);
	}

}
