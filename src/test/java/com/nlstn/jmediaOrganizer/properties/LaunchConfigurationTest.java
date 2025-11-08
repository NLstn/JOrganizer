package com.nlstn.jmediaOrganizer.properties;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class LaunchConfigurationTest {

        @Test
        public void parseThrowsWhenHeadlessInputMissing() {
                InvalidLaunchConfigurationException exception = assertThrows(
                                InvalidLaunchConfigurationException.class,
                                () -> LaunchConfiguration.parse(new String[] { "-h" }));

                assertTrue(exception.getMessage().contains("input folder"));
                assertNotNull(exception.getHelpText());
                assertFalse(exception.getHelpText().isBlank());
        }

        @Test
        public void parseThrowsWhenUnknownOptionSupplied() {
                InvalidLaunchConfigurationException exception = assertThrows(
                                InvalidLaunchConfigurationException.class,
                                () -> LaunchConfiguration.parse(new String[] { "--unknown" }));

                assertTrue(exception.getMessage().contains("Unrecognized option"));
                assertNotNull(exception.getHelpText());
                assertFalse(exception.getHelpText().isBlank());
        }
}
