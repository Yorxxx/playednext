package com.piticlistudio.playednext.image.model.entity.datasource;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test cases
 * Created by jorge.garcia on 13/02/2017.
 */
public class RealmImageDataTest {

    private RealmImageData data = new RealmImageData("id", "url", 100, 200);

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