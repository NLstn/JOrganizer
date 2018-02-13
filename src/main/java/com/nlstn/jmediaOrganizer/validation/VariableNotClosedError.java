package com.nlstn.jmediaOrganizer.validation;

/**
 * Creation: 13 Feb 2018
 *
 * @author Niklas Lahnstein
 */
public class VariableNotClosedError extends ValidationError {

	/**
	 * @param index
	 */
	public VariableNotClosedError(int index) {
		super(index);
	}

	public String toString() {
		return "Open '%' at " + index;
	}

}
