package com.piticlistudio.playednext.gamerelation.model.entity;

import com.fernandocejas.arrow.optional.Optional;
import com.piticlistudio.playednext.GameFactory;
import com.piticlistudio.playednext.game.model.entity.Game;
import com.piticlistudio.playednext.relationinterval.model.entity.RelationInterval;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

    @Test
    public void given_nullStatuses_When_getCurrentStatus_Then_ReturnsAbsent() throws Exception {
        Game game = GameFactory.provide(10, "title");
        GameRelation result = GameRelation.create(game, 100);
        result.setStatuses(null);

        // Act
        Optional<RelationInterval> current = result.getCurrent();

        // Assert
        assertNotNull(current);
        assertFalse(current.isPresent());

    }

    @Test
    public void given_noStatuses_When_getCurrentStatus_Then_ReturnsAbsent() throws Exception {

        Game game = GameFactory.provide(10, "title");
        GameRelation result = GameRelation.create(game, 100);
        result.setStatuses(new ArrayList<>());

        // Act
        Optional<RelationInterval> current = result.getCurrent();

        // Assert
        assertNotNull(current);
        assertFalse(current.isPresent());
    }

    @Test
    public void given_singleStatusNotEnded_When_getCurrentStatus_Then_ReturnsStatus() throws Exception {

        Game game = GameFactory.provide(10, "title");
        GameRelation result = GameRelation.create(game, 100);
        RelationInterval interval = RelationInterval.create(1, RelationInterval.RelationType.DONE, 1000);
        result.getStatuses().add(interval);

        // Act
        Optional<RelationInterval> current = result.getCurrent();

        // Assert
        assertNotNull(current);
        assertTrue(current.isPresent());
        assertEquals(interval, current.get());
    }

    @Test
    public void given_singleIntervalEnded_When_getCurrentStatus_Then_ReturnsAbsent() throws Exception {
        Game game = GameFactory.provide(10, "title");
        GameRelation result = GameRelation.create(game, 100);
        RelationInterval interval = RelationInterval.create(1, RelationInterval.RelationType.DONE, 1000);
        interval.setEndAt(2000);
        result.getStatuses().add(interval);

        // Act
        Optional<RelationInterval> current = result.getCurrent();

        // Assert
        assertNotNull(current);
        assertFalse(current.isPresent());
    }

    @Test
    public void given_multipleIntervals_When_getCurrentStatus_Then_ReturnsNotEndedOne() throws Exception {
        Game game = GameFactory.provide(10, "title");
        GameRelation result = GameRelation.create(game, 100);
        RelationInterval interval = RelationInterval.create(1, RelationInterval.RelationType.PENDING, 1000);
        interval.setEndAt(2000);
        RelationInterval interval2 = RelationInterval.create(1, RelationInterval.RelationType.PLAYING, 2000);
        interval2.setEndAt(3000);
        RelationInterval interval3 = RelationInterval.create(1, RelationInterval.RelationType.DONE, 3000);
        result.getStatuses().add(interval);
        result.getStatuses().add(interval2);
        result.getStatuses().add(interval3);

        // Act
        Optional<RelationInterval> current = result.getCurrent();

        // Assert
        assertNotNull(current);
        assertTrue(current.isPresent());
        assertEquals(interval3, current.get());
    }

    @Test
    public void given_multipleEndedIntervals_When_getCurrentStatus_Then_ReturnsAbsent() throws Exception {
        Game game = GameFactory.provide(10, "title");
        GameRelation result = GameRelation.create(game, 100);
        RelationInterval interval = RelationInterval.create(1, RelationInterval.RelationType.PENDING, 1000);
        interval.setEndAt(2000);
        RelationInterval interval2 = RelationInterval.create(1, RelationInterval.RelationType.PLAYING, 2000);
        interval2.setEndAt(3000);
        RelationInterval interval3 = RelationInterval.create(1, RelationInterval.RelationType.DONE, 3000);
        interval3.setEndAt(4000);
        result.getStatuses().add(interval);
        result.getStatuses().add(interval2);
        result.getStatuses().add(interval3);

        // Act
        Optional<RelationInterval> current = result.getCurrent();

        // Assert
        assertNotNull(current);
        assertFalse(current.isPresent());
    }

    @Test
    public void given_nonCurrentStatus_When_IsBoostEnabled_Then_ReturnsFalse() throws Exception {

        Game game = GameFactory.provide(10, "title");
        GameRelation result = GameRelation.create(game, 100);

        // Act
        assertFalse(result.isBoostEnabled());
    }

    @Test
    public void given_statusIsNotWaiting_When_IsBoostEnabled_Then_ReturnsFalse() throws Exception {

        Game game = GameFactory.provide(10, "title");
        GameRelation result = GameRelation.create(game, 100);
        RelationInterval interval = RelationInterval.create(1, RelationInterval.RelationType.PENDING, 1000);
        interval.setEndAt(2000);
        RelationInterval interval2 = RelationInterval.create(1, RelationInterval.RelationType.PLAYING, 2000);
        interval2.setEndAt(3000);
        RelationInterval interval3 = RelationInterval.create(1, RelationInterval.RelationType.DONE, 3000);
        interval3.setEndAt(4000);
        result.getStatuses().add(interval);
        result.getStatuses().add(interval2);
        result.getStatuses().add(interval3);

        // Act
        assertFalse(result.isBoostEnabled());
    }

    @Test
    public void given_statusIsWaiting_When_IsBoostEnabled_Then_ReturnsTrue() throws Exception {

        Game game = GameFactory.provide(10, "title");
        GameRelation result = GameRelation.create(game, 100);
        RelationInterval interval = RelationInterval.create(1, RelationInterval.RelationType.DONE, 1000);
        interval.setEndAt(2000);
        RelationInterval interval2 = RelationInterval.create(1, RelationInterval.RelationType.PENDING, 2000);
        result.getStatuses().add(interval);
        result.getStatuses().add(interval2);

        // Act
        assertTrue(result.isBoostEnabled());
    }

    @Test
    public void given_any_When_getLastRelease_Then_ReturnsGameLastRelease() throws Exception {

        Game game = mock(Game.class);
        GameRelation result = GameRelation.create(game, 100);
        when(game.getLastRelease()).thenReturn(100L);

        // Assert
        assertEquals(100L, result.getLastRelease());
    }

    @Test
    public void given_any_When_getFirstRelease_Then_ReturnsGameFirstRelease() throws Exception {

        Game game = mock(Game.class);
        GameRelation result = GameRelation.create(game, 100);
        when(game.getFirstRelease()).thenReturn(100L);

        // Assert
        assertEquals(100L, result.getFirstRelease());
    }

    @Test
    public void given_nonWaitingStatus_When_getWaitingStartedAt_Then_ReturnsZero() throws Exception {

        Game game = GameFactory.provide(10, "title");
        GameRelation result = GameRelation.create(game, 100);
        RelationInterval interval = RelationInterval.create(1, RelationInterval.RelationType.PENDING, 1000);
        interval.setEndAt(2000);
        RelationInterval interval2 = RelationInterval.create(1, RelationInterval.RelationType.PLAYING, 2000);
        interval2.setEndAt(3000);
        RelationInterval interval3 = RelationInterval.create(1, RelationInterval.RelationType.DONE, 3000);
        interval3.setEndAt(4000);
        result.getStatuses().add(interval);
        result.getStatuses().add(interval2);
        result.getStatuses().add(interval3);

        // Assert
        assertEquals(0, result.getWaitingStartedAt());
    }

    @Test
    public void given_waitingStatus_When_getWaitingStartedAt_Then_ReturnsIntervalStart() throws Exception {

        Game game = GameFactory.provide(10, "title");
        GameRelation result = GameRelation.create(game, 100);
        RelationInterval interval = RelationInterval.create(1, RelationInterval.RelationType.PLAYING, 1000);
        interval.setEndAt(2000);
        RelationInterval interval2 = RelationInterval.create(1, RelationInterval.RelationType.PENDING, 2000);
        result.getStatuses().add(interval);
        result.getStatuses().add(interval2);

        // Assert
        assertEquals(2000, result.getWaitingStartedAt());
    }
}