package com.nlstn.jmediaOrganizer.gui.settings;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.nlstn.jmediaOrganizer.Settings;
import com.nlstn.jmediaOrganizer.processing.FileProcessor;
import com.nlstn.jmediaOrganizer.processing.MP3File;

public class ConverterSettingsPanel extends SettingsPanel {

	private static final long	serialVersionUID	= 5821559048829857180L;

	private JTextField			txtPattern;

	private JTextArea			lblExample;

	private JCheckBox			chkEnabled;

	private JScrollPane			scrollExample;

	private String				exampleArtist		= "Linkin Park";
	private String				exampleAlbum		= "A Thousand Suns";
	private String				exampleTrack		= "12";
	private String				exampleTitle		= "Iridescent";

	public ConverterSettingsPanel() {

		setLayout(null);

		chkEnabled = new JCheckBox();
		chkEnabled.setBounds(10, 7, 15, 15);
		chkEnabled.addActionListener((ActionEvent e) -> onToggleEnabled());
		add(chkEnabled);

		JLabel lblEnabled = new JLabel("Enabled");
		lblEnabled.setBounds(30, 7, 50, 15);
		add(lblEnabled);

		lblExample = new JTextArea();
		lblExample.setEditable(false);

		Border lineBorder = BorderFactory.createLineBorder(Color.BLACK);
		Border insets = new EmptyBorder(5, 5, 5, 5);

		scrollExample = new JScrollPane(lblExample);
		scrollExample.setBounds(10, 27, 580, 150);
		scrollExample.setBorder(new CompoundBorder(lineBorder, insets));
		add(scrollExample);

		JLabel lblPattern = new JLabel("Pattern:");
		lblPattern.setBounds(10, 197, 50, 28);
		add(lblPattern);

		txtPattern = new JTextField();
		txtPattern.setBounds(70, 197, 460, 28);
		txtPattern.getDocument().addDocumentListener(new DocumentListener() {

			public void insertUpdate(DocumentEvent e) {
				lblExample.setText(buildPreview());
				lblExample.setCaretPosition(0);
			}

			public void removeUpdate(DocumentEvent e) {
				lblExample.setText(buildPreview());
				lblExample.setCaretPosition(0);
			}

			public void changedUpdate(DocumentEvent e) {
				lblExample.setText(buildPreview());
				lblExample.setCaretPosition(0);
			}

		});
		add(txtPattern);

		MP3File preview = FileProcessor.getPreviewExample();
		if (preview != null)
			setPreviewData(preview.getTrack(), preview.getTitle(), preview.getArtist(), preview.getAlbum());

		loadSettings();

		lblExample.setText(buildPreview());
		lblExample.setCaretPosition(0);

		setVisible(true);
	}

	private void onToggleEnabled() {
		txtPattern.setEnabled(chkEnabled.isSelected());
		lblExample.setEnabled(chkEnabled.isSelected());
	}

	public void loadSettings() {
		chkEnabled.setSelected(Settings.getID3ToNameEnabled());
		txtPattern.setText(Settings.getID3ToNamePattern());
		onToggleEnabled();
	}

	public void saveSettings() {
		Settings.setID3ToNameEnabled(chkEnabled.isSelected());
		Settings.setID3ToNamePattern(txtPattern.getText());
		Settings.save();
	}

	public void setPreviewData(String track, String title, String artist, String album) {
		exampleTrack = track;
		exampleTitle = title;
		exampleArtist = artist;
		exampleAlbum = album;
	}

	private String buildPreview() {
		StringBuilder builder = new StringBuilder();
		builder.append("Track (%track%):\t").append(exampleTrack).append("\n");
		builder.append("Title (%title%):\t").append(exampleTitle).append("\n");
		builder.append("Artist (%artist%):\t").append(exampleArtist).append("\n");
		builder.append("Album (%album%):\t").append(exampleAlbum).append("\n");
		builder.append("\n");
		StringBuilder previewLine = new StringBuilder();
		previewLine.append("Preview:\n");
		if (txtPattern.getText() != null && !txtPattern.getText().equals("")) {
			String pattern = txtPattern.getText().replace("%track%", exampleTrack).replace("%title%", exampleTitle).replace("%artist%", exampleArtist).replace("%album%", exampleAlbum).replace("%output%", Settings.getOutputFolder()).replace("%extension%", ".mp3");
			previewLine.append(pattern);
		}
		Graphics g = lblExample.getGraphics();
		if (g != null) { // before making the label visible, graphics are null
			int stringWidth = g.getFontMetrics().stringWidth(previewLine.toString());
			if (stringWidth >= 560) {
				lblExample.setBounds(lblExample.getX(), lblExample.getY(), stringWidth + 20, lblExample.getHeight());
			}
			else {
				lblExample.setBounds(lblExample.getX(), lblExample.getY(), 560, lblExample.getHeight());
			}
		}

		builder.append(previewLine.toString());
		return builder.toString();
	}

}
