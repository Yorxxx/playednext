package com.piticlistudio.playednext.releasedate.model.entity.datasource;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test cases
 * Created by jorge.garcia on 15/02/2017.
 */
public class RealmReleaseDateTest {

    @Test
    public void getHumanDate() throws Exception {

        RealmReleaseDate data = new RealmReleaseDate("human", 1000);

        assertEquals("human", data.getHumanDate());
    }

    @Test
    public void getDate() throws Exception {

        RealmReleaseDate data = new RealmReleaseDate("human", 1000);

        assertNotNull(data.getDate());
        assertTrue(data.getDate().isPresent());
        assertEquals(1000, (long)data.getDate().get());

        data.setDate(0);

        assertNotNull(data.getDate());
        assertFalse(data.getDate().isPresent());
    }

}