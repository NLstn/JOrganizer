package com.nlstn.jmediaOrganizer.gui.converters;

import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.nlstn.jmediaOrganizer.gui.Window;

public class ID3ToNameConverterWindow extends JDialog {
	private static final long	serialVersionUID	= -9218566319600619223L;

	private int					width				= 600;
	private int					height				= 300;

	private JTextField			txtPattern;

	private JTextArea			lblExample;

	private String				exampleArtist		= "Linkin Park";
	private String				exampleAlbum		= "A Thousand Suns";
	private String				exampleTrack		= "12";
	private String				exampleTitle		= "Iridescent";

	public ID3ToNameConverterWindow(JFrame mainFrame) {
		super(mainFrame, "ID3Tag to Name Converter", true);
		setSize(width, height);
		setLocationRelativeTo(mainFrame);
		setLayout(null);

		lblExample = new JTextArea();
		lblExample.setBounds(10, 10, 580, 130);
		lblExample.setEditable(false);
		add(lblExample);

		JLabel lblPattern = new JLabel("Pattern:");
		lblPattern.setBounds(10, 150, 50, 28);
		add(lblPattern);

		txtPattern = new JTextField();
		txtPattern.setBounds(70, 150, 380, 28);
		txtPattern.getDocument().addDocumentListener(new DocumentListener() {

			public void insertUpdate(DocumentEvent e) {
				lblExample.setText(buildPreview());
			}

			public void removeUpdate(DocumentEvent e) {
				lblExample.setText(buildPreview());
			}

			public void changedUpdate(DocumentEvent e) {
				lblExample.setText(buildPreview());
			}

		});
		add(txtPattern);

		JButton button = new JButton("Convert all");
		button.addActionListener((ActionEvent e) -> onConvert());
		button.setBounds(width / 2 - Window.BUTTON_WIDTH / 2, height - 100, Window.BUTTON_WIDTH, 28);
		add(button);

		lblExample.setText(buildPreview());

		setVisible(true);
	}

	private String buildPreview() {
		StringBuilder builder = new StringBuilder();
		builder.append("Track (%track%):\t").append(exampleTrack).append("\n");
		builder.append("Title (%title%):\t").append(exampleTitle).append("\n");
		builder.append("Artist (%artist%):\t").append(exampleArtist).append("\n");
		builder.append("Album (%album%):\t").append(exampleAlbum).append("\n");
		builder.append("\n");
		builder.append("Preview:\t");
		if (txtPattern.getText() != null) {
			String pattern = txtPattern.getText().replace("%track%", exampleTrack).replace("%title%", exampleTitle).replace("%artist%", exampleArtist).replace("%album%", exampleAlbum);
			builder.append(pattern);
		}
		return builder.toString();
	}

	private void onConvert() {

	}

}
