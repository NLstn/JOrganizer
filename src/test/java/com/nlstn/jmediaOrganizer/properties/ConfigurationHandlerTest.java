package com.nlstn.jmediaOrganizer.properties;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class ConfigurationHandlerTest {

        @TempDir
        Path temporaryFolder;

        private String previousHome;

        @BeforeEach
        void rememberHomeProperty() {
                previousHome = System.getProperty("jmediaOrganizer.home");
        }

        @AfterEach
        void restoreHomeProperty() {
                if (previousHome != null) {
                        System.setProperty("jmediaOrganizer.home", previousHome);
                } else {
                        System.clearProperty("jmediaOrganizer.home");
                }
        }

        @Test
        void loadsConfigurationWhenHomeUsesUnixSeparators() {
                Path customHome = temporaryFolder.resolve("config-root").resolve("nested");
                System.setProperty("jmediaOrganizer.home", customHome.toString());

                ConfigurationHandler handler = new ConfigurationHandler("settings.config");

                assertNotNull(handler.getConfig(), "Configuration should be available");
                assertTrue(Files.isDirectory(customHome), "Configuration directory should be created");
                assertTrue(Files.exists(customHome.resolve("settings.config")), "Configuration file should be copied");
        }
}
