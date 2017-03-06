package com.piticlistudio.playednext.genre.model.entity.datasource;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test cases
 * Created by jorge.garcia on 14/02/2017.
 */
public class RealmGenreTest {

    private RealmGenre data = new RealmGenre(50, "name");

    @Test
    public void getId() throws Exception {

        data = new RealmGenre();
        data.setId(100);

        assertEquals(100, data.getId());
    }

    @Test
    public void getName() throws Exception {
        assertEquals("name", data.getName());
    }

}