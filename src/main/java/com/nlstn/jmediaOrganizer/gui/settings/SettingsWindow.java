package com.nlstn.jmediaOrganizer.gui.settings;

import java.awt.CardLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import com.nlstn.jmediaOrganizer.Settings;

/**
 * The SettingsWindow uses a CardLayout to display different settings panes<br>
 * <br>
 * Creation: 10.12.2017
 *
 * @author Niklas Lahnstein
 */
public class SettingsWindow {

	/**
	 * The window
	 */
	private JDialog				dialog;

	/**
	 * A list of all settings panels
	 */
	private List<SettingsPanel>	settingsPanels;

	/**
	 * The main panel, in which the card will be displayed
	 */
	private JPanel				mainPanel;

	/**
	 * Creates the SettingsWindow on top of the given JFrame
	 * 
	 * @param mainFrame
	 *            The underlying JFrame
	 */
	public SettingsWindow(JFrame mainFrame) {
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

	/**
	 * Registers a new SettingsPanel
	 * 
	 * @param panel
	 * @param identifier
	 */
	private void addSettingsPanel(SettingsPanel panel, String identifier) {
		mainPanel.add(panel, identifier);
		settingsPanels.add(panel);
	}

	/**
	 * Tells all SettingsPanels to load their settings from the settings file
	 */
	private void load() {
		Settings.loadSettings();
		for (SettingsPanel panel : settingsPanels) {
			panel.loadSettings();
		}
	}

	/**
	 * Tells all SettingsPanels to save their settings to the settings file
	 */
	private void save() {
		for (SettingsPanel panel : settingsPanels) {
			panel.saveSettings();
		}
		Settings.save();
	}

}
