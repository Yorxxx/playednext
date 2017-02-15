package com.piticlistudio.playednext.releasedate.model.entity;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test cases
 * Created by jorge.garcia on 15/02/2017.
 */
public class ReleaseDateTest {

    @Test
    public void create() throws Exception {

        ReleaseDate date = ReleaseDate.create(1000, "human");

        assertNotNull(date);
        assertEquals(1000, date.date());
        assertEquals("human", date.humanDate());
    }

}