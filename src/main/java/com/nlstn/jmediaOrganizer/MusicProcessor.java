package com.nlstn.jmediaOrganizer;

import java.io.File;

import com.nlstn.jmediaOrganizer.gui.Window;

public class MusicProcessor {

	private static Window	window;

	private static File		inputFolder		= null;

	private static File		outputFolder	= null;

	public static void main(String[] args) {
		window = new Window();
	}

	public static Window getWindow() {
		return window;
	}

	public static File getInputFolder() {
		return inputFolder;
	}

	public static void setInputFolder(File inputFolder) {
		MusicProcessor.inputFolder = inputFolder;
	}

	public static File getOutputFolder() {
		return outputFolder;
	}

	public static void setOutputFolder(File outputFolder) {
		MusicProcessor.outputFolder = outputFolder;
	}

}
