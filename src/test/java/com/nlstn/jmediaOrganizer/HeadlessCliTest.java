package com.nlstn.jmediaOrganizer;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class HeadlessCliTest {

        @AfterEach
        void resetFactory() {
                JMediaOrganizer.resetHeadlessHandlerFactory();
        }

        @Test
        void mainInvokesHeadlessHandlerWhenHeadlessFlagPresent(@TempDir Path tempDir) {
                AtomicBoolean invoked = new AtomicBoolean(false);

                JMediaOrganizer.setHeadlessHandlerFactory(() -> new HeadlessHandler() {
                        @Override
                        public void run() {
                                invoked.set(true);
                        }
                });

                JMediaOrganizer.main(new String[] { "-h", "-i", tempDir.toString() });

                assertTrue(invoked.get(), "Headless handler should be invoked when -h flag is used");
        }
}
