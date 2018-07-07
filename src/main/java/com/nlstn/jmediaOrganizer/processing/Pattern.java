package com.nlstn.jmediaOrganizer.processing;

import java.util.ArrayList;
import java.util.List;

/**
 * Creation: 06.07.2018
 *
 * @author Niklas Lahnstein
 */
public class Pattern {

	private List<ConverterVariable>	variables	= new ArrayList<ConverterVariable>();

	private String					pattern;

	public Pattern(String pattern) {
		this.pattern = pattern;
	}

	public List<ConverterVariable> getUsedVariables() {
		if (variables == null) {
			variables = new ArrayList<ConverterVariable>();
			char[] chars = pattern.toCharArray();
			int beginIndex = -1;

			for (int i = 0; i < chars.length; i++) {
				if (chars[i] == '%') {
					if (beginIndex == -1)
						beginIndex = i;
					else {
						String variable = new String(chars, beginIndex, i + 1 - beginIndex);
						variables.add(Converter.getVariables().stream().filter(e -> e.getVariable().equals(variable)).findFirst().orElse(null));
						beginIndex = -1;
					}
				}
			}
		}
		return variables;
	}

	public String toString() {
		return pattern;
	}
}
