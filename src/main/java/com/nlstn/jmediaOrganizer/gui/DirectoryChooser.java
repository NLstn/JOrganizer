package com.nlstn.jmediaOrganizer.gui;

import java.awt.Component;
import java.io.File;

import javax.swing.JFileChooser;

import li.flor.nativejfilechooser.NativeJFileChooser;

/**
 * Wrapper class, opens a new NativeJFileChooser on top of the given component<br>
 * and returns the chosen files
 * 
 * Creation: 09.12.2017
 *
 * @author Niklas Lahnstein
 */
public class DirectoryChooser {

	/**
	 * JFileChooser reference
	 */
	private JFileChooser fileChooser;

	/**
	 * Opens a new NativeJFileChooser on top of the given component.<br>
	 * NativeJFileChooser uses JavaFx's file chooser, because it uses the standard windows file chooser.<br>
	 * @param parent The component, on top of which this dialog will be.
	 */
	public DirectoryChooser(Component parent) {
		fileChooser = new NativeJFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileChooser.showOpenDialog(parent);
	}

	/**
	 * Blocks until the user confirmed his selection, then returns the selected folder
	 * @return The selected folder
	 */
	public File getSelectedDirectory() {
		return fileChooser.getSelectedFile();
	}

}
