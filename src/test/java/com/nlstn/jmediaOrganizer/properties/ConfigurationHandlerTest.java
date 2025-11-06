package com.nlstn.jmediaOrganizer.properties;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class ConfigurationHandlerTest {

        @Rule
        public TemporaryFolder temporaryFolder = new TemporaryFolder();

        private String previousHome;

        @Before
        public void rememberHomeProperty() {
                previousHome = System.getProperty("jmediaOrganizer.home");
        }

        @After
        public void restoreHomeProperty() {
                if (previousHome != null)
                        System.setProperty("jmediaOrganizer.home", previousHome);
                else
                        System.clearProperty("jmediaOrganizer.home");
        }

        @Test
        public void loadsConfigurationWhenHomeUsesUnixSeparators() throws IOException {
                File root = temporaryFolder.getRoot();
                Path customHome = root.toPath().resolve("config-root/nested");
                System.setProperty("jmediaOrganizer.home", customHome.toString());

                ConfigurationHandler handler = new ConfigurationHandler("settings.config");

                assertNotNull("Configuration should be available", handler.getConfig());
                assertTrue("Configuration directory should be created", Files.isDirectory(customHome));
                assertTrue("Configuration file should be copied", Files.exists(customHome.resolve("settings.config")));
        }
}
