package com.nlstn.jmediaOrganizer.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

import com.nlstn.jmediaOrganizer.files.MP3File;
import com.nlstn.jmediaOrganizer.processing.Converter;
import com.nlstn.jmediaOrganizer.processing.Pattern;

class ConverterTest {

        @Test
        void patternPlaceholdersAreReplaced() {
                MP3File file = new MP3File();
                file.setArtist("ArtistName");
                file.setAlbum("AlbumName");
                file.setTrack("01");
                file.setTitle("SongName");

                Pattern pattern = new Pattern("%artist%/%album%/%track% - %title% (%artist%)");

                String result = Converter.getNewPath(file, pattern);

                assertEquals("ArtistName/AlbumName/01 - SongName (ArtistName)", result);
                assertFalse(result.contains("%"), "Result should not contain unresolved placeholders");
        }
}
