package com.nlstn.jmediaOrganizer.gui.settings.panels;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
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
                setLayout(new GridBagLayout());

                invalidTypes = new ArrayList<String>();

                JLabel lblOutputLabel = new JLabel("Output Folder:");
                GridBagConstraints gbc = createGbc(0, 0);
                add(lblOutputLabel, gbc);

                outputFolder = new JTextField();
                gbc = createGbc(1, 0);
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.weightx = 1.0;
                add(outputFolder, gbc);

                JButton openFolder = new JButton("Open Folder");
                openFolder.addActionListener((ActionEvent e) -> onOpenDirectoryChooser(0));
                gbc = createGbc(2, 0);
                add(openFolder, gbc);

                JLabel lblThreadCount = new JLabel("Threadcount:");
                gbc = createGbc(0, 1);
                add(lblThreadCount, gbc);

                int cores = Runtime.getRuntime().availableProcessors();
                List<Integer> values = new ArrayList<Integer>();
                for (int i = 1; i <= cores; i++) {
                        values.add(i);
                }
                Integer[] valuesArray = new Integer[cores];
                values.toArray(valuesArray);

                threadCount = new JComboBox<Integer>(valuesArray);
                gbc = createGbc(1, 1);
                add(threadCount, gbc);

                JLabel lblInvalidTypes = new JLabel("Invalid Types:");
                gbc = createGbc(0, 2);
                add(lblInvalidTypes, gbc);

                field = new JTextField();
                field.addKeyListener(new KeyAdapter() {
                        public void keyReleased(KeyEvent e) {
                                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                                        addInvalidType(field.getText());
                        }
                });
                gbc = createGbc(1, 2);
                gbc.fill = GridBagConstraints.HORIZONTAL;
                add(field, gbc);

                model = new DefaultListModel<String>();
                invalidTypeList = new JList<String>(model);
                JScrollPane scrollPane = new JScrollPane(invalidTypeList);
                gbc = createGbc(1, 3);
                gbc.fill = GridBagConstraints.BOTH;
                gbc.weightx = 1.0;
                gbc.weighty = 1.0;
                add(scrollPane, gbc);

                JButton deleteType = new JButton("Delete Type");
                deleteType.addActionListener((ActionEvent e) -> deleteInvalidType());
                gbc = createGbc(2, 3);
                gbc.anchor = GridBagConstraints.NORTHWEST;
                add(deleteType, gbc);

                JLabel lblStandardFolder = new JLabel("<html>Standard Open<br> Folder:</html>");
                gbc = createGbc(0, 4);
                add(lblStandardFolder, gbc);

                standardOpenFolder = new JTextField();
                gbc = createGbc(1, 4);
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.weightx = 1.0;
                add(standardOpenFolder, gbc);

                JButton standardOpenFolderButton = new JButton("Open Folder");
                standardOpenFolderButton.addActionListener((ActionEvent e) -> onOpenDirectoryChooser(1));
                gbc = createGbc(2, 4);
                add(standardOpenFolderButton, gbc);

                JLabel lblDeleteRootFolder = new JLabel("<html>Delete root<br> folder:</html>");
                gbc = createGbc(0, 5);
                add(lblDeleteRootFolder, gbc);

                deleteRootFolder = new JCheckBox();
                gbc = createGbc(1, 5);
                add(deleteRootFolder, gbc);
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

	public void reload() {

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

        private GridBagConstraints createGbc(int x, int y) {
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.gridx = x;
                gbc.gridy = y;
                gbc.insets = new Insets(5, 5, 5, 5);
                gbc.anchor = GridBagConstraints.WEST;
                return gbc;
        }
}
