package com.nlstn.jmediaOrganizer.files;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MP3FileTest {

        @Test
        public void setGenreIgnoresUnknownGenres() {
                MP3File file = new MP3File();
                file.setGenre("Rock");
                assertEquals("Rock", file.getGenre());

                file.setGenre("Unknown Genre");
                assertEquals("Rock", file.getGenre());
        }

        @Test
        public void setGenreSkipsBlankGenres() {
                MP3File file = new MP3File();
                file.setGenre(" ");
                assertEquals("", file.getGenre());
        }
}
