package com.nlstn.jmediaOrganizer.gui.settings.panels;

import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import com.nlstn.jmediaOrganizer.gui.DirectoryChooser;
import com.nlstn.jmediaOrganizer.gui.settings.SettingsPanel;
import com.nlstn.jmediaOrganizer.properties.Settings;

public class MainSettingsPanel extends SettingsPanel {

	private static final long			serialVersionUID	= 6380112269593540681L;

	private JTextField					outputFolder;
	private JComboBox<Integer>			threadCount;
	private DefaultListModel<String>	model;
	private JTextField					field;
	private List<String>				invalidTypes;
	private JList<String>				invalidTypeList;

	private JTextField					standardOpenFolder;
	private JCheckBox					deleteRootFolder;

	public MainSettingsPanel() {
		setLayout(null);

		invalidTypes = new ArrayList<String>();

		JLabel lblOutputLabel = new JLabel("Output Folder:");
		lblOutputLabel.setBounds(10, 10, 80, 28);
		add(lblOutputLabel);

		outputFolder = new JTextField();
		outputFolder.setBounds(90, 10, 320, 28);
		add(outputFolder);

		JButton openFolder = new JButton("Open Folder");
		openFolder.addActionListener((ActionEvent e) -> onOpenDirectoryChooser(0));
		openFolder.setBounds(420, 10, 150, 28);
		add(openFolder);

		JLabel lblThreadCount = new JLabel("Threadcount:");
		lblThreadCount.setBounds(10, 48, 80, 28);
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

		JLabel lblInvalidTypes = new JLabel("Invalid Types:");
		lblInvalidTypes.setBounds(10, 80, 80, 28);
		add(lblInvalidTypes);

		field = new JTextField();
		field.setBounds(90, 80, 80, 28);
		field.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
					addInvalidType(field.getText());
			}
		});
		add(field);

		model = new DefaultListModel<String>();
		invalidTypeList = new JList<String>(model);
		JScrollPane scrollPane = new JScrollPane(invalidTypeList);
		scrollPane.setBounds(90, 108, 80, 120);
		add(scrollPane);

		JButton deleteType = new JButton("Delete Type");
		deleteType.addActionListener((ActionEvent e) -> deleteInvalidType());
		deleteType.setBounds(175, 80, 120, 28);
		add(deleteType);

		JLabel lblStandardFolder = new JLabel("<html>Standard Open<br> Folder:</html>");
		lblStandardFolder.setBounds(10, 240, 80, 56);
		add(lblStandardFolder);

		standardOpenFolder = new JTextField();
		standardOpenFolder.setBounds(90, 240, 320, 28);
		add(standardOpenFolder);

		JButton standardOpenFolderButton = new JButton("Open Folder");
		standardOpenFolderButton.setBounds(420, 240, 150, 28);
		standardOpenFolderButton.addActionListener((ActionEvent e) -> onOpenDirectoryChooser(1));
		add(standardOpenFolderButton);

		JLabel lblDeleteRootFolder = new JLabel("<html>Delete root<br> folder:</html>");
		lblDeleteRootFolder.setBounds(10, 290, 80, 28);
		add(lblDeleteRootFolder);

		deleteRootFolder = new JCheckBox();
		deleteRootFolder.setBounds(90, 290, 28, 28);
		add(deleteRootFolder);
	}

	public void loadSettings() {
		outputFolder.setText(Settings.getOutputFolder());
		threadCount.setSelectedItem(Settings.getThreadCount());
		for (String type : Settings.getInvalidTypes()) {
			model.addElement(type);
			invalidTypes.add(type);
		}
		standardOpenFolder.setText(Settings.getStandardDirectoryChooserFolder());
		deleteRootFolder.setSelected(Settings.getDeleteRootFolder());
	}

	public boolean saveSettings() {
		Settings.setOutputFolder(outputFolder.getText());
		Settings.setThreadCount((int) threadCount.getSelectedItem());
		Settings.setInvalidTypes(invalidTypes);
		Settings.setStandardDirectoryChooserFolder(standardOpenFolder.getText());
		Settings.setDeleteRootFolder(deleteRootFolder.isSelected());
		return true;
	}

	private void addInvalidType(String type) {
		if (type.contains(" "))
			showInvalidInputDialog("Invalid input!");
		if (!type.startsWith("."))
			type = "." + type;
		if (!invalidTypes.contains(type)) {
			invalidTypes.add(type);
			rebuildInvalidTypesList();
		}
		else {
			showInvalidInputDialog("Type is already in list!");
		}
		field.setText("");
	}

	private void deleteInvalidType() {
		int[] indices = invalidTypeList.getSelectedIndices();

		int index = indices.length - 1;

		while (index >= 0) { // also checking if the list is empty
			invalidTypes.remove(indices[index--]);
		}
		rebuildInvalidTypesList();
	}

	private void onOpenDirectoryChooser(int option) {
		DirectoryChooser chooser = new DirectoryChooser(getParent());
		File folder = chooser.getSelectedDirectory();
		if (folder == null)
			return;
		if (option == 0)
			outputFolder.setText(folder.getAbsolutePath());
		else
			if (option == 1)
				standardOpenFolder.setText(folder.getAbsolutePath());
	}

	private void showInvalidInputDialog(String message) {
		JOptionPane.showMessageDialog(this, message, "Warning", JOptionPane.WARNING_MESSAGE);
	}

	private void rebuildInvalidTypesList() { // model is cleared to maintain the alphabetical order of the list
		Collections.sort(invalidTypes);
		model.clear();
		for (String invalidType : invalidTypes) {
			model.addElement(invalidType);
		}
	}
}
