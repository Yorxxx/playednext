package com.piticlistudio.playednext.game.model.entity.datasource;

import com.piticlistudio.playednext.GameFactory;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test cases
 * Created by jorge.garcia on 10/02/2017.
 */
public class RealmGameTest {

    RealmGame data = GameFactory.provideRealmGame(60, "title");

    @Test
    public void getId() throws Exception {
        assertEquals(60, data.getId());
    }

    @Test
    public void getName() throws Exception {
        assertEquals("title", data.getName());
    }

    @Test
    public void getSummary() throws Exception {
        data.setSummary("summary");
        assertEquals("summary", data.getSummary());
    }

    @Test
    public void getStoryline() throws Exception {
        data.setStoryline("storyline");
        assertEquals("storyline", data.getStoryline());
    }

    @Test
    public void getCollection() throws Exception {
        assertNotNull(data.getCollection());
        assertTrue(data.getCollection().isPresent());
        assertEquals(data.collection.id, data.getCollection().get().id);
        assertTrue(data.getCollection().get().data.isPresent());
        assertEquals(data.collection.id, data.getCollection().get().data.get().getId());
        assertEquals(data.collection.getName(), data.getCollection().get().data.get().getName());

        // Arrange
        data.setCollection(null);

        // Assert
        assertNotNull(data.getCollection());
        assertFalse(data.getCollection().isPresent());
    }
}