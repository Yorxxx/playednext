package com.piticlistudio.playednext.game.model.entity;

import com.fernandocejas.arrow.optional.Optional;
import com.piticlistudio.playednext.BaseTest;
import com.piticlistudio.playednext.GameFactory;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test cases for Game entity
 * Created by jorge.garcia on 07/03/2017.
 */
public class GameTest extends BaseTest {

    @Test
    public void given_nullCover_When_GetCover_Then_ReturnsNull() throws Exception {

        Game game = Game.create(10, "title");
        game.cover = null;

        // Assert
        assertNull(game.getThumbCoverUrl());
    }

    @Test
    public void given_absentCover_When_GetCover_Then_ReturnsNull() throws Exception {

        Game game = Game.create(10, "title");
        game.cover = Optional.absent();

        // Assert
        assertNull(game.getThumbCoverUrl());
    }

    @Test
    public void given_existingCover_When_GetThumbCoverUrl_Then_ReturnsThumbUrl() throws Exception {

        Game game = GameFactory.provide(10, "title");
        assertTrue(game.cover.isPresent());

        // Assert
        assertNotNull(game.getThumbCoverUrl());
        assertEquals(game.cover.get().getThumbUrl(), game.getThumbCoverUrl());
    }
}