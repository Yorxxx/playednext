package com.piticlistudio.playednext.genre.model.entity.datasource;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test cases
 * Created by jorge.garcia on 14/02/2017.
 */
public class IGDBGenreTest {

    private IGDBGenre data;

    @Test
    public void create() throws Exception {

        data = IGDBGenre.create(50, "name", "slug", "url", 1000, 2000);

        assertNotNull(data);
        assertEquals(50, data.id());
        assertEquals("name", data.name());
        assertEquals("slug", data.slug());
        assertEquals("url", data.url());
        assertEquals(1000, data.created_at());
        assertEquals(2000, data.updated_at());
    }

    @Test
    public void getId() throws Exception {

        // Arrange
        data = IGDBGenre.create(50, "name", "slug", "url", 1000, 2000);

        // Assert
        assertEquals(50, data.getId());
    }

    @Test
    public void getName() throws Exception {
        // Arrange
        data = IGDBGenre.create(50, "name", "slug", "url", 1000, 2000);

        // Assert
        assertEquals("name", data.getName());
    }
}