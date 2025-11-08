package com.nlstn.jmediaOrganizer.processing;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.nlstn.jmediaOrganizer.JMediaOrganizer;
import com.nlstn.jmediaOrganizer.properties.Settings;

public class FileProcessorTest {

        private Path inputDirectory;

        @BeforeClass
        public static void setupSettings() throws IOException {
                Path settingsDirectory = Files.createTempDirectory("jmedia-settings");
                System.setProperty("jmediaOrganizer.home", settingsDirectory.toString());
                Settings.loadSettings();
                Settings.setThreadCount(2);
        }

        @Before
        public void setup() throws IOException {
                inputDirectory = Files.createTempDirectory("jmedia-empty-input");
                JMediaOrganizer.setInputFolder(inputDirectory);
                FileProcessor.loadAllFiles();
        }

        @After
        public void tearDown() throws IOException {
                JMediaOrganizer.setInputFolder(null);
                if (inputDirectory != null) {
                        try (var paths = Files.walk(inputDirectory)) {
                                paths.sorted(Comparator.reverseOrder())
                                                .forEach(path -> {
                                                        try {
                                                                Files.deleteIfExists(path);
                                                        }
                                                        catch (IOException e) {
                                                                // ignore cleanup failures in tests
                                                        }
                                                });
                        }
                }
        }

        @Test
        public void getConversionPreviewReturnsEmptyListWhenNoFilesPresent() {
                assertTrue(FileProcessor.getConversionPreview().isEmpty());
        }

        @Test
        public void convertFilesSucceedsWhenNoFilesPresent() {
                assertTrue(FileProcessor.convertFiles());
        }
}
