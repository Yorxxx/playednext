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

    @Test
    public void getThumbUrl() throws Exception {

        ImageData data = ImageData.create("id", 100, 200, "url");

        // Assert
        assertEquals("http://url", data.getThumbUrl());

        data = ImageData.create("id", 100, 200, "http://url");

        // Assert
        assertEquals("http://url", data.getThumbUrl());

        data = ImageData.create("id", 100, 200, "//url");

        // Assert
        assertEquals("http://url", data.getThumbUrl());
    }

    @Test
    public void getFullUrl() throws Exception {

        ImageData data = ImageData.create("id", 100, 200, "url");

        // Assert
        assertEquals("http://url", data.getFullUrl());

        data = ImageData.create("1", 100, 200, "//images.igdb.com/igdb/image/upload/t_thumb/tdwfj2vupiyho2ph60kv.png");

        // Assert
        assertEquals("http://images.igdb.com/igdb/image/upload/tdwfj2vupiyho2ph60kv.png", data.getFullUrl());
    }
}