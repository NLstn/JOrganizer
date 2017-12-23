package com.nlstn.jmediaOrganizer.processing;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import com.nlstn.jmediaOrganizer.MP3File;
import com.nlstn.jmediaOrganizer.properties.Settings;

public class ConversionPreviewCallable implements Callable<List<String>> {

	public static List<String>	invalidTypes	= Settings.getInvalidTypes();

	private int					startIndex;
	private List<File>			files;
	private int					amount;

	private volatile int		progress;

	public ConversionPreviewCallable(int startIndex, int amount, List<File> files) {
		this.startIndex = startIndex;
		this.amount = amount;
		this.files = files;
	}

	public List<String> call() {
		List<String> result = new ArrayList<String>();
		for (int i = startIndex; i < startIndex + amount; i++) {
			MP3File mp3File = new MP3File(files.get(i));
			if (!mp3File.isOfType(invalidTypes) && mp3File.loadMp3Data())
				result.add(Converter.getNewPath(mp3File));
			progress++;
		}
		return result;
	}

	public int getProgress() {
		return progress;
	}
}
