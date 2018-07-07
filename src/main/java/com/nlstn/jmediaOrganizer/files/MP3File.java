package com.nlstn.jmediaOrganizer.files;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.ID3v24Tag;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.NotSupportedException;
import com.mpatric.mp3agic.UnsupportedTagException;

/**
 * This class represents a single mp3 file and contains methods for retrieving and manipulating it's id3 tags.<br>
 * <br>
 * Creation: 22.12.2017
 *
 * @author Niklas Lahnstein
 */
public class MP3File extends MediaFile {

	private static Logger log;

	static {
		log = LogManager.getLogger(MP3File.class);
	}

	/**
	 * Mapping between the byte representation and the display name of genres, copied from http://id3.org/d3v2.3.0?highlight=(id3v2.3.0.txt)
	 */
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

	/**
	 * The {@link Mp3File}, which contains id3 information
	 */
	private Mp3File	mp3File;

	/**
	 * The actual {@link ID3v2 ID3 tags}
	 */
	private ID3v2	id3Tag;

	/**
	 * Constructs a new MP3File from the specified file.<br>
	 * ID3 tags are not loaded within the constructor, an extra call to {@link #loadMp3Data()} is necessary to have this information available.
	 * 
	 * @param file
	 *            The mp3 file
	 */
	public MP3File(File file) {
		super(file);
	}

	/**
	 * Constructs a new MP3File without an underlying file.<br>
	 * This is used to generate virtual ID3Tags, for example for the preview in the converter settings pane.
	 */
	public MP3File() {
		id3Tag = new ID3v24Tag();
	}

	/**
	 * Loads the physical mp3 file from disc.<br>
	 * Afterwards, the ID3Tags are being loaded, if there are any.<br>
	 * If any of these steps fails, either because the file cannot be read, or if the file does not have ID3Tags, this method returns false.
	 * 
	 * @return Whether loading the file and it's ID3Tags was successful.
	 */
	public boolean loadMp3Data() {
		try {
			mp3File = new Mp3File(file);
		}
		catch (UnsupportedTagException | InvalidDataException | IOException e) {
			log.error("Unable to load Mp3 data " + file.getAbsolutePath(), e);
			return false;
		}
		return getId3Tags();
	}

	/**
	 * Tries to create all parent folders of the files new path and move the file there.
	 * 
	 * @param newLocation
	 *            The new location to move the file to.
	 */
	public boolean moveToLocation(String newLocation) {
		if (!createNewFile(newLocation))
			return false;
		try {
			mp3File.save(newLocation);
			return true;
		}
		catch (NotSupportedException e) {
			ID3v2 tags = copyID3ToID3v2(mp3File.getId3v2Tag());
			clearTags();
			mp3File.setId3v2Tag(tags);
			try {
				mp3File.save(newLocation);
			}
			catch (NotSupportedException | IOException e2) {
				log.error("Failed to relocate file", e2);
				return false;
			}
			return true;
		}
		catch (IOException e) {
			log.error("Failed to relocate file", e);
			return false;
		}
	}

	/**
	 * Tries to load the ID3Tags of this file.<br>
	 * <br>
	 * First of all, the file is inspected if it has ID3v1 or ID3v2 tags.<br>
	 * If the file contains none of both, the method returns false.<br>
	 * If the file contains ID3v1 tags, it's information is copied into a new ID3v2 instance.<br>
	 * If the file contains ID3v2 tags, the method continues to the validation of the tags.<br>
	 * <br>
	 * After that, a little fix is being applied: If the artist of the loaded ID3Tag is empty, copy the album artist into the artist.<br>
	 * The artist is more important for the program than the album artist, so if the artist is empty, this is an attempt to fix it.<br>
	 * <br>
	 * Last but not least, the loaded ID3Tags are checked for completeness.<br>
	 * The mandatory fields are: track, title, album and artist. if any of this fields is missing, the file is marked incomplete.<br>
	 * <br>
	 * Only if the ID3Tags are successfully being loaded and are "complete", this method returns true.
	 * 
	 * @return Whether the ID3Tags of this file could be loaded and are complete
	 */
	private boolean getId3Tags() {
		id3Tag = null;
		if (mp3File.hasId3v2Tag()) {
			id3Tag = mp3File.getId3v2Tag();
		}
		else
			if (mp3File.hasId3v1Tag()) {
				id3Tag = copyID3ToID3v2(mp3File.getId3v1Tag());
				mp3File.removeId3v1Tag();
				mp3File.setId3v2Tag(id3Tag);
				log.info("Fixed outdated ID3Tags!");
			}
			else {
				log.error("Missing ID3Tags " + getAbsolutePath());
				return false;
			}
		if (id3Tag.getArtist() == null && id3Tag.getAlbumArtist() != null) {
			id3Tag.setArtist(id3Tag.getAlbumArtist());
			log.debug("Filling empty Artist with Album Artist");
		}
		if (id3Tag.getArtist() != null && id3Tag.getAlbumArtist() == null) {
			id3Tag.setAlbumArtist(id3Tag.getArtist());
			log.debug("Filling empty Album Artist with Artist!");
		}
		if (id3Tag.getArtist() == null || id3Tag.getArtist().equals("") || id3Tag.getAlbum() == null || id3Tag.getAlbum().equals("") || id3Tag.getTitle() == null || id3Tag.getTitle().equals("") || id3Tag.getTrack() == null || id3Tag.getTrack().equals("")) {
			log.error("Missing ID3Tags " + getAbsolutePath());
			return false;
		}
		return true;
	}

