package com.nlstn.jmediaOrganizer.test;

import static org.junit.Assert.*;

import java.io.File;
import java.util.List;

import org.junit.Test;

import com.nlstn.jmediaOrganizer.JMediaOrganizer;
import com.nlstn.jmediaOrganizer.processing.FileProcessor;
import com.nlstn.jmediaOrganizer.properties.Settings;

/**
 * Creation: 7 Jan 2018
 *
 * @author Niklas Lahnstein
 */
public class MainTest {

	@Test
	public void conversionPreviewSyntaxTest() {
		JMediaOrganizer.setInputFolder(new File("./src/test/resources/ConversionPreviewSyntaxTest"));
		Settings.loadSettings();
		Settings.setID3ToNameEnabled(true);
		Settings.setOutputFolder("D:/TestOutput");
		Settings.setID3ToNamePattern("%output%/%albumArtist% (%artist%) - %album% (%year%)/%track% - %title% (%bpm%)%extension%");
		FileProcessor.loadAllFiles();
		List<String> result = FileProcessor.getConversionPreview();
		assertEquals("Conversion Preview result should be D:/TestOutput/A Day to Remember (A Day to Remember) - And Their Name Was Treason (2005)/9 - 1958 (-1).mp3!", "D:/TestOutput/A Day to Remember (A Day to Remember) - And Their Name Was Treason (2005)/9 - 1958 (-1).mp3", result.get(0));
	}

	@Test
	public void conversionPreviewCountTest() {
		JMediaOrganizer.setInputFolder(new File("./src/test/resources/ConversionPreviewCountTest"));
		Settings.loadSettings();
		FileProcessor.loadAllFiles();
		List<String> result = FileProcessor.getConversionPreview();
		assertEquals("Conversion Preview count should be 14", 14, result.size());
	}

}
