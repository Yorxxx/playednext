package com.piticlistudio.playednext.game.model.entity;

import com.fernandocejas.arrow.optional.Optional;
import com.piticlistudio.playednext.BaseTest;
import com.piticlistudio.playednext.GameFactory;
import com.piticlistudio.playednext.gamerelease.model.entity.GameRelease;
import com.piticlistudio.playednext.platform.model.entity.Platform;
import com.piticlistudio.playednext.releasedate.model.entity.ReleaseDate;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

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

    @Test
    public void given_any_When_isBoostEnabled_Then_ReturnsTrue() throws Exception {

        Game game = GameFactory.provide(10, "title");
        assertTrue(game.isBoostEnabled());
    }

    @Test
    public void given_multipleReleases_When_getLastRelease_Then_ReturnsLast() throws Exception {

        Game game = GameFactory.provide(10, "title");
        GameRelease release1 = GameRelease.create(Platform.create(10, "platform10"), ReleaseDate.create(8, "eight"));
        GameRelease release2 = GameRelease.create(Platform.create(3, "platform3"), ReleaseDate.create(9, "eight"));
        GameRelease release3 = GameRelease.create(Platform.create(3, "platform3"), ReleaseDate.create(5, "five"));
        List<GameRelease> releases = new ArrayList<>();
        releases.add(release1);
        releases.add(release2);
        releases.add(release3);
        game.releases = releases;

        // Assert
        assertEquals(9L, game.getLastRelease());
    }

    @Test
    public void given_multipleReleases_When_getFirstRelease_Then_ReturnsFirst() throws Exception {

        Game game = GameFactory.provide(10, "title");
        GameRelease release1 = GameRelease.create(Platform.create(10, "platform10"), ReleaseDate.create(8, "eight"));
        GameRelease release2 = GameRelease.create(Platform.create(3, "platform3"), ReleaseDate.create(9, "eight"));
        GameRelease release3 = GameRelease.create(Platform.create(3, "platform3"), ReleaseDate.create(5, "five"));
        List<GameRelease> releases = new ArrayList<>();
        releases.add(release1);
        releases.add(release2);
        releases.add(release3);
        game.releases = releases;

        // Assert
        assertEquals(5L, game.getFirstRelease());
    }

    @Test
    public void given_any_when_getWaitingStartedAt_Then_ReturnsZero() throws Exception {

        Game game = GameFactory.provide(10, "title");
        assertEquals(0, game.getWaitingStartedAt());
    }

    @Test
    public void given_any_When_getCompletedCount_Then_ReturnsZero() throws Exception {
        Game game = GameFactory.provide(10, "title");
        assertEquals(0, game.getCompletedCount());
    }
}