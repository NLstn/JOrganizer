package com.nlstn.jmediaOrganizer.processing;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nlstn.jmediaOrganizer.ID3ToNameConverter;
import com.nlstn.jmediaOrganizer.MusicProcessor;
import com.nlstn.jmediaOrganizer.Settings;

public class FileProcessor {

	static {
		logger = LogManager.getLogger(FileProcessor.class);
	}

	public static volatile int	progress;

	private static List<File>	currentFiles;

	private static Logger		logger;

	public static List<File> loadAllFiles() {
		currentFiles = new ArrayList<File>();
		loadAllFilesRec(MusicProcessor.getInputFolder());
		return currentFiles;
	}

	private static void loadAllFilesRec(File folder) {
		File[] listFiles = folder.listFiles();
		for (File file : listFiles) {
			if (file.isDirectory()) {
				loadAllFilesRec(file);
			}
			else
				currentFiles.add(file);
		}
	}

	public static List<String> getConversionPreview() {
		long currentTime = System.currentTimeMillis();
		int threadCount = Settings.getThreadCount();
		ExecutorService service = Executors.newFixedThreadPool(threadCount);
		List<FutureTask<List<String>>> futureTasks = new ArrayList<FutureTask<List<String>>>();
		List<ConversionPreviewCallable> callables = new ArrayList<ConversionPreviewCallable>();

		List<String> result = new ArrayList<String>();

		int amountPerThread = currentFiles.size() / threadCount;

		ID3ToNameConverter converter = new ID3ToNameConverter();

		for (int i = 0; i < threadCount; i++) {
			ConversionPreviewCallable callable = new ConversionPreviewCallable(converter, i * amountPerThread, amountPerThread, currentFiles);
			callables.add(callable);
			FutureTask<List<String>> task = new FutureTask<List<String>>(callable);
			futureTasks.add(task);
			service.execute(task);
		}

		for (FutureTask<List<String>> task : futureTasks) {
			try {
				result.addAll(task.get());
			}
			catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}
		logger.debug("Took: " + (System.currentTimeMillis() - currentTime));
		return result;
	}

	public static void cleanInputFolder() {
		File inputFolder = MusicProcessor.getInputFolder();
		File[] subFiles = inputFolder.listFiles();
		for (File subFile : subFiles) {
			if (subFile.isDirectory())
				cleanFolder(subFile);
		}
		if (inputFolder.listFiles().length == 0) {
			logger.info("Deleting empty folder " + inputFolder.getAbsolutePath());
			inputFolder.delete();
			MusicProcessor.setInputFolder(null);
		}
	}

	private static void cleanFolder(File folder) {
		File[] subFiles = folder.listFiles();
		for (File subFile : subFiles) {
			if (subFile.isDirectory())
				cleanFolder(subFile);
		}
		if (folder.listFiles().length == 0) {
			logger.info("Deleting Folder " + folder.getAbsolutePath());
			folder.delete();
		}
	}

	public static void convertFiles() {
		ID3ToNameConverter converter = new ID3ToNameConverter();
		for (File file : currentFiles) {
			MP3File mp3File = new MP3File(file);
			if (mp3File.deleteIfOfType(ConversionPreviewCallable.invalidTypes)) {
				continue;
			}
			if (mp3File.loadMp3Data()) {
				
			}
		}
	}

	public static MP3File getPreviewExample() {
		if (currentFiles == null || currentFiles.size() == 0)
			return null;
		MP3File example = new MP3File(currentFiles.get(0));
		if (!example.loadMp3Data())
			return null;
		return example;
	}

	public static int getFileCount() {
		return currentFiles.size();
	}

	public static void clearCurrentFiles() {
		currentFiles = null;
	}

	public static boolean isFolderLoaded() {
		return currentFiles != null;
	}
}
