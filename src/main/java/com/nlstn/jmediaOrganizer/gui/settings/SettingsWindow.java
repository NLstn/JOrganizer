package com.nlstn.jmediaOrganizer.gui.settings;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
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

                dialog.setLayout(new BorderLayout());
		dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		dialog.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				save();
			}
		});

		
		CardLayout layout = new CardLayout();
                mainPanel = new JPanel(layout);

		MainSettingsPanel mainSettingsPanel = new MainSettingsPanel();
		addSettingsPanel("General", mainSettingsPanel);

		ConverterSettingsPanel converterSettingsPanel = new ConverterSettingsPanel();
		addSettingsPanel("Converter", converterSettingsPanel);

                layout.show(mainPanel, "General");

                SettingsPanelTree tree = new SettingsPanelTree();

                JScrollPane treeScrollPane = new JScrollPane(tree);
		
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
				String selectedPanelName = e.getNewLeadSelectionPath().getLastPathComponent().toString();
				SettingsPanel selectedPanel = settingsPanels.get(selectedPanelName);
				selectedPanel.reload();
				layout.show(mainPanel, selectedPanelName);
			}
			
		});
                JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treeScrollPane, mainPanel);
                splitPane.setDividerLocation(220);
                splitPane.setResizeWeight(0.25);
                splitPane.setContinuousLayout(true);
                splitPane.setBorder(null);
                dialog.getContentPane().add(splitPane, BorderLayout.CENTER);

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
