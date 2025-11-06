package com.nlstn.jmediaOrganizer.processing.callable;

import java.io.File;
import java.util.List;
import java.util.concurrent.Callable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nlstn.jmediaOrganizer.files.MP3File;
import com.nlstn.jmediaOrganizer.processing.Converter;

/**
 * Creation: 6 Jan 2018
 *
 * @author Niklas Lahnstein
 */
public class ConversionCallable implements Callable<Boolean> {

    private static final Logger LOG = LogManager.getLogger(ConversionCallable.class);

    private final List<File> files;
    private final List<String> invalidTypes;

    public ConversionCallable(List<File> files, List<String> invalidTypes) {
        this.files = List.copyOf(files);
        this.invalidTypes = List.copyOf(invalidTypes);
    }

    @Override
    public Boolean call() {
        boolean success = true;
        for (File file : files) {
            MP3File mp3File = new MP3File(file);
            if (mp3File.deleteIfOfType(invalidTypes)) {
                continue;
            }
            if (mp3File.loadMp3Data()) {
                if (mp3File.moveToLocation(Converter.getNewPath(mp3File))) {
                    if (!file.delete()) {
                        LOG.warn("Failed to delete relocated file: {}", file.getAbsolutePath());
                    }
                }
                else {
                    success = false;
                }
            }
        }
        return Boolean.valueOf(success);
    }
}
