package com.nlstn.jmediaOrganizer.processing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nlstn.jmediaOrganizer.files.MP3File;
import com.nlstn.jmediaOrganizer.properties.Settings;

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

	private static Logger log;

	static {
		log = LogManager.getLogger(Converter.class);
	}

	private static List<ConverterVariable> availableVariables;

	static {
		availableVariables = new ArrayList<ConverterVariable>();
		availableVariables.add(new ConverterVariable("Output Folder", "%output%"));
		availableVariables.add(new ConverterVariable("File Extension", "%extension%"));
		availableVariables.add(new ConverterVariable("Artist", "%artist%"));
		availableVariables.add(new ConverterVariable("Track Nr", "%track%"));
		availableVariables.add(new ConverterVariable("Album", "%album%"));
		availableVariables.add(new ConverterVariable("Album Artist", "%albumArtist%"));
		availableVariables.add(new ConverterVariable("BPM", "%bpm%"));
		availableVariables.add(new ConverterVariable("Composer", "%composer%"));
		availableVariables.add(new ConverterVariable("Date", "%date%"));
		availableVariables.add(new ConverterVariable("Length", "%length%"));
		availableVariables.add(new ConverterVariable("Year", "%year%"));
		availableVariables.add(new ConverterVariable("Title", "%title%"));
		availableVariables.add(new ConverterVariable("Genre", "%genre%"));

		Collections.sort(availableVariables, new Comparator<ConverterVariable>() {
			public int compare(ConverterVariable o1, ConverterVariable o2) {
				return o1.getDisplayName().compareToIgnoreCase(o2.getDisplayName());
			}
		});
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

	public static String getNewPath(MP3File file, Pattern pattern) {

		List<ConverterVariable> usedVariables = pattern.getUsedVariables();
		String patternString = pattern.toString();

		for (ConverterVariable variable : usedVariables) {
			patternString.replace(variable.getVariable(), variable.getValue(file));
		}

		log.debug("Recalculated: " + file.getAbsolutePath() + " to " + pattern);

		return patternString;
	}

	public static List<ConverterVariable> getVariables() {
		return availableVariables;
	}
}
