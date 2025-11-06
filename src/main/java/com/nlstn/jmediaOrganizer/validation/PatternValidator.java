package com.nlstn.jmediaOrganizer.validation;

import java.util.ArrayList;
import java.util.List;

import com.nlstn.jmediaOrganizer.processing.Converter;
import com.nlstn.jmediaOrganizer.processing.Pattern;
import com.nlstn.jmediaOrganizer.processing.Pattern.PatternToken;

/**
 * Creation: 13 Feb 2018
 *
 * @author Niklas Lahnstein
 */
public final class PatternValidator {

    private PatternValidator() {
    }

    /**
     * Returns a list of all unknown tags in the given pattern string,<br>
     * e.g. "%ads% - %title%/%track% - %ablum%" returns { ads, ablum } as a list.
     *
     * @param pattern
     *            The pattern to check
     * @return The unknown tags
     */
    public static List<ValidationError> validate(String pattern) {
        List<ValidationError> errors = new ArrayList<>();
        for (PatternToken token : Pattern.analyze(pattern)) {
            if (!token.closed()) {
                errors.add(new VariableNotClosedError(token.startIndex()));
                continue;
            }
            if (Converter.findVariable(token.token()).isEmpty()) {
                errors.add(new UnknownVariableError(token.token(), token.startIndex()));
            }
        }
        return errors;
    }
}
