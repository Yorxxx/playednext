package com.piticlistudio.playednext.genre.model.entity;

import com.piticlistudio.playednext.genre.model.entity.datasource.NetGenre;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test cases
 * Created by jorge.garcia on 14/02/2017.
 */
public class GenreTest {


    @Test
    public void create() throws Exception {

        Genre data = Genre.create(50, "name");

        // Assert
        assertNotNull(data);
        assertEquals(50, data.id());
        assertEquals("name", data.name());
    }

}