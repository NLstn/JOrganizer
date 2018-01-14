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

import com.nlstn.jmediaOrganizer.JMediaOrganizer;
import com.nlstn.jmediaOrganizer.MP3File;
import com.nlstn.jmediaOrganizer.processing.callable.ConversionCallable;
import com.nlstn.jmediaOrganizer.processing.callable.ConversionPreviewCallable;
import com.nlstn.jmediaOrganizer.properties.Settings;

/**
 * This class is responsible for processing files.
 * 
 * Creation: 23.12.2017
 *
 * @author Niklas Lahnstein
 */
public class FileProcessor {

	static {
		logger = LogManager.getLogger(FileProcessor.class);
	}

	public static volatile int	progress;

	private static List<File>	currentFiles;

	private static Logger		logger;

	private static MP3File		example;

	public static List<File> loadAllFiles() {
		currentFiles = new ArrayList<File>();
		loadAllFilesRec(JMediaOrganizer.getInputFolder());
		logger.debug("Found {} files in input folder {}", currentFiles.size(), JMediaOrganizer.getInputFolder().getAbsolutePath());
		return currentFiles;
	}

	private static void loadAllFilesRec(File folder) {
		File[] listFiles = folder.listFiles();
		if (listFiles != null)
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

		int fileCount = currentFiles.size();

		if (threadCount >= fileCount) {
			threadCount = fileCount;
		}

		ExecutorService service = Executors.newFixedThreadPool(threadCount);
		List<FutureTask<List<String>>> futureTasks = new ArrayList<FutureTask<List<String>>>();

		List<String> result = new ArrayList<String>();

		int amountPerThread = fileCount / threadCount;

		int processedFiles = 0;

		for (int i = 0; i < threadCount - 1; i++) {
			ConversionPreviewCallable callable = new ConversionPreviewCallable(currentFiles.subList(i * amountPerThread, i * amountPerThread + amountPerThread));
			processedFiles += amountPerThread;
			FutureTask<List<String>> task = new FutureTask<List<String>>(callable);
			futureTasks.add(task);
			service.execute(task);
		}

		int remainingFiles = fileCount - processedFiles;
		ConversionPreviewCallable callable = new ConversionPreviewCallable(currentFiles.subList(fileCount - remainingFiles, fileCount));
		FutureTask<List<String>> futureTask = new FutureTask<List<String>>(callable);
		futureTasks.add(futureTask);
		service.execute(futureTask);

		for (FutureTask<List<String>> task : futureTasks) {
			try {
				result.addAll(task.get());
			}
			catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}
		logger.debug("Processed " + result.size() + " of " + fileCount + " files");
		logger.debug("Took: " + (System.currentTimeMillis() - currentTime));
		return result;
	}

	public static boolean convertFiles() {
		long currentTime = System.currentTimeMillis();
		int threadCount = Settings.getThreadCount();

		int fileCount = currentFiles.size();

		if (threadCount >= fileCount) {
			threadCount = fileCount;
		}

		ExecutorService service = Executors.newFixedThreadPool(threadCount);
		List<FutureTask<Boolean>> futureTasks = new ArrayList<FutureTask<Boolean>>();

		int amountPerThread = fileCount / threadCount;

		int processedFiles = 0;

		for (int i = 0; i < threadCount - 1; i++) {
			ConversionCallable callable = new ConversionCallable(currentFiles.subList(i * amountPerThread, i * amountPerThread + amountPerThread));
			processedFiles += amountPerThread;
			FutureTask<Boolean> task = new FutureTask<Boolean>(callable);
			futureTasks.add(task);
			service.execute(task);
		}

		int remainingFiles = fileCount - processedFiles;
		ConversionCallable callable = new ConversionCallable(currentFiles.subList(fileCount - remainingFiles, fileCount));
		FutureTask<Boolean> futureTask = new FutureTask<Boolean>(callable);
		futureTasks.add(futureTask);
		service.execute(futureTask);

		boolean success = true;
		for (FutureTask<Boolean> task : futureTasks) {
			try {
				if (!task.get())
					success = false;
			}
			catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}
		logger.debug("Took: " + (System.currentTimeMillis() - currentTime));
		return success;
	}

	public static void cleanInputFolder() {
		File inputFolder = JMediaOrganizer.getInputFolder();
		File[] subFiles = inputFolder.listFiles();
		if (subFiles != null)
			for (File subFile : subFiles) {
				if (subFile.isDirectory())
					cleanFolder(subFile);
			}
		subFiles = inputFolder.listFiles();
		if (subFiles != null)
			if (subFiles.length == 0) {
				logger.info("Deleting empty folder " + inputFolder.getAbsolutePath());
				if (Settings.getDeleteRootFolder()) {
					if (!inputFolder.delete()) {
						logger.warn("Failed to delete empty root folder in cleanup stage: {}", inputFolder.getAbsolutePath());
					}
				}
				JMediaOrganizer.setInputFolder(null);
			}
	}

	private static void cleanFolder(File folder) {
		File[] subFiles = folder.listFiles();
		if (subFiles != null)
			for (File subFile : subFiles) {
				if (subFile.isDirectory())
					cleanFolder(subFile);
			}
		subFiles = folder.listFiles();
		if (subFiles != null)
			if (subFiles.length == 0) {
				logger.info("Deleting Folder " + folder.getAbsolutePath());
				if (!folder.delete())
					logger.warn("Failed to delete empty folder in cleanup stage: {}", folder.getAbsolutePath());
			}
	}

	public static MP3File getPreviewExample() {
		if (currentFiles != null) {
			MP3File example = new MP3File(currentFiles.get(0));
			if (example.loadMp3Data())
				return example;
		}
		synchronized (FileProcessor.class) {// synchronized because of multithreaded race condition in :189, where multiple examples might be created
			if (FileProcessor.example == null) {
				example = new MP3File();
				example.setTitle("Iridescent");
				example.setAlbum("A Thousand Suns");
				example.setTrack("12");
				example.setArtist("Linkin Park");
				example.setAlbumArtist("Linkin Park");
				example.setBPM(120);
				example.setGenre("Rock");
				example.setYear("2010");
				example.setDate("8. Sep 2010");
				example.setComposer("Warner Bros. Records");
			}
		}
		return FileProcessor.example;
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
