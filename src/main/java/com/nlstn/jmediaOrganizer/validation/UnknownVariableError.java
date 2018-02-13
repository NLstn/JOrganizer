package com.nlstn.jmediaOrganizer.validation;

/**
 * Creation: 13 Feb 2018
 *
 * @author Niklas Lahnstein
 */
public class UnknownVariableError extends ValidationError {

	private String variable;

	/**
	 * @param index
	 */
	public UnknownVariableError(String variable, int index) {
		super(index);
		this.variable = variable;
	}

	public String toString() {
		return "Unknown Variable(" + index + "): " + variable;
	}
}
