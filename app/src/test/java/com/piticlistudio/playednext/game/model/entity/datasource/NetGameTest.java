package com.piticlistudio.playednext.game.model.entity.datasource;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test cases
 * Created by jorge.garcia on 10/02/2017.
 */
public class NetGameTest {

    private NetGame data = NetGame.create(10, "name", "slug", "url", 1000, 2500);


    @Test
    public void getId() throws Exception {
        assertEquals(data.id(), data.getId());
    }

    @Test
    public void getName() throws Exception {
        assertEquals(data.name(), data.getName());
    }

    @Test
    public void getSummary() throws Exception {
        data.summary = "summary";
        assertEquals(data.summary, data.getSummary());
    }

    @Test
    public void getStoryline() throws Exception {
        data.storyline = "storyline";
        assertEquals(data.storyline, data.getStoryline());
    }

    @Test
    public void getCollection() throws Exception {
        data.collection = 10;
        assertNotNull(data.getCollection());
        assertTrue(data.getCollection().isPresent());
        assertEquals(data.collection, data.getCollection().get().id);
        assertFalse(data.getCollection().get().data.isPresent());

        // Arrange
        data.collection = -1;

        // Assert
        assertNotNull(data.getCollection());
        assertFalse(data.getCollection().isPresent());
    }
}