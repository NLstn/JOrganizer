package com.nlstn.jmediaOrganizer.processing;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.nlstn.jmediaOrganizer.JMediaOrganizer;
import com.nlstn.jmediaOrganizer.properties.Settings;

class FileProcessorTest {

        @TempDir
        static Path settingsDirectory;

        @TempDir
        Path inputDirectory;

        @BeforeAll
        static void setupSettings() {
                System.setProperty("jmediaOrganizer.home", settingsDirectory.toString());
                Settings.loadSettings();
                Settings.setThreadCount(2);
        }

        @BeforeEach
        void setup() {
                JMediaOrganizer.setInputFolder(inputDirectory.toFile());
                FileProcessor.loadAllFiles();
        }

        @AfterEach
        void tearDown() {
                JMediaOrganizer.setInputFolder(null);
        }

        @Test
        void getConversionPreviewReturnsEmptyListWhenNoFilesPresent() {
                assertTrue(FileProcessor.getConversionPreview().isEmpty());
        }

        @Test
        void convertFilesSucceedsWhenNoFilesPresent() {
                assertTrue(FileProcessor.convertFiles());
        }
}
