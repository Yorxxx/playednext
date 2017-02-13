package com.piticlistudio.playednext.image.model.entity;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test cases
 * Created by jorge.garcia on 13/02/2017.
 */
public class ImageDataTest {

    @Test
    public void create() throws Exception {

        ImageData data = ImageData.create("id", 100, 200, "url");

        // Assert
        assertNotNull(data);
        assertEquals("id", data.id());
        assertEquals(100, data.fullWidth());
        assertEquals(200, data.fullHeight());
        assertEquals("url", data.thumbUrl());
    }

}