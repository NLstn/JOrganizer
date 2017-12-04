package com.nlstn.jmediaOrganizer.processing;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.ID3v24Tag;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.NotSupportedException;
import com.mpatric.mp3agic.UnsupportedTagException;
import com.nlstn.jmediaOrganizer.Settings;

public class MP3File {

	private static final String	outputPath	= "D:\\Media\\Music\\";

	private File				file;
	private Mp3File				mp3File;

	private ID3v2				id3Tag;

	private String				newLoc;

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

	public void moveTonewLoc() {
		try {
			File newFile = new File(getNewLoc());
			newFile.getParentFile().mkdirs();
			newFile.createNewFile();
		}
		catch (IOException e1) {
			System.err.println("Failed to get newFile " + newLoc + ", " + e1.getClass().getName() + ": " + e1.getMessage());
			return;
		}
		try {
			mp3File.save(newLoc);
			file.delete();
		}
		catch (IOException | NotSupportedException e) {
			System.err.println("Failed to move " + file.getAbsolutePath() + " to " + newLoc + ", " + e.getClass().getName());
		}
	}

	public String getNewLoc() {
		return newLoc = ((Settings.getOutputFolder() + "\\" + (id3Tag.getAlbumArtist().trim() + " - " + id3Tag.getAlbum().trim()).replace(":", "") + "\\").replace("?", "").replaceAll("ue", "ü").replaceAll("/", "").replace("'", "") + id3Tag.getTitle().replaceAll("[\\\\/:*?\"<>|]", "") + getExtension()).trim();
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
		if (id3Tag.getAlbum() == null || id3Tag.getAlbum() == "" || id3Tag.getAlbumArtist() == null || id3Tag.getAlbumArtist() == "" || id3Tag.getTitle() == null || id3Tag.getTitle() == "" || id3Tag.getTrack() == null || id3Tag.getTrack() == "") {
			System.err.println("Missing ID3Tags " + file.getAbsolutePath());
			return false;
		}
		return true;
	}

	private String getExtension() {
		return file.getName().substring(file.getName().lastIndexOf('.'));
	}

}
