package com.piticlistudio.playednext.relationinterval.model.entity;

import com.piticlistudio.playednext.BaseTest;

import org.junit.Test;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * Test cases
 * Created by jorge.garcia on 27/02/2017.
 */
public class RelationIntervalTest extends BaseTest {

    @Test
    public void given_empty_When_CreatesNewInterval_Then_ReturnsNewInterval() throws Exception {

        RelationInterval data = RelationInterval.create(50, RelationInterval.RelationType.PLAYING, 5000);

        assertNotNull(data);
        assertEquals(50, data.id());
        assertEquals(RelationInterval.RelationType.PLAYING, data.type());
        assertEquals(5000, data.startAt());
        assertEquals(0, data.getEndAt());
    }

    @Test
    public void given_4hoursInterval_When_getHours_Then_Returns4() throws Exception {

        long startAt = 5000;
        long endAt = startAt + TimeUnit.HOURS.toMillis(4);
        RelationInterval data = RelationInterval.create(50, RelationInterval.RelationType.PLAYING, 5000);
        data.setEndAt(endAt);

        // Act
        double value = data.getHours();

        // Assert
        assertEquals(4, value, 0);
    }

    @Test
    public void given_notFinishedInterval_When_getHours_Then_ComparesAgainstCurrentTime() throws Exception {

        long startAt = System.currentTimeMillis() - TimeUnit.HOURS.toMillis(10);
        RelationInterval data = RelationInterval.create(50, RelationInterval.RelationType.PLAYING, startAt);

        // Act
        double value = data.getHours();

        // Assert
        assertEquals(10, value, 0);
    }
}