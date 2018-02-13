package com.nlstn.jmediaOrganizer.validation;

/**
 * Creation: 13 Feb 2018
 *
 * @author Niklas Lahnstein
 */
public class ValidationError {

	/**
	 * Index in string, where the error happended
	 */
	protected int index;

	public ValidationError(int index) {
		this.index = index;
	}

	public int getIndex() {
		return index;
	}

}
