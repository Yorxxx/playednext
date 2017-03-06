package com.piticlistudio.playednext.image.model.entity.datasource;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test cases
 * Created by jorge.garcia on 13/02/2017.
 */
public class IGDBImageDataTest {

    private IGDBImageData data = IGDBImageData.create("url", 100, 200, "id");

    @Test
    public void create() throws Exception {

        // Act
        IGDBImageData data = IGDBImageData.create("url", 100, 200, "id");

        assertNotNull(data);
        assertEquals("url", data.url());
        assertEquals(100, data.width());
        assertEquals(200, data.height());
        assertEquals("id", data.cloudinary_id());

    }

    @Test
    public void getId() throws Exception {

        assertEquals("id", data.getId());
    }

    @Test
    public void getWidth() throws Exception {
        assertEquals(100, data.getWidth());
    }

    @Test
    public void getHeight() throws Exception {
        assertEquals(200, data.getHeight());
    }

    @Test
    public void getUrl() throws Exception {
        assertEquals("url", data.getUrl());
    }

}