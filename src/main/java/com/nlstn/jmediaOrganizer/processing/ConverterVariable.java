package com.nlstn.jmediaOrganizer.processing;

public class ConverterVariable {

	private String	displayName;
	private String	variable;

	public ConverterVariable(String displayName, String variable) {
		this.displayName = displayName;
		this.variable = variable;
	}

	public String getDisplayName() {
		return displayName;
	}

	public String getVariable() {
		return variable;
	}

	public String toString() {
		return displayName;
	}
}
