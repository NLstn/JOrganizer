package com.nlstn.jmediaOrganizer.processing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

public class PatternTest {

        @Test
        public void getUsedVariablesFiltersUnknownPlaceholder() {
                Pattern pattern = new Pattern("%artist%-%unknown%-%title%");

                List<ConverterVariable> usedVariables = pattern.getUsedVariables();

                List<String> variableNames = usedVariables.stream().map(ConverterVariable::getVariable).collect(Collectors.toList());

                assertEquals(2, usedVariables.size());
                assertEquals("%artist%", variableNames.get(0));
                assertEquals("%title%", variableNames.get(1));
                assertFalse(variableNames.contains(null));
        }

        @Test
        public void getUsedVariablesCachesAfterFirstAccess() {
                Pattern pattern = new Pattern("%artist%");

                List<ConverterVariable> firstCall = pattern.getUsedVariables();
                List<ConverterVariable> secondCall = pattern.getUsedVariables();

                assertSame(firstCall, secondCall);
        }
}
