package com.nlstn.jmediaOrganizer.processing;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import com.nlstn.jmediaOrganizer.ID3ToNameConverter;

public class ConversionPreviewCallable implements Callable<List<String>> {

	public static List<String> invalidTypes = new ArrayList<String>();

	static {
		invalidTypes.add(".nfo");
		invalidTypes.add(".png");
		invalidTypes.add(".jpg");
		invalidTypes.add(".jpeg");
		invalidTypes.add(".gif");
		invalidTypes.add(".txt");
		invalidTypes.add(".m3u");
		invalidTypes.add(".pdf");
		invalidTypes.add(".doc");
		invalidTypes.add(".plc");
		invalidTypes.add(".pls");
	}

	private int					startIndex;
	private List<File>			files;
	private int					amount;

	private ID3ToNameConverter	converter;

	private volatile int		progress;

	public ConversionPreviewCallable(ID3ToNameConverter converter, int startIndex, int amount, List<File> files) {
		this.converter = converter;
		this.startIndex = startIndex;
		this.amount = amount;
		this.files = files;
	}

	public List<String> call() {
		List<String> result = new ArrayList<String>();
		for (int i = startIndex; i < startIndex + amount; i++) {
			MP3File mp3File = new MP3File(files.get(i));
			if (!mp3File.isOfType(invalidTypes) && mp3File.loadMp3Data())
				result.add(converter.getNewPath(mp3File));
			progress++;
		}
		return result;
	}

	public int getProgress() {
		return progress;
	}
}
