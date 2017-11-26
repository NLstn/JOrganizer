package com.nlstn.musicProcessor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class FileProcessor {

	public static volatile int	progress;

	private static List<File>	currentFiles;

	private static File			rootFolder;

	public static List<File> loadAllFiles(File folder) {
		currentFiles = new ArrayList<File>();
		loadAllFilesRec(folder);
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
		int threadCount = 4;
		ExecutorService service = Executors.newFixedThreadPool(threadCount);
		List<FutureTask<List<String>>> futureTasks = new ArrayList<FutureTask<List<String>>>();

		List<String> result = new ArrayList<String>();

		int amountPerThread = currentFiles.size() / threadCount;

		for (int i = 0; i < threadCount; i++) {
			FutureTask<List<String>> task = new FutureTask<List<String>>(new ConversionPreviewCallable(i * amountPerThread, amountPerThread, currentFiles));
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
		System.out.println("Took: " + (System.currentTimeMillis() - currentTime));
		return result;
	}

	public static void cleanFolder(File folder) {
		if (rootFolder == null)
			rootFolder = folder;
		File[] subFiles = folder.listFiles();
		for (File subFile : subFiles) {
			if (subFile.isDirectory())
				cleanFolder(subFile);
		}
		if (folder.listFiles().length == 0) {
			System.out.println("Deleting Folder " + folder.getAbsolutePath());
			folder.delete();
		}
	}

	public static void convertFiles() {
		for (File file : currentFiles) {
			MP3File mp3File = new MP3File(file);
			if (mp3File.deleteIfOfType(ConversionPreviewCallable.invalidTypes)) {
				continue;
			}
			if (mp3File.loadMp3Data()) {
				mp3File.moveTonewLoc();
			}
		}
	}

	public static void clearCurrentFiles() {
		currentFiles = null;
	}

	public static boolean isFolderLoaded() {
		return currentFiles != null;
	}
}