	private ID3v2 copyID3ToID3v2(ID3v1 v1Tags) {
		ID3v24Tag id3Tag = new ID3v24Tag();
		id3Tag.setTitle(v1Tags.getTitle());
		id3Tag.setTrack(v1Tags.getTrack());
		id3Tag.setArtist(v1Tags.getArtist());
		id3Tag.setAlbumArtist(v1Tags.getArtist());
		id3Tag.setYear(v1Tags.getYear());
		id3Tag.setTrack(v1Tags.getTrack());
		id3Tag.setGenre(v1Tags.getGenre());
		id3Tag.setGenreDescription(v1Tags.getGenreDescription());
		id3Tag.setAlbum(v1Tags.getAlbum());
		return id3Tag;
	}

	private ID3v2 copyID3ToID3v2(ID3v2 v2Tags) {
		ID3v24Tag id3Tag = new ID3v24Tag();
		id3Tag.setTitle(v2Tags.getTitle());
		id3Tag.setTrack(v2Tags.getTrack());
		id3Tag.setArtist(v2Tags.getArtist());
		id3Tag.setAlbumArtist(v2Tags.getArtist());
		id3Tag.setYear(v2Tags.getYear());
		id3Tag.setTrack(v2Tags.getTrack());
		id3Tag.setGenre(v2Tags.getGenre());
		id3Tag.setGenreDescription(v2Tags.getGenreDescription());
		id3Tag.setAlbum(v2Tags.getAlbum());
		return id3Tag;
	}

	public void clearTags() {
		mp3File.removeCustomTag();
		mp3File.removeId3v1Tag();
		mp3File.removeId3v2Tag();
	}

	public String getTitle() {
		return id3Tag.getTitle() != null ? id3Tag.getTitle() : "";
	}

	public void setTitle(String title) {
		id3Tag.setTitle(title);
	}

	public String getArtist() {
		String artist = id3Tag.getArtist();
		if (artist == null)
			artist = "";
		else {
			artist = artist.replace("/", ";");
		}
		return artist;
	}

	public void setArtist(String artist) {
		id3Tag.setArtist(artist);
	}

	public String getAlbum() {
		return id3Tag.getAlbum() != null ? id3Tag.getAlbum() : "";
	}

	public void setAlbum(String album) {
		id3Tag.setAlbum(album);
	}

	public String getTrack() {
		return id3Tag.getTrack() != null ? id3Tag.getTrack() : "";
	}

	public void setTrack(String track) {
		id3Tag.setTrack(track);
	}

	public String getAlbumArtist() {
		String albumArtist = id3Tag.getAlbumArtist();
		if (albumArtist == null)
			albumArtist = "";
		else {
			albumArtist = albumArtist.replace("/", ";");
		}
		return albumArtist;
	}

	public void setAlbumArtist(String albumArtist) {
		id3Tag.setAlbumArtist(albumArtist);
	}

	public String getBPM() {
		return String.valueOf(id3Tag.getBPM());
	}

	public void setBPM(int bpm) {
		id3Tag.setBPM(bpm);
	}

	public String getComposer() {
		return id3Tag.getComposer() != null ? id3Tag.getComposer() : "";
	}

	public void setComposer(String composer) {
		id3Tag.setComposer(composer);
	}

	public String getDate() {
		return id3Tag.getDate() != null ? id3Tag.getDate() : "";
	}

	public void setDate(String date) {
		id3Tag.setDate(date);
	}

	public String getGenre() {
		return genreMapping.get(id3Tag.getGenre()) != null ? genreMapping.get(id3Tag.getGenre()) : "";
	}

	public void setGenre(String genre) {
		id3Tag.setGenre(genreMapping.entrySet().stream().filter(entry -> entry.getValue().equals(genre)).collect(Collectors.toList()).get(0).getKey());
	}

	public String getLength() {
		return String.valueOf(id3Tag.getLength());
	}

	public String getYear() {
		return id3Tag.getYear() != null ? id3Tag.getYear() : "";
	}

	public void setYear(String year) {
		id3Tag.setYear(year);
	}
}