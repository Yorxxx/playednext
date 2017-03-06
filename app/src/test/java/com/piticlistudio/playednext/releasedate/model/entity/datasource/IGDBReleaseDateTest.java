package com.piticlistudio.playednext.releasedate.model.entity.datasource;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test cases
 * Created by jorge.garcia on 15/02/2017.
 */
public class IGDBReleaseDateTest {

    @Test
    public void create() throws Exception {
        IGDBReleaseDate date = IGDBReleaseDate.create(1000, "human-yy");

        assertNotNull(date);
        assertEquals(1000, date.date());
        assertEquals("human-yy", date.human());
    }

    @Test
    public void getHumanDate() throws Exception {

        IGDBReleaseDate date = IGDBReleaseDate.create(1000, "human-yy");

        assertEquals("human-yy", date.getHumanDate());
    }

    @Test
    public void getDate() throws Exception {
        IGDBReleaseDate date = IGDBReleaseDate.create(1000, "human-yy");

        assertNotNull(date.getDate());
        assertTrue(date.getDate().isPresent());
        assertEquals(1000, (long)date.getDate().get());

        date = IGDBReleaseDate.create(0, "human-yy");

        assertNotNull(date.getDate());
        assertFalse(date.getDate().isPresent());
    }

}