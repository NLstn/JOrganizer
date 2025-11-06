package com.nlstn.jmediaOrganizer.gui.settings.panels;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

import com.nlstn.jmediaOrganizer.files.MP3File;
import com.nlstn.jmediaOrganizer.gui.models.VariableComboBoxModel;
import com.nlstn.jmediaOrganizer.gui.settings.SettingsPanel;
import com.nlstn.jmediaOrganizer.processing.Converter;
import com.nlstn.jmediaOrganizer.processing.ConverterVariable;
import com.nlstn.jmediaOrganizer.processing.FileProcessor;
import com.nlstn.jmediaOrganizer.processing.Pattern;
import com.nlstn.jmediaOrganizer.properties.Settings;
import com.nlstn.jmediaOrganizer.validation.PatternValidator;
import com.nlstn.jmediaOrganizer.validation.ValidationError;

public class ConverterSettingsPanel extends SettingsPanel {

	private static final long	serialVersionUID	= 5821559048829857180L;

	private JTextField			txtPattern;

	private JTextArea			lblExample;

	private JCheckBox			chkEnabled;

	private JScrollPane			scrollExample;

	private transient MP3File	preview;

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
		scrollExample.setBounds(10, 27, 580, 160);
		scrollExample.setBorder(new CompoundBorder(lineBorder, insets));
		add(scrollExample);

		JLabel lblPattern = new JLabel("Pattern:");
		lblPattern.setBounds(10, 197, 50, 28);
		add(lblPattern);

		JComboBox<ConverterVariable> comboBox = new JComboBox<ConverterVariable>(new VariableComboBoxModel());
		comboBox.setBounds(70, 197, 150, 28);
		comboBox.addActionListener((ActionEvent e) -> {
			appendVariable((ConverterVariable) comboBox.getSelectedItem());
		});
		add(comboBox);

		txtPattern = new JTextField();
		txtPattern.setBounds(10, 230, 460, 28);
		add(txtPattern);

		preview = FileProcessor.getPreviewExample();

		setVisible(true);
	}

	private void appendVariable(ConverterVariable variable) {
		if (variable == null)
			return;
		String token = variable.getVariable();
		if (token == null || token.isBlank())
			return;
		try {
			txtPattern.getDocument().insertString(txtPattern.getCaretPosition(), token, null);
		}
		catch (BadLocationException e) {
			e.printStackTrace();
		}
		txtPattern.requestFocus();
	}

	private void onToggleEnabled() {
		txtPattern.setEnabled(chkEnabled.isSelected());
		lblExample.setEnabled(chkEnabled.isSelected());
	}

	public void loadSettings() {
		chkEnabled.setSelected(Settings.getID3ToNameEnabled());
		txtPattern.setText(Settings.getID3ToNamePattern().toString());
		onToggleEnabled();
		lblExample.setText(buildPreview());
		lblExample.setCaretPosition(0);
		addActionListeners();
	}

	public void reload() {
		lblExample.setText(buildPreview());
	}

	public boolean saveSettings() {
		List<ValidationError> errors = PatternValidator.validate(txtPattern.getText());
		if (errors.size() != 0) {
			StringBuilder builder = new StringBuilder();
			builder.append("The pattern is invalid.\n");
			for (ValidationError error : errors)
				builder.append(error + "\n");
			JOptionPane.showMessageDialog(this, builder.toString(), "Invalid Pattern", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		Settings.setID3ToNameEnabled(chkEnabled.isSelected());
		Settings.setID3ToNamePattern(new Pattern(txtPattern.getText()));
		return true;
	}

	private void addActionListeners() {
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
	}

	private String buildPreview() {
		StringBuilder builder = new StringBuilder();
		builder.append("Track (%track%):\t").append(preview.getTrack()).append("\n");
		builder.append("Title (%title%):\t").append(preview.getTitle()).append("\n");
		builder.append("Artist (%artist%):\t").append(preview.getArtist()).append("\n");
		builder.append("Album (%album%):\t").append(preview.getAlbum()).append("\n");
		builder.append("\n");
		StringBuilder previewLine = new StringBuilder();
		previewLine.append("Preview:\n");
		if (txtPattern.getText() != null && !txtPattern.getText().equals("")) {
			String pattern = Converter.getNewPath(preview, new Pattern(txtPattern.getText()));
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
