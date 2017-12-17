package com.nlstn.jmediaOrganizer.gui.settings;

import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import com.nlstn.jmediaOrganizer.Settings;
import com.nlstn.jmediaOrganizer.gui.DirectoryChooser;

public class MainSettingsPanel extends SettingsPanel {

	private static final long	serialVersionUID	= 6380112269593540681L;

	private TextField			outputFolder;
	private JComboBox<Integer>	threadCount;

	public MainSettingsPanel() {
		setLayout(null);

		JLabel lblOutputLabel = new JLabel("Output Folder");
		lblOutputLabel.setBounds(10, 10, 70, 28);
		add(lblOutputLabel);

		outputFolder = new TextField();
		outputFolder.setBounds(90, 10, 320, 28);
		add(outputFolder);

		JButton openFolder = new JButton("Open Folder");
		openFolder.addActionListener((ActionEvent e) -> onOpenDirectoryChooser());
		openFolder.setBounds(420, 10, 150, 28);
		add(openFolder);

		JLabel lblThreadCount = new JLabel("Threadcount");
		lblThreadCount.setBounds(10, 48, 70, 28);
		add(lblThreadCount);

		int cores = Runtime.getRuntime().availableProcessors();
		List<Integer> values = new ArrayList<Integer>();
		for (int i = 1; i <= cores; i++) {
			values.add(i);
		}
		Integer[] valuesArray = new Integer[cores];
		values.toArray(valuesArray);

		threadCount = new JComboBox<Integer>(valuesArray);
		threadCount.setBounds(90, 48, 80, 28);
		add(threadCount);
	}

	public void onOpenDirectoryChooser() {
		DirectoryChooser chooser = new DirectoryChooser(getParent());
		File folder = chooser.getSelectedDirectory();
		if (folder != null)
			outputFolder.setText(folder.getAbsolutePath());
	}

	public void loadSettings() {
		outputFolder.setText(Settings.getOutputFolder());
		threadCount.setSelectedItem(Settings.getThreadCount());
	}

	public void saveSettings() {
		Settings.setOutputFolder(outputFolder.getText());
		Settings.setThreadCount((int) threadCount.getSelectedItem());
	}
}
