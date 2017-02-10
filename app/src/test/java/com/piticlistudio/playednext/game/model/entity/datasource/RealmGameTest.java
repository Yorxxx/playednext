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

}