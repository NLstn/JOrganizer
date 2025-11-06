package com.nlstn.jmediaOrganizer.processing;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nlstn.jmediaOrganizer.files.MP3File;
import com.nlstn.jmediaOrganizer.properties.Settings;

/**
 * Converter, to set the new path + filename of the file after processing.<br>
 * The converter loads the user defined pattern and replaces the variables in this <br>
 * pattern with the information from the file.<br>
 * <br>
 * Creation: 09.12.2017<br>
 *
 * @author Niklas Lahnstein
 */
public final class Converter {

    private static final Logger LOG = LogManager.getLogger(Converter.class);

    private static final List<ConverterVariable> AVAILABLE_VARIABLES;
    private static final Map<String, ConverterVariable> VARIABLE_BY_TOKEN;

    static {
        List<ConverterVariable> variables = List.of(
                new ConverterVariable("Album", "%album%", MP3File::getAlbum),
                new ConverterVariable("Album Artist", "%albumArtist%", MP3File::getAlbumArtist),
                new ConverterVariable("Artist", "%artist%", MP3File::getArtist),
                new ConverterVariable("BPM", "%bpm%", file -> safeToString(file.getBPM())),
                new ConverterVariable("Composer", "%composer%", MP3File::getComposer),
                new ConverterVariable("Date", "%date%", MP3File::getDate),
                new ConverterVariable("File Extension", "%extension%", MP3File::getExtension),
                new ConverterVariable("Genre", "%genre%", MP3File::getGenre),
                new ConverterVariable("Length", "%length%", MP3File::getLength),
                new ConverterVariable("Output Folder", "%output%", file -> safeToString(Settings.getOutputFolder())),
                new ConverterVariable("Title", "%title%", MP3File::getTitle),
                new ConverterVariable("Track Nr", "%track%", MP3File::getTrack),
                new ConverterVariable("Year", "%year%", MP3File::getYear));

        AVAILABLE_VARIABLES = variables.stream()
                .sorted(Comparator.comparing(ConverterVariable::getDisplayName, String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toUnmodifiableList());

        VARIABLE_BY_TOKEN = AVAILABLE_VARIABLES.stream()
                .collect(Collectors.toUnmodifiableMap(ConverterVariable::getVariable, Function.identity()));
    }

    private Converter() {
    }

    /**
     * Replaces all variables in the pattern with the values from the file.
     *
     * @param file
     *            The file to take the information of
     * @return The new path for this file
     */
    public static String getNewPath(MP3File file) {
        return getNewPath(file, Settings.getID3ToNamePattern());
    }

    public static String getNewPath(MP3File file, Pattern pattern) {
        Objects.requireNonNull(file, "file");
        Objects.requireNonNull(pattern, "pattern");

        String result = pattern.toString();
        if (result == null || result.isEmpty()) {
            return "";
        }

        for (ConverterVariable variable : pattern.getUsedVariables()) {
            String replacement = safeToString(variable.getValue(file));
            result = result.replace(variable.getVariable(), replacement);
        }

        LOG.debug("Recalculated: {} to {}", file.getAbsolutePath(), result);
        return result;
    }

    public static List<ConverterVariable> getVariables() {
        return AVAILABLE_VARIABLES;
    }

    public static Optional<ConverterVariable> findVariable(String token) {
        return Optional.ofNullable(VARIABLE_BY_TOKEN.get(token));
    }

    private static String safeToString(Object value) {
        return value == null ? "" : value.toString();
    }
}
