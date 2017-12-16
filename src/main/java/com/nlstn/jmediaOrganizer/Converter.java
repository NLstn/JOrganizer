package com.nlstn.jmediaOrganizer;

import java.util.ArrayList;
import java.util.List;

import com.nlstn.jmediaOrganizer.processing.MP3File;

/**
 * Converter, to set the new path + filename of the file after processing.<br>
 * The converter loads the user defined pattern and replaces the variables in this <br>
 * pattern with the information from the file.<br>
 * <br>
 * Creation: 09.12.2017<br>
 *
 * @author Niklas Lahnstein
 */
public class Converter {

	private static List<String> availableVariables;

	static {
		availableVariables = new ArrayList<String>();
		availableVariables.add("%output%");
		availableVariables.add("%extension%");
		availableVariables.add("%artist%");
		availableVariables.add("%track%");
		availableVariables.add("%album%");
		availableVariables.add("%albumArtist%");
		availableVariables.add("%bpm%");
		availableVariables.add("%composer%");
		availableVariables.add("%date%");
		availableVariables.add("%length%");
		availableVariables.add("%yeart%");
		availableVariables.add("%title%");
	}

	/**
	 * Replaces all variables in the pattern with the values from the file.
	 * 
	 * @param file
	 *            The file to take the information of
	 * @return The new path for this file
	 */
	public static String getNewPath(MP3File file) {
		return getNewPath(file, Settings.getID3ToNamePattern());
	}

	public static String getNewPath(MP3File file, String pattern) {
		pattern = pattern.replace("%output%", Settings.getOutputFolder());
		pattern = pattern.replace("%extension%", file.getExtension());

		pattern = pattern.replace("%artist%", file.getArtist());
		pattern = pattern.replace("%track%", file.getTrack());
		pattern = pattern.replace("%album%", file.getAlbum());
		pattern = pattern.replace("%albumArtist%", file.getAlbumArtist());
		pattern = pattern.replace("%bpm%", file.getBPM());
		pattern = pattern.replace("%composer", file.getComposer());
		pattern = pattern.replace("%date%", file.getDate());
		pattern = pattern.replace("%length%", file.getLength());
		pattern = pattern.replace("%year%", file.getYear());
		pattern = pattern.replace("%title%", file.getTitle());

		return pattern;
	}
}
