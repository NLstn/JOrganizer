package com.nlstn.jmediaOrganizer.gui.settings;

import javax.swing.JPanel;

public abstract class SettingsPanel extends JPanel {

	private static final long serialVersionUID = -874500939221169844L;

	public abstract void loadSettings();

	public abstract void saveSettings();

}
