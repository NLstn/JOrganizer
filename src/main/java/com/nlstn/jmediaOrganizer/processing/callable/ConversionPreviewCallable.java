package com.nlstn.jmediaOrganizer.processing.callable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

import com.nlstn.jmediaOrganizer.files.MP3File;
import com.nlstn.jmediaOrganizer.processing.Converter;

public class ConversionPreviewCallable implements Callable<List<String>> {

    private final List<File> files;
    private final List<String> invalidTypes;
    private final AtomicInteger progress = new AtomicInteger();

    public ConversionPreviewCallable(List<File> files, List<String> invalidTypes) {
        this.files = List.copyOf(files);
        this.invalidTypes = List.copyOf(invalidTypes);
    }

    @Override
    public List<String> call() {
        List<String> result = new ArrayList<>();
        for (File file : files) {
            MP3File mp3File = new MP3File(file);
            if (!mp3File.isOfType(invalidTypes) && mp3File.loadMp3Data()) {
                result.add(Converter.getNewPath(mp3File));
            }
            progress.incrementAndGet();
        }
        return result;
    }

    public int getProgress() {
        return progress.get();
    }
}
