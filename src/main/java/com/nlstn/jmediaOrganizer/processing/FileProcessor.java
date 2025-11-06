package com.nlstn.jmediaOrganizer.processing;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nlstn.jmediaOrganizer.JMediaOrganizer;
import com.nlstn.jmediaOrganizer.files.MP3File;
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
public final class FileProcessor {

    private static final Logger LOGGER = LogManager.getLogger(FileProcessor.class);

    private static volatile List<File> currentFiles = List.of();
    private static volatile boolean folderLoaded;
    private static MP3File example;

    private FileProcessor() {
    }

    public static List<File> loadAllFiles() {
        File inputFolder = JMediaOrganizer.getInputFolder();
        if (inputFolder == null) {
            currentFiles = List.of();
            folderLoaded = false;
            return currentFiles;
        }

        try (Stream<Path> stream = Files.walk(inputFolder.toPath())) {
            currentFiles = stream
                    .filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .sorted(Comparator.comparing(File::getAbsolutePath, String.CASE_INSENSITIVE_ORDER))
                    .collect(Collectors.toCollection(ArrayList::new));
        }
        catch (IOException e) {
            throw new UncheckedIOException("Failed to load files from input folder " + inputFolder, e);
        }

        folderLoaded = true;
        example = null;
        LOGGER.debug("Found {} files in input folder {}", currentFiles.size(), inputFolder.getAbsolutePath());
        return List.copyOf(currentFiles);
    }

    public static List<String> getConversionPreview() {
        if (!folderLoaded || currentFiles.isEmpty()) {
            return List.of();
        }
        int fileCount = currentFiles.size();
        int threadCount = determineThreadCount(fileCount);
        List<List<File>> partitions = partitionFiles(threadCount);
        List<String> preview = new ArrayList<>();
        List<String> invalidTypes = Settings.getInvalidTypes();

        ExecutorService service = Executors.newFixedThreadPool(threadCount);
        try {
            List<Future<List<String>>> futures = service.invokeAll(partitions.stream()
                    .map(partition -> new ConversionPreviewCallable(partition, invalidTypes))
                    .toList());

            for (Future<List<String>> future : futures) {
                try {
                    preview.addAll(future.get());
                }
                catch (ExecutionException e) {
                    LOGGER.error("Failed to build conversion preview", e.getCause());
                }
            }
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Interrupted while building conversion preview", e);
        }
        finally {
            service.shutdown();
        }

        LOGGER.debug("Processed {} of {} files", preview.size(), fileCount);
        return preview;
    }

    public static boolean convertFiles() {
        if (!folderLoaded || currentFiles.isEmpty()) {
            return true;
        }
        int fileCount = currentFiles.size();
        int threadCount = determineThreadCount(fileCount);
        List<List<File>> partitions = partitionFiles(threadCount);
        List<String> invalidTypes = Settings.getInvalidTypes();

        ExecutorService service = Executors.newFixedThreadPool(threadCount);
        boolean success = true;
        try {
            List<Future<Boolean>> futures = service.invokeAll(partitions.stream()
                    .map(partition -> new ConversionCallable(partition, invalidTypes))
                    .toList());

            for (Future<Boolean> future : futures) {
                try {
                    success &= Boolean.TRUE.equals(future.get());
                }
                catch (ExecutionException e) {
                    LOGGER.error("Failed to convert files", e.getCause());
                    success = false;
                }
            }
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Interrupted while converting files", e);
        }
        finally {
            service.shutdown();
        }

        return success;
    }

    public static void cleanInputFolder() {
        File inputFolder = JMediaOrganizer.getInputFolder();
        if (inputFolder == null) {
            return;
        }
        File[] subFiles = inputFolder.listFiles();
        if (subFiles != null) {
            for (File subFile : subFiles) {
                if (subFile.isDirectory()) {
                    cleanFolder(subFile);
                }
            }
        }
        subFiles = inputFolder.listFiles();
        if (subFiles != null && subFiles.length == 0) {
            LOGGER.info("Deleting empty folder {}", inputFolder.getAbsolutePath());
            if (Settings.getDeleteRootFolder() && !inputFolder.delete()) {
                LOGGER.warn("Failed to delete empty root folder in cleanup stage: {}", inputFolder.getAbsolutePath());
            }
            JMediaOrganizer.setInputFolder(null);
        }
    }

    private static void cleanFolder(File folder) {
        File[] subFiles = folder.listFiles();
        if (subFiles != null) {
            for (File subFile : subFiles) {
                if (subFile.isDirectory()) {
                    cleanFolder(subFile);
                }
            }
        }
        subFiles = folder.listFiles();
        if (subFiles != null && subFiles.length == 0) {
            LOGGER.info("Deleting Folder {}", folder.getAbsolutePath());
            if (!folder.delete()) {
                LOGGER.warn("Failed to delete empty folder in cleanup stage: {}", folder.getAbsolutePath());
            }
        }
    }

    public static MP3File getPreviewExample() {
        if (!currentFiles.isEmpty()) {
            MP3File sample = new MP3File(currentFiles.get(0));
            if (sample.loadMp3Data()) {
                return sample;
            }
        }
        synchronized (FileProcessor.class) {
            if (example == null) {
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
        return example;
    }

    public static int getFileCount() {
        return currentFiles.size();
    }

    public static void clearCurrentFiles() {
        currentFiles = List.of();
        folderLoaded = false;
        example = null;
    }

    public static boolean isFolderLoaded() {
        return folderLoaded;
    }

    private static int determineThreadCount(int fileCount) {
        int configured = Settings.getThreadCount();
        if (configured <= 0) {
            configured = 1;
        }
        return Math.max(1, Math.min(configured, fileCount));
    }

    private static List<List<File>> partitionFiles(int partitions) {
        if (partitions <= 0) {
            return List.of();
        }
        List<List<File>> result = new ArrayList<>(partitions);
        int size = currentFiles.size();
        int baseSize = size / partitions;
        int remainder = size % partitions;
        int index = 0;
        for (int i = 0; i < partitions; i++) {
            int currentSize = baseSize + (i < remainder ? 1 : 0);
            int endIndex = index + currentSize;
            result.add(new ArrayList<>(currentFiles.subList(index, endIndex)));
            index = endIndex;
        }
        return result;
    }
}
