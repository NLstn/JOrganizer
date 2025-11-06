package com.nlstn.jmediaOrganizer.processing;

import java.util.Objects;
import java.util.function.Function;

import com.nlstn.jmediaOrganizer.files.MP3File;

public class ConverterVariable {

    private final String displayName;
    private final String variable;
    private final Function<MP3File, String> extractor;

    public ConverterVariable(String displayName, String variable, Function<MP3File, String> extractor) {
        this.displayName = Objects.requireNonNull(displayName, "displayName");
        this.variable = Objects.requireNonNull(variable, "variable");
        this.extractor = Objects.requireNonNull(extractor, "extractor");
    }

    public ConverterVariable(String displayName, String variable) {
        this(displayName, variable, file -> "");
    }

    public String getValue(MP3File file) {
        return extractor.apply(file);
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getVariable() {
        return variable;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof ConverterVariable other))
            return false;
        return Objects.equals(displayName, other.displayName) && Objects.equals(variable, other.variable);
    }

    @Override
    public int hashCode() {
        return Objects.hash(displayName, variable);
    }

    @Override
    public String toString() {
        return displayName;
    }
}
