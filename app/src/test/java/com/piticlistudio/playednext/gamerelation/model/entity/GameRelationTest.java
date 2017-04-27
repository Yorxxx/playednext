package com.piticlistudio.playednext.gamerelation.model.entity;

import com.fernandocejas.arrow.optional.Optional;
import com.piticlistudio.playednext.GameFactory;
import com.piticlistudio.playednext.game.model.entity.Game;
import com.piticlistudio.playednext.relationinterval.model.entity.RelationInterval;

import org.junit.Test;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

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
    public void Given_nullStatuses_When_getStatuses_Then_ReturnsEmptyList() throws Exception {
        Game game = GameFactory.provide(10, "title");
        GameRelation result = GameRelation.create(game, 100);
        result.setStatuses(null);

        // Assert
        assertNotNull(result.getStatuses());
        assertTrue(result.getStatuses().isEmpty());
    }

    @Test
    public void given_emptyStatus_When_getTotalHoursWithStatus_Then_Returns0() throws Exception {
        Game game = GameFactory.provide(10, "title");
        GameRelation result = GameRelation.create(game, 100);

        // Assert
        assertEquals(0, result.getTotalHoursWithStatus(RelationInterval.RelationType.DONE));
    }

    @Test
    public void given_multipleStatus_When_getTotalHoursWithStatus_Then_ReturnsHoursCount() throws Exception {

        Game game = GameFactory.provide(10, "title");
        GameRelation result = GameRelation.create(game, 100);
        RelationInterval interval1 = RelationInterval.create(1, RelationInterval.RelationType.DONE, 5000);
        interval1.setEndAt(5000+ TimeUnit.HOURS.toMillis(2));
        RelationInterval interval2 = RelationInterval.create(2, RelationInterval.RelationType.PLAYING, 5000);
        interval2.setEndAt(5000+ TimeUnit.HOURS.toMillis(5));
        RelationInterval interval3 = RelationInterval.create(3, RelationInterval.RelationType.PENDING, 5000);
        interval3.setEndAt(5000+ TimeUnit.HOURS.toMillis(1));
        RelationInterval interval4 = RelationInterval.create(4, RelationInterval.RelationType.PLAYING, 5000);
        interval4.setEndAt(5000+ TimeUnit.HOURS.toMillis(14));
        result.getStatuses().add(interval1);
        result.getStatuses().add(interval2);
        result.getStatuses().add(interval3);
        result.getStatuses().add(interval4);

        // Assert
        assertEquals(2, result.getTotalHoursWithStatus(RelationInterval.RelationType.DONE));
        assertEquals(19, result.getTotalHoursWithStatus(RelationInterval.RelationType.PLAYING));
        assertEquals(1, result.getTotalHoursWithStatus(RelationInterval.RelationType.PENDING));
    }

    @Test
    public void given_emptyStatus_When_getNumberOfTimesInStatus_Then_Returns0() throws Exception {
        Game game = GameFactory.provide(10, "title");
        GameRelation result = GameRelation.create(game, 100);

        // Assert
        assertEquals(0, result.getNumberOfTimesInStatus(RelationInterval.RelationType.DONE));
    }

    @Test
    public void given_multipleStatus_When_getNumberOfTimesInStatus_Then_ReturnsTimesCount() throws Exception {

        Game game = GameFactory.provide(10, "title");
        GameRelation result = GameRelation.create(game, 100);
        RelationInterval interval1 = RelationInterval.create(1, RelationInterval.RelationType.DONE, 5000);
        RelationInterval interval2 = RelationInterval.create(2, RelationInterval.RelationType.PLAYING, 5000);
        RelationInterval interval3 = RelationInterval.create(3, RelationInterval.RelationType.PENDING, 5000);
        RelationInterval interval4 = RelationInterval.create(4, RelationInterval.RelationType.PLAYING, 5000);
        RelationInterval interval5 = RelationInterval.create(5, RelationInterval.RelationType.PLAYING, 5000);
        RelationInterval interval6 = RelationInterval.create(5, RelationInterval.RelationType.PENDING, 5000);
        result.getStatuses().add(interval1);
        result.getStatuses().add(interval2);
        result.getStatuses().add(interval3);
        result.getStatuses().add(interval4);
        result.getStatuses().add(interval5);
        result.getStatuses().add(interval6);

        // Assert
        assertEquals(1, result.getNumberOfTimesInStatus(RelationInterval.RelationType.DONE));
        assertEquals(3, result.getNumberOfTimesInStatus(RelationInterval.RelationType.PLAYING));
        assertEquals(2, result.getNumberOfTimesInStatus(RelationInterval.RelationType.PENDING));
    }
}