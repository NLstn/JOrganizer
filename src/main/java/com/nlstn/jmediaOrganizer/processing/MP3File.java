package com.nlstn.jmediaOrganizer.processing;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.JOptionPane;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.ID3v24Tag;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.NotSupportedException;
import com.mpatric.mp3agic.UnsupportedTagException;
import com.nlstn.jmediaOrganizer.MusicProcessor;

public class MP3File {

	private File	file;
	private Mp3File	mp3File;

	private ID3v2	id3Tag;

	public MP3File(File file) {
		this.file = file;
	}

	public boolean loadMp3Data() {
		try {
			mp3File = new Mp3File(file);
		}
		catch (UnsupportedTagException | InvalidDataException | IOException e) {
			System.err.println("Unable to load Mp3 data " + file.getAbsolutePath());
			return false;
		}
		return getId3Tags();
	}

	public boolean isOfType(List<String> types) {
		return types.contains(getExtension().toLowerCase());
	}

	public boolean deleteIfOfType(List<String> types) {
		if (types.contains(getExtension().toLowerCase())) {
			if (!file.delete()) {
				System.out.println("Failed to delete file " + file.getAbsolutePath());
			}
			else {
				System.out.println("Deleting file " + file.getAbsolutePath());
			}
			return true;
		}
		return false;
	}

	public void moveToLocation(String newLocation) {
		try {
			mp3File.save(newLocation);
		}
		catch (NotSupportedException | IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(MusicProcessor.getWindow().getFrame(), "Error", "Failed to relocate file!", JOptionPane.ERROR_MESSAGE);
		}
	}

	private boolean getId3Tags() {
		id3Tag = null;
		if (mp3File.hasId3v2Tag()) {
			id3Tag = mp3File.getId3v2Tag();
		}
		else
			if (mp3File.hasId3v1Tag()) {
				// TODO: convert existing tags
				ID3v1 v1Tags = mp3File.getId3v1Tag();
				id3Tag = new ID3v24Tag();
				id3Tag.setTitle(v1Tags.getTitle());
				id3Tag.setTrack(v1Tags.getTrack());
				id3Tag.setArtist(v1Tags.getArtist());
				id3Tag.setAlbumArtist(v1Tags.getArtist());
				id3Tag.setYear(v1Tags.getYear());
				id3Tag.setTrack(v1Tags.getTrack());
				id3Tag.setGenre(v1Tags.getGenre());
				id3Tag.setGenreDescription(v1Tags.getGenreDescription());
				id3Tag.setAlbum(v1Tags.getAlbum());
				mp3File.setId3v2Tag(id3Tag);
				System.err.println("Outdated ID3Tags!");
			}
			else {
				System.err.println("Missing ID3Tags " + file.getAbsolutePath());
				return false;
			}
		if (id3Tag.getArtist() == null) {
			try {
				id3Tag.setArtist(((ID3v2) id3Tag).getAlbumArtist());
			}
			catch (Exception e) {

			}
		}
		if (id3Tag.getAlbum() == null || id3Tag.getAlbum() == "" || id3Tag.getTitle() == null || id3Tag.getTitle() == "" || id3Tag.getTrack() == null || id3Tag.getTrack() == "") {
			System.err.println("Missing ID3Tags " + file.getAbsolutePath());
			return false;
		}
		return true;
	}

	public String getExtension() {
		return file.getName().substring(file.getName().lastIndexOf('.'));
	}

	public String getTitle() {
		return id3Tag.getTitle();
	}

	public String getArtist() {
		return id3Tag.getArtist();
	}

	public String getAlbum() {
		return id3Tag.getAlbum();
	}

	public String getTrack() {
		return id3Tag.getTrack();
	}

}
