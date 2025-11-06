package com.nlstn.jmediaOrganizer;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.After;
import org.junit.Test;

public class HeadlessCliTest {

        @After
        public void resetFactory() {
                JMediaOrganizer.resetHeadlessHandlerFactory();
        }

        @Test
        public void mainInvokesHeadlessHandlerWhenHeadlessFlagPresent() throws IOException {
                Path tempDir = Files.createTempDirectory("jmediaOrganizerHeadlessTest");
                AtomicBoolean invoked = new AtomicBoolean(false);

                JMediaOrganizer.setHeadlessHandlerFactory(() -> new HeadlessHandler() {
                        @Override
                        public void run() {
                                invoked.set(true);
                        }
                });

                try {
                        JMediaOrganizer.main(new String[] { "-h", "-i", tempDir.toString() });
                }
                finally {
                        Files.deleteIfExists(tempDir);
                }

                assertTrue("Headless handler should be invoked when -h flag is used", invoked.get());
        }
}
