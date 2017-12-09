package com.nlstn.jmediaOrganizer;

import com.nlstn.jmediaOrganizer.processing.MP3File;

public class ID3ToNameConverter {

	private String pattern;

	public ID3ToNameConverter() {
		pattern = Settings.getID3ToNamePattern();
	}

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
