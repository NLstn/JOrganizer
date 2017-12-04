package com.nlstn.jmediaOrganizer.gui;

import java.awt.Component;
import java.io.File;

import javax.swing.JFileChooser;

import li.flor.nativejfilechooser.NativeJFileChooser;

public class DirectoryChooser {

	private JFileChooser fileChooser;

	public DirectoryChooser(Component parent) {
		fileChooser = new NativeJFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileChooser.showOpenDialog(parent);
	}

	public File getSelectedDirectory() {
		return fileChooser.getSelectedFile();
	}

}
