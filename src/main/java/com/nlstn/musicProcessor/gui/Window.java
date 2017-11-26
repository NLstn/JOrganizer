package com.nlstn.musicProcessor.gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileFilter;

import com.nlstn.musicProcessor.FileProcessor;

import li.flor.nativejfilechooser.NativeJFileChooser;

public class Window {

	private int			width			= 1200;
	private int			height			= width / 16 * 9;	// 387

	private File		inputFolder		= null;
	private File		outputFolder	= null;

	private JFrame		frame;

	private JTextArea	oldValues;
	private JTextArea	newValues;

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

		JMenuBar menubar = new JMenuBar();

		JMenu file = new JMenu("File");
		JMenuItem itmLoad = new JMenuItem("Open");
		itmLoad.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		itmLoad.addActionListener((ActionEvent e) -> onChooseInputFolder());
		file.add(itmLoad);

		JMenuItem itmUnload = new JMenuItem("Unload Folder");
		itmUnload.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U, ActionEvent.CTRL_MASK));
		itmUnload.addActionListener((ActionEvent e) -> {
			outputFolder = null;
			oldValues.setText("");
			FileProcessor.clearCurrentFiles();
		});
		file.add(itmUnload);

		JMenuItem itmSettings = new JMenuItem("Settings");
		itmSettings.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));

		menubar.add(file);

		JMenu action = new JMenu("Action");

		JMenuItem itmPrevConvert = new JMenuItem("Preview Conversion");
		itmPrevConvert.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK | ActionEvent.SHIFT_MASK));
		itmPrevConvert.addActionListener((ActionEvent e) -> getConversionPreview());
		action.add(itmPrevConvert);

		JMenuItem itmConvert = new JMenuItem("Convert Files");
		itmConvert.addActionListener((ActionEvent e) -> {
			FileProcessor.convertFiles();
			FileProcessor.cleanFolder(inputFolder);
		});
		action.add(itmConvert);

		JMenuItem itmClean = new JMenuItem("Clean Folder");
		itmClean.addActionListener((ActionEvent e) -> FileProcessor.cleanFolder(inputFolder));
		action.add(itmClean);

		menubar.add(action);

		frame.setJMenuBar(menubar);

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 10, 550, 30, 20, 30, 550, 10 };
		gridBagLayout.rowHeights = new int[] { 10, 317, 50, 10 };
		gridBagLayout.columnWeights = new double[] { 0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
		frame.getContentPane().setLayout(gridBagLayout);

		oldValues = new JTextArea();
		oldValues.setEditable(false);
		JScrollPane oldScrollPane = new JScrollPane(oldValues);
		oldScrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK));

		// oldValues.setColumnModel();

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

		JButton convert = new JButton("Convert!");

		GridBagConstraints gbcConvert = new GridBagConstraints();
		gbcConvert.gridx = 2;
		gbcConvert.gridwidth = 3;
		gbcConvert.gridy = 2;

		frame.getContentPane().add(convert, gbcConvert);

		frame.setVisible(true);
	}

	public void onChooseInputFolder() {
		JFileChooser fileChooser = new NativeJFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileChooser.setFileFilter(new FolderFilter());
		fileChooser.showOpenDialog(frame);

		inputFolder = fileChooser.getSelectedFile();
		reloadInputFolder();
	}

	private void getConversionPreview() {
		if (!FileProcessor.isFolderLoaded()) {
			JOptionPane.showMessageDialog(frame, "Please choose a folder!");
			return;
		}
		List<String> files = FileProcessor.getConversionPreview();
		StringBuilder builder = new StringBuilder();
		for (String file : files)
			builder.append(file).append("\n");
		newValues.setText(builder.toString());
	}

	private void reloadInputFolder() {
		List<File> files = FileProcessor.loadAllFiles(inputFolder);
		StringBuilder builder = new StringBuilder();
		for (File file : files) {
			builder.append(file.getAbsolutePath()).append("\n");
		}
		oldValues.setText(builder.toString());
	}

	private static class FolderFilter extends FileFilter {

		public boolean accept(File f) {
			return f.isDirectory();
		}

		public String getDescription() {
			return "Folder";
		}
	}

	public static void main(String[] args) {
		new Window();
	}
}
