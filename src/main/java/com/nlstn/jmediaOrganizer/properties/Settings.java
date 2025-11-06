package com.nlstn.jmediaOrganizer.properties;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nlstn.jmediaOrganizer.processing.Pattern;

/**
 * This class is used to control settings. Settings are being saved to a file under C:\Users\User\AppData\Roaming\JMediaOrganizer.<br>
 * <br>
 * Creation: 09.12.2017<br>
 *
 * @author Niklas Lahnstein
 */
public class Settings {

    private static final Logger LOG = LogManager.getLogger(Settings.class);

    /**
     * The actual properties
     */
    private static ConfigurationHandler config;

    /**
     * Tries to find the file AppData\Roaming\JMediaOrganizer\settings.config.<br>
     * If the file was found, the settings in it are being loaded.<br>
     * If not, a new file will be created and filled with default values.
     */
    public static void loadSettings() {
        LOG.info("Loading settings...");
        config = new ConfigurationHandler("settings.config");
    }

    /**
     * Saves all settings to the settings file
     */
    public static void save() {
        config.save();
        LOG.info("Settings saved");
    }

    public static List<String> getInvalidTypes() {
        String[] configured = config.getConfig().getStringArray("invalidTypes");
        if (configured == null || configured.length == 0) {
            return List.of();
        }
        return Arrays.stream(configured)
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toUnmodifiableList());
    }

    public static void setInvalidTypes(List<String> invalidTypes) {
        List<String> sanitized = invalidTypes.stream()
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
        config.getConfig().setProperty("invalidTypes", sanitized.toArray(String[]::new));
    }

    public static boolean getID3ToNameEnabled() {
        return config.getConfig().getBoolean("id3ToNameEnabled", false);
    }

    public static void setID3ToNameEnabled(boolean enabled) {
        config.getConfig().setProperty("id3ToNameEnabled", enabled);
    }

    public static Pattern getID3ToNamePattern() {
        return new Pattern(config.getConfig().getString("id3ToNamePattern", ""));
    }

    public static void setID3ToNamePattern(Pattern pattern) {
        config.getConfig().setProperty("id3ToNamePattern", pattern);
    }

    public static String getOutputFolder() {
        return config.getConfig().getString("outputFolder", "");
    }

    public static void setOutputFolder(String outputFolder) {
        config.getConfig().setProperty("outputFolder", outputFolder);
    }

    public static int getThreadCount() {
        return config.getConfig().getInt("threadCount", 1);
    }

    public static void setThreadCount(int threadCount) {
        config.getConfig().setProperty("threadCount", threadCount);
    }

    public static void setStandardDirectoryChooserFolder(String folder) {
        config.getConfig().setProperty("standardDirectoryChooserFolder", folder);
    }

    public static String getStandardDirectoryChooserFolder() {
        return config.getConfig().getString("standardDirectoryChooserFolder", "");
    }

    public static boolean getDeleteRootFolder() {
        return config.getConfig().getBoolean("deleteRootFolder", false);
    }

    public static void setDeleteRootFolder(boolean value) {
        config.getConfig().setProperty("deleteRootFolder", String.valueOf(value));
    }
}
