package com.nlstn.jmediaOrganizer.validation;

import java.util.ArrayList;
import java.util.List;

import com.nlstn.jmediaOrganizer.processing.Converter;

/**
 * Creation: 13 Feb 2018
 *
 * @author Niklas Lahnstein
 */
public class PatternValidator {

	/**
	 * Returns a list of all unknown tags in the given pattern string,<br>
	 * e.g. "%ads% - %title%/%track% - %ablum%" returns { ads, ablum } as a list.
	 * 
	 * @param pattern
	 *            The pattern to check
	 * @return The unknown tags
	 */
	public static List<ValidationError> validate(String pattern) {
		List<ValidationError> errors = new ArrayList<ValidationError>();
		char[] chars = new char[pattern.length()];
		pattern.getChars(0, pattern.length(), chars, 0);

		int beginIndex = -1;

		for (int i = 0; i < chars.length; i++) {
			if (chars[i] == '%') {
				if (beginIndex == -1)
					beginIndex = i;
				else {
					String variable = new String(chars, beginIndex, i + 1 - beginIndex);
					if (!Converter.getVariables().stream().anyMatch(e -> e.getVariable().equals(variable))) {
						errors.add(new UnknownVariableError(variable, beginIndex));
					}
					beginIndex = -1;
				}
			}
		}
		if (beginIndex != -1) {
			errors.add(new VariableNotClosedError(beginIndex));
		}
		return errors;
	}

}
