package com.nlstn.jmediaOrganizer.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import com.nlstn.jmediaOrganizer.files.MP3File;
import com.nlstn.jmediaOrganizer.processing.Converter;
import com.nlstn.jmediaOrganizer.processing.Pattern;

public class ConverterTest {

        @Test
        public void patternPlaceholdersAreReplaced() {
                MP3File file = new MP3File();
                file.setArtist("ArtistName");
                file.setAlbum("AlbumName");
                file.setTrack("01");
                file.setTitle("SongName");

                Pattern pattern = new Pattern("%artist%/%album%/%track% - %title% (%artist%)");

                String result = Converter.getNewPath(file, pattern);

                assertEquals("ArtistName/AlbumName/01 - SongName (ArtistName)", result);
                assertFalse("Result should not contain unresolved placeholders", result.contains("%"));
        }
}
