package com.nlstn.jmediaOrganizer.processing;

import com.nlstn.jmediaOrganizer.files.MP3File;
import com.nlstn.jmediaOrganizer.properties.Settings;

public class ConverterVariable {

	private String	displayName;
	private String	variable;

	public ConverterVariable(String displayName, String variable) {
		this.displayName = displayName;
		this.variable = variable;
	}

	public String getValue(MP3File file) {
		switch (getVariable()) {
			case "%artist%":
				return file.getArtist();
			case "%track%":
				return file.getTrack();
			case "%album%":
				return file.getAlbum();
			case "%albumArtist%":
				return file.getAlbumArtist();
			case "%bpm%":
				return file.getBPM();
			case "%composer%":
				return file.getComposer();
			case "%date%":
				return file.getDate();
			case "%length%":
				return file.getLength();
			case "%year%":
				return file.getYear();
			case "%title%":
				return file.getTitle();
			case "%genre%":
				return file.getGenre();
			case "%output%":
				return Settings.getOutputFolder();
		}
		return "";
	}

	public String getDisplayName() {
		return displayName;
	}

	public String getVariable() {
		return variable;
	}

	public String toString() {
		return displayName;
	}
}
