package com.nlstn.jmediaOrganizer.files;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tika.Tika;

/**
 * Creation: 26 Jan 2018
 *
 * @author Niklas Lahnstein
 */
public class MediaFile {

	private static Logger	log;

	private static Tika		tika;

	static {
		log = LogManager.getLogger(MediaFile.class);
		tika = new Tika();
	}

	/**
	 * The file where this mp3File is stored
	 */
	protected File		file;

	protected String	fileType	= "Undefined";

	public MediaFile(File file) {
		this.file = file;
	}

	public MediaFile() {

	}

	public boolean delete() {
		if (!file.exists())
			return true;
		return file.delete();
	}

	public String getAbsolutePath() {
		return file.getAbsolutePath();
	}

	public String getExtension() {
		if (file == null)
			return "";
		return file.getName().substring(file.getName().lastIndexOf('.'));
	}

	/**
	 * Goes through the given list of types and checks, whether this file is of any of these types.
	 * 
	 * @param types
	 *            The types to check
	 * @return Whether the list contains the type of this file.
	 */
	public boolean isOfType(List<String> types) {
		return types.contains(getExtension().toLowerCase(Locale.getDefault()));
	}

	/**
	 * Checks whether this {@link #isOfType(List)} is true, and if so, deletes this file from disc.
	 * 
	 * @param types
	 *            The types to check
	 * @return Whether or not this file was deleted, based on the outcome of {@link #isOfType(List)} and {@link File#delete()}.
	 */
	public boolean deleteIfOfType(List<String> types) {
		if (types.contains(getExtension().toLowerCase(Locale.getDefault()))) {
			if (!delete()) {
				log.error("Failed to delete file " + getAbsolutePath());
			}
			else {
				log.error("Deleting file " + getAbsolutePath());
			}
			return true;
		}
		return false;
	}

	public String determineType() {
		if (fileType.equals("Undefined") && file != null) {
			try {
				fileType = tika.detect(file);
			}
			catch (IOException e) {
				log.error("Error while resolving mime type.", e);
			}
		}
		return fileType;
	}

	protected boolean createNewFile(String newPath) {
		File f = new File(newPath);
		File parent = f.getParentFile();
		if (!parent.mkdirs() && !parent.exists()) {
			log.error("Failed create parent folder of path {}", newPath);
			return false;
		}
		try {
			return f.createNewFile() || f.exists();
		}
		catch (IOException e) {
			log.error("Exception while creating new file!", e);
			return false;
		}
	}
}
