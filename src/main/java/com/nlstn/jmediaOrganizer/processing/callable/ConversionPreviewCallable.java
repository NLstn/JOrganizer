package com.nlstn.jmediaOrganizer.processing.callable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

import com.nlstn.jmediaOrganizer.files.MP3File;
import com.nlstn.jmediaOrganizer.processing.Converter;
import com.nlstn.jmediaOrganizer.properties.Settings;

public class ConversionPreviewCallable implements Callable<List<String>> {

	public static final List<String>	invalidTypes	= Settings.getInvalidTypes();

	private List<File>					files;

	private volatile AtomicInteger		progress;

	public ConversionPreviewCallable(List<File> files) {
		this.files = files;
		progress = new AtomicInteger();
	}

	public List<String> call() {
		List<String> result = new ArrayList<String>();
		for (int i = 0; i < files.size(); i++) {
			MP3File mp3File = new MP3File(files.get(i));
			if (!mp3File.isOfType(invalidTypes) && mp3File.loadMp3Data())
				result.add(Converter.getNewPath(mp3File));
			synchronized (this) {
				progress.incrementAndGet();
			}
		}
		return result;
	}

	public int getProgress() {
		return progress.get();
	}
}
