package com.nlstn.jmediaOrganizer.processing;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Creation: 06.07.2018
 *
 * @author Niklas Lahnstein
 */
public class Pattern {

    private final String pattern;
    private List<ConverterVariable> variables;

    public Pattern(String pattern) {
        this.pattern = pattern == null ? "" : pattern;
    }

    public synchronized List<ConverterVariable> getUsedVariables() {
        if (variables == null) {
            variables = analyze(pattern).stream()
                    .filter(PatternToken::closed)
                    .map(PatternToken::token)
                    .map(Converter::findVariable)
                    .flatMap(Optional::stream)
                    .distinct()
                    .collect(Collectors.toUnmodifiableList());
        }
        return variables;
    }

    public static List<PatternToken> analyze(String pattern) {
        if (pattern == null || pattern.isEmpty()) {
            return List.of();
        }
        List<PatternToken> tokens = new ArrayList<>();
        int beginIndex = -1;
        for (int index = 0; index < pattern.length(); index++) {
            if (pattern.charAt(index) == '%') {
                if (beginIndex == -1) {
                    beginIndex = index;
                }
                else {
                    tokens.add(new PatternToken(pattern.substring(beginIndex, index + 1), beginIndex, true));
                    beginIndex = -1;
                }
            }
        }
        if (beginIndex != -1) {
            tokens.add(new PatternToken(pattern.substring(beginIndex), beginIndex, false));
        }
        return List.copyOf(tokens);
    }

    @Override
    public String toString() {
        return pattern;
    }

    public record PatternToken(String token, int startIndex, boolean closed) {
        public PatternToken {
            Objects.requireNonNull(token, "token");
        }
    }
}
