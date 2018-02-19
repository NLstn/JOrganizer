package com.nlstn.jmediaOrganizer.gui.settings;

import java.awt.CardLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.JTree.DynamicUtilTreeNode;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import com.nlstn.jmediaOrganizer.gui.settings.panels.ConverterSettingsPanel;
import com.nlstn.jmediaOrganizer.gui.settings.panels.MainSettingsPanel;
import com.nlstn.jmediaOrganizer.properties.Settings;

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
	private JDialog						dialog;

	/**
	 * A list of all settings panels
	 */
	private Map<String, SettingsPanel>	settingsPanels;

	/**
	 * The main panel, in which the card will be displayed
	 */
	private JPanel						mainPanel;

	/**
	 * Creates the SettingsWindow on top of the given JFrame
	 * 
	 * @param mainFrame
	 *            The underlying JFrame
	 */
	public SettingsWindow(JFrame mainFrame) {
		settingsPanels = new HashMap<String, SettingsPanel>();
		dialog = new JDialog(mainFrame, "Settings", true);
		dialog.setSize(850, 550);
		dialog.setLocationRelativeTo(mainFrame);

		dialog.setLayout(null);
		dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		dialog.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				save();
			}
		});

		Hashtable<String, String> settingPanels = new Hashtable<String, String>();
		settingPanels.put("Settings", "settings");
		settingPanels.put("Converter", "converter");

		JTree tree = new JTree(settingPanels);
		tree.setBounds(10, 10, 200, 445);

		CardLayout layout = new CardLayout();
		mainPanel = new JPanel(layout);
		mainPanel.setBounds(220, 10, 620, 530);

		MainSettingsPanel mainSettingsPanel = new MainSettingsPanel();
		addSettingsPanel("settings", mainSettingsPanel);

		ConverterSettingsPanel converterSettingsPanel = new ConverterSettingsPanel();
		addSettingsPanel("converter", converterSettingsPanel);

		layout.show(mainPanel, "converter");

		dialog.getContentPane().add(mainPanel);

		tree.addTreeSelectionListener(new TreeSelectionListener() {

			public void valueChanged(TreeSelectionEvent e) {
				boolean successful = true;
				for (SettingsPanel panel : settingsPanels.values()) {
					if (!panel.saveSettings()) {
						successful = false;
					}
				}
				if (!successful)
					tree.setSelectionPath(e.getOldLeadSelectionPath());
				String selectedIdentifier = ((DynamicUtilTreeNode) tree.getLastSelectedPathComponent()).toString().toLowerCase(Locale.getDefault());
				SettingsPanel selectedPanel = settingsPanels.get(selectedIdentifier);
				selectedPanel.reload();
				layout.show(mainPanel, selectedIdentifier);
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
	private void addSettingsPanel(String identifier, SettingsPanel panel) {
		mainPanel.add(panel, identifier);
		settingsPanels.put(identifier, panel);
	}

	/**
	 * Tells all SettingsPanels to load their settings from the settings file
	 */
	private void load() {
		Settings.loadSettings();
		for (SettingsPanel panel : settingsPanels.values()) {
			panel.loadSettings();
		}
	}

	/**
	 * Tells all SettingsPanels to save their settings to the settings file
	 */
	private void save() {
		boolean successful = true;
		for (SettingsPanel panel : settingsPanels.values()) {
			if (!panel.saveSettings()) {
				successful = false;
			}
		}
		if (successful) {
			Settings.save();
			dialog.dispose();
		}
	}

}
