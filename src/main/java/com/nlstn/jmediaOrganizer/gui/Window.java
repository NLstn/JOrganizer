package com.nlstn.jmediaOrganizer.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.nlstn.jmediaOrganizer.JMediaOrganizer;
import com.nlstn.jmediaOrganizer.gui.settings.SettingsWindow;
import com.nlstn.jmediaOrganizer.processing.FileProcessor;

/**
 * This class represents the main window.<br>
 * <br>
 * The MainWindow shows the files in the current input folder and, if already created, a preview of the files after the conversion.<br>
 * The menu bar has 3 entries:
 * <ul>
 * <li>File</li>
 * <li>Action</li>
 * <li>Converters</li>
 * </ul>
 * 
 * Creation: 04.12.2017<br>
 *
 * @author Niklas Lahnstein
 */
public class Window {

	public static final int	BUTTON_WIDTH	= 150;

	/**
	 * The initial width of the window, which is {@value}.
	 */
	private final int		width			= 1200;

	/**
	 * The initial height of the window, which is {@value}<br>
	 * (16:9 ratio to {@link #width}).
	 */
	private final int		height			= width / 16 * 9;	// 387

	private JFrame			frame;

	private JTextArea		oldValues;
	private JTextArea		newValues;

	public Window() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		frame = new JFrame("MusicProcessor");
		frame.setSize(width, height);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setMinimumSize(new Dimension(800, 800 / 16 * 9));

		JMenuBar menuBar = createMenuBar();

		frame.setJMenuBar(menuBar);

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 10, 550, 30, 20, 30, 550, 10 };
		gridBagLayout.rowHeights = new int[] { 10, 317, 50, 10 };
		gridBagLayout.columnWeights = new double[] { 0.0, 0.495, 0.0, 0.01, 0.0, 0.495, 0.0 };
		gridBagLayout.rowWeights = new double[] { 0.0, 1.0, 0.0, 0.0 };
		frame.getContentPane().setLayout(gridBagLayout);

		oldValues = new JTextArea();
		oldValues.setEditable(false);
		JScrollPane oldScrollPane = new JScrollPane(oldValues);
		oldScrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK));

		GridBagConstraints gbcOldValues = new GridBagConstraints();
		gbcOldValues.gridx = 1;
		gbcOldValues.gridwidth = 2;
		gbcOldValues.gridy = 1;
		gbcOldValues.fill = GridBagConstraints.BOTH;
		frame.getContentPane().add(oldScrollPane, gbcOldValues);

		newValues = new JTextArea();
		newValues.setEditable(false);
		JScrollPane newScrollPane = new JScrollPane(newValues);
		newScrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK));

		GridBagConstraints gbcNewValues = new GridBagConstraints();
		gbcNewValues.gridx = 4;
		gbcNewValues.gridwidth = 2;
		gbcNewValues.gridy = 1;
		gbcNewValues.fill = GridBagConstraints.BOTH;
		frame.getContentPane().add(newScrollPane, gbcNewValues);

		frame.setVisible(true);
	}

	/**
	 * Creates the menu bar for the main window.
	 * 
	 * @return The menu bar for the main window
	 */
	private JMenuBar createMenuBar() {
		JMenuBar menuBar = new JMenuBar();

		JMenu file = new JMenu("File");
		JMenuItem itmLoad = new JMenuItem("Open");
		itmLoad.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		itmLoad.addActionListener((ActionEvent e) -> onChooseInputFolder());
		file.add(itmLoad);

		JMenuItem itmUnload = new JMenuItem("Unload Folder");
		itmUnload.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U, ActionEvent.CTRL_MASK));
		itmUnload.addActionListener((ActionEvent e) -> {
			JMediaOrganizer.setInputFolder(null);
			oldValues.setText("");
			FileProcessor.clearCurrentFiles();
		});
		file.add(itmUnload);

		JMenuItem itmSettings = new JMenuItem("Settings");
		itmSettings.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));
		itmSettings.addActionListener((ActionEvent e) -> {
			new SettingsWindow(frame);
		});
		file.add(itmSettings);

		menuBar.add(file);

		JMenu action = new JMenu("Action");

		JMenuItem itmPrevConvert = new JMenuItem("Preview Conversion");
		itmPrevConvert.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK | ActionEvent.SHIFT_MASK));
		itmPrevConvert.addActionListener((ActionEvent e) -> SwingUtilities.invokeLater(() -> getConversionPreview()));
		action.add(itmPrevConvert);

		JMenuItem itmConvert = new JMenuItem("Convert Files");
		itmConvert.addActionListener((ActionEvent e) -> {
			if (!checkInputFolderLoaded())
				return;
			FileProcessor.convertFiles();
			FileProcessor.cleanInputFolder();
		});
		action.add(itmConvert);

		JMenuItem itmClean = new JMenuItem("Clean Folder");
		itmClean.addActionListener((ActionEvent e) -> {
			if (!checkInputFolderLoaded())
				return;
			FileProcessor.cleanInputFolder();
		});
		action.add(itmClean);

		menuBar.add(action);

		return menuBar;
	}

	public void onChooseInputFolder() {
		DirectoryChooser chooser = new DirectoryChooser(frame);
		File folder = chooser.getSelectedDirectory();
		if (folder == null)
			return;
		JMediaOrganizer.setInputFolder(folder);
		reloadInputFolder();
	}

	private void getConversionPreview() {
		if (!checkInputFolderLoaded())
			return;
		List<String> files = FileProcessor.getConversionPreview();
		StringBuilder builder = new StringBuilder();
		for (String file : files)
			builder.append(file).append("\n");
		newValues.setText(builder.toString());
	}

	private boolean checkInputFolderLoaded() {
		if (!FileProcessor.isFolderLoaded()) {
			JOptionPane.showMessageDialog(frame, "Please choose a folder!");
			return false;
		}
		return true;
	}

	private void reloadInputFolder() {
		List<File> files = FileProcessor.loadAllFiles();
		StringBuilder builder = new StringBuilder();
		for (File file : files) {
			builder.append(file.getAbsolutePath()).append("\n");
		}
		oldValues.setText(builder.toString());
	}

	public JFrame getFrame() {
		return frame;
	}
}
