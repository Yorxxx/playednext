package com.piticlistudio.playednext.collection.model.entity.datasource;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Test case
 * Created by jorge.garcia on 10/02/2017.
 */
public class IGDBCollectionTest {

    IGDBCollection data = IGDBCollection.create(10, "collection", "url", 1000, 2000, new ArrayList<>());

    @Test
    public void getId() throws Exception {
        assertEquals(data.id(), data.getId());
    }

    @Test
    public void getName() throws Exception {
        assertEquals(data.name(), data.getName());
    }

    @Test
    public void given_newEntity_When_creates_fillEntityData() throws Exception {

        data = IGDBCollection.create(10, "collection", "url", 1000, 2000, new ArrayList<>());

        assertEquals(10, data.id());
        assertEquals("collection", data.getName());
        assertEquals("url", data.url());
        assertEquals(1000, data.created_at());
        assertEquals(2000, data.updated_at());
        assertNotNull(data.games());
        assertTrue(data.games().isEmpty());

    }
}