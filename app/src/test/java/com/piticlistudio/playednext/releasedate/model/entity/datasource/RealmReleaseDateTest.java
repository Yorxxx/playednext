package com.piticlistudio.playednext.releasedate.model.entity.datasource;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Test cases
 * Created by jorge.garcia on 15/02/2017.
 */
public class RealmReleaseDateTest {

    @Test
    public void given_releaseDate_When_getHumanDate_Then_ReturnsHumanDate() throws Exception {

        RealmReleaseDate data = new RealmReleaseDate();
        data.setDate(1000);
        data.setHuman("human");

        // Assert
        assertEquals("human", data.getHumanDate());
    }

    @Test
    public void given_invalidReleaseDate_When_getDate_Then_ReturnsAbsent() throws Exception {
        RealmReleaseDate data = new RealmReleaseDate("human", 0);
        data.setDate(0);

        // Assert
        assertNotNull(data.getDate());
        assertFalse(data.getDate().isPresent());
    }

    @Test
    public void given_validReleaseDate_When_getDate_Then_ReturnsDate() throws Exception {

        RealmReleaseDate data = new RealmReleaseDate("human", 1000);

        assertNotNull(data.getDate());
        assertTrue(data.getDate().isPresent());
        assertEquals(1000, (long) data.getDate().get());
    }
}