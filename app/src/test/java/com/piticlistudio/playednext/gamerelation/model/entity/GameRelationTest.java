package com.piticlistudio.playednext.gamerelation.model.entity;

import com.piticlistudio.playednext.GameFactory;
import com.piticlistudio.playednext.game.model.entity.Game;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test cases for GameRelation
 * Created by jorge.garcia on 24/02/2017.
 */
public class GameRelationTest {


    @Test
    public void create() throws Exception {

        Game game = GameFactory.provide(10, "title");
        GameRelation result = GameRelation.create(game, 100);

        assertNotNull(result);
        assertEquals(game, result.game());
        assertEquals(100, result.createdAt());
        assertEquals(100, result.getUpdatedAt());
        assertEquals(game.id(), result.id());
    }

}