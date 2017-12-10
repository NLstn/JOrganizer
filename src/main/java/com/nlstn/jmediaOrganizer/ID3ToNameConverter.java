package com.nlstn.jmediaOrganizer;

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
public class ID3ToNameConverter {

	/**
	 * The user defined pattern
	 */
	private String pattern;

	/**
	 * Loads the user pattern
	 */
	public ID3ToNameConverter() {
		pattern = Settings.getID3ToNamePattern();
	}

	/**
	 * Replaces all variables in the pattern with the values from the file.
	 * @param file The file to take the information of
	 * @return The new path for this file
	 */
	public String getNewPath(MP3File file) {
		String pattern = new String(this.pattern);
		pattern = pattern.replace("%output%", Settings.getOutputFolder());
		pattern = pattern.replace("%artist%", file.getArtist());
		pattern = pattern.replace("%track%", file.getTrack());
		pattern = pattern.replace("%album%", file.getAlbum());
		pattern = pattern.replace("%title%", file.getTitle());
		pattern = pattern.replace("%extension%", file.getExtension());

		return pattern;
	}
}
