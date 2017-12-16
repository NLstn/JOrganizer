package com.nlstn.jmediaOrganizer.processing;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.ID3v24Tag;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.NotSupportedException;
import com.mpatric.mp3agic.UnsupportedTagException;
import com.nlstn.jmediaOrganizer.JMediaOrganizer;

public class MP3File {

	private static Map<Integer, String> genreMapping;

	static {
		genreMapping = new HashMap<Integer, String>();
		genreMapping.put(0, "Blues");
		genreMapping.put(1, "Classic Rock");
		genreMapping.put(2, "Country");
		genreMapping.put(3, "Dance");
		genreMapping.put(4, "Disco");
		genreMapping.put(5, "Funk");
		genreMapping.put(6, "Grunge");
		genreMapping.put(7, "Hip-Hop");
		genreMapping.put(8, "Jazz");
		genreMapping.put(9, "Metal");
		genreMapping.put(10, "New Age");
		genreMapping.put(11, "Oldies");
		genreMapping.put(12, "Other");
		genreMapping.put(13, "Pop");
		genreMapping.put(14, "R&B");
		genreMapping.put(15, "Rap");
		genreMapping.put(16, "Reggae");
		genreMapping.put(17, "Rock");
		genreMapping.put(18, "Techno");
		genreMapping.put(19, "Industrial");
		genreMapping.put(20, "Alternative");
		genreMapping.put(21, "Ska");
		genreMapping.put(22, "Death Metal");
		genreMapping.put(23, "Pranks");
		genreMapping.put(24, "Soundtrack");
		genreMapping.put(25, "Euro-Techno");
		genreMapping.put(26, "Ambient");
		genreMapping.put(27, "Trip-Hop");
		genreMapping.put(28, "Vocal");
		genreMapping.put(29, "Jazz+Funk");
		genreMapping.put(30, "Fusion");
		genreMapping.put(31, "Trance");
		genreMapping.put(32, "Classical");
		genreMapping.put(33, "Instrumental");
		genreMapping.put(34, "Acid");
		genreMapping.put(35, "House");
		genreMapping.put(36, "Game");
		genreMapping.put(37, "Sound Clip");
		genreMapping.put(38, "Gospel");
		genreMapping.put(39, "Noise");
		genreMapping.put(40, "AlternRock");
		genreMapping.put(41, "Bass");
		genreMapping.put(42, "Soul");
		genreMapping.put(43, "Punk");
		genreMapping.put(44, "Space");
		genreMapping.put(45, "Meditative");
		genreMapping.put(46, "Instrumental Pop");
		genreMapping.put(47, "Instrumental Rock");
		genreMapping.put(48, "Ethnic");
		genreMapping.put(49, "Gothic");
		genreMapping.put(50, "Darkwave");
		genreMapping.put(51, "Techno-Industrial");
		genreMapping.put(52, "Electronic");
		genreMapping.put(53, "Pop-Folk");
		genreMapping.put(54, "Eurodance");
		genreMapping.put(55, "Dream");
		genreMapping.put(56, "Southern Rock");
		genreMapping.put(57, "Comedy");
		genreMapping.put(58, "Cult");
		genreMapping.put(59, "Gangsta");
		genreMapping.put(60, "Top 40");
		genreMapping.put(61, "Christian Rap");
		genreMapping.put(62, "Pop/Funk");
		genreMapping.put(63, "Jungle");
		genreMapping.put(64, "Native American");
		genreMapping.put(65, "Cabaret");
		genreMapping.put(66, "New Wave");
		genreMapping.put(67, "Psychadelic");
		genreMapping.put(68, "Rave");
		genreMapping.put(69, "Showtunes");
		genreMapping.put(70, "Trailer");
		genreMapping.put(71, "Lo-Fi");
		genreMapping.put(72, "Tribal");
		genreMapping.put(73, "Acid Punk");
		genreMapping.put(74, "Acid Jazz");
		genreMapping.put(75, "Polka");
		genreMapping.put(76, "Retro");
		genreMapping.put(77, "Musical");
		genreMapping.put(78, "Rock & Roll");
		genreMapping.put(79, "Hard Rock");
	}

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
		File f = new File(newLocation);
		File parent = f.getParentFile();
		parent.mkdirs();
		try {
			f.createNewFile();
			mp3File.save(newLocation);
		}
		catch (NotSupportedException | IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(JMediaOrganizer.getWindow().getFrame(), "Error", "Failed to relocate file!", JOptionPane.ERROR_MESSAGE);
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

	public String getAlbumArtist() {
		return id3Tag.getAlbumArtist();
	}

	public String getBPM() {
		return String.valueOf(id3Tag.getBPM());
	}

	public String getComposer() {
		return id3Tag.getComposer();
	}

	public String getDate() {
		return id3Tag.getDate();
	}

	public String getGenre() {
		return genreMapping.get(id3Tag.getGenre());
	}

	public String getLength() {
		return String.valueOf(id3Tag.getLength());
	}

	public String getYear() {
		return id3Tag.getYear();
	}

}
