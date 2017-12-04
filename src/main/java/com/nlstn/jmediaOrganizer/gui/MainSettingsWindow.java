package com.nlstn.jmediaOrganizer.gui;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import com.nlstn.jmediaOrganizer.Settings;
import com.nlstn.jmediaOrganizer.gui.settings.MainSettingsPanel;
import com.nlstn.jmediaOrganizer.gui.settings.SettingsPanel;

public class MainSettingsWindow {

	private JDialog				dialog;

	private List<SettingsPanel>	settingsPanels;

	private JPanel				mainPanel;

	public MainSettingsWindow(JFrame mainFrame) {
		settingsPanels = new ArrayList<SettingsPanel>();
		dialog = new JDialog(mainFrame, "Settings", true);
		dialog.setSize(850, 550);
		dialog.setLocationRelativeTo(mainFrame);

		dialog.setLayout(null);
		dialog.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				save();
			}
		});

		Hashtable<String, String> settingPanels = new Hashtable<String, String>();
		settingPanels.put("Settings", "main");
		settingPanels.put("Test", "test");
		settingPanels.put("Test2", "test2");

		JTree tree = new JTree(settingPanels);
		tree.setBounds(10, 10, 200, 445);

		JButton save = new JButton("Save");
		save.setBounds(40, 470, 140, 25);
		save.addActionListener((ActionEvent e) -> save());
		dialog.getContentPane().add(save);

		CardLayout layout = new CardLayout();
		mainPanel = new JPanel(layout);
		mainPanel.setBounds(220, 10, 620, 530);

		MainSettingsPanel mainSettingsPanel = new MainSettingsPanel();
		addSettingsPanel(mainSettingsPanel, "main");

		layout.show(mainPanel, "main");

		dialog.getContentPane().add(mainPanel);

		tree.addTreeSelectionListener(new TreeSelectionListener() {

			public void valueChanged(TreeSelectionEvent e) {
				layout.show(mainPanel, e.getPath().toString());
			}

		});

		dialog.getContentPane().add(tree);
		
		load();

		dialog.setVisible(true);
	}

	private void addSettingsPanel(SettingsPanel panel, String identifier) {
		mainPanel.add(panel, identifier);
		settingsPanels.add(panel);
	}

	private void load() {
		Settings.loadSettings();
		for (SettingsPanel panel : settingsPanels) {
			panel.loadSettings();
		}
	}

	private void save() {
		for (SettingsPanel panel : settingsPanels) {
			panel.saveSettings();
		}
		Settings.save();
	}

}
