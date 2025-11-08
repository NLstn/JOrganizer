package com.nlstn.jmediaOrganizer.processing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

class PatternTest {

        @Test
        void getUsedVariablesFiltersUnknownPlaceholder() {
                Pattern pattern = new Pattern("%artist%-%unknown%-%title%");

                List<ConverterVariable> usedVariables = pattern.getUsedVariables();

                List<String> variableNames = usedVariables.stream().map(ConverterVariable::getVariable).collect(Collectors.toList());

                assertEquals(2, usedVariables.size());
                assertEquals("%artist%", variableNames.get(0));
                assertEquals("%title%", variableNames.get(1));
                assertFalse(variableNames.contains(null));
        }

        @Test
        void getUsedVariablesCachesAfterFirstAccess() {
                Pattern pattern = new Pattern("%artist%");

                List<ConverterVariable> firstCall = pattern.getUsedVariables();
                List<ConverterVariable> secondCall = pattern.getUsedVariables();

                assertSame(firstCall, secondCall);
        }
}
