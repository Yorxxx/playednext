package com.piticlistudio.playednext.relationinterval.model.entity.datasource;

import com.piticlistudio.playednext.BaseTest;
import com.piticlistudio.playednext.relationinterval.model.entity.RelationInterval;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test cases for RealmRelationInterval
 * Created by jorge.garcia on 27/02/2017.
 */
public class RealmRelationIntervalTest extends BaseTest {


    @Test
    public void given_relationInterval_When_RequestsId_Then_ReturnsId() throws Exception {

        RealmRelationInterval interval = new RealmRelationInterval();
        interval.setId(50);

        assertEquals(50, interval.getId());
    }

    @Test
    public void given_emptyRelationInterval_When_GetType_Then_ReturnsType() throws Exception {

        RealmRelationInterval interval = new RealmRelationInterval();
        assertEquals(RelationInterval.RelationType.NONE, interval.getType());
    }

    @Test
    public void given_playingRelationInterval_When_GetType_Then_ReturnsPlaying() throws Exception {

        RealmRelationInterval interval = new RealmRelationInterval();
        interval.setType(RelationInterval.RelationType.PLAYING.ordinal());

        assertEquals(RelationInterval.RelationType.PLAYING, interval.getType());
    }

    @Test
    public void given_emptyRelationInterval_When_GetStartedAt_Then_ReturnsZero() throws Exception {

        RealmRelationInterval interval = new RealmRelationInterval();
        assertEquals(0, interval.getStartedAt());
    }

    @Test
    public void given_startedRelationInterval_When_GetStartedAt_Then_ReturnsTimestamp() throws Exception {

        RealmRelationInterval interval = new RealmRelationInterval();
        interval.setStartedAt(5000);

        assertEquals(5000, interval.getStartedAt());
    }

    @Test
    public void given_emptyRelationInterval_When_GetEndedAt_Then_ReturnsZero() throws Exception {

        RealmRelationInterval interval = new RealmRelationInterval();
        assertEquals(0, interval.getEndedAt());
    }

    @Test
    public void given_endedRelationInterval_When_GetEndedAt_Then_ReturnsTimestamp() throws Exception {

        RealmRelationInterval interval = new RealmRelationInterval();
        interval.setEndedAt(5000);
        assertEquals(5000, interval.getEndedAt());
    }

    @Test
    public void given_notEndedRelationInterval_When_GetEndedAt_Then_ReturnsZero() throws Exception {
        RealmRelationInterval interval = new RealmRelationInterval();
        interval.setStartedAt(5000);
        interval.setId(50);
        interval.setType(RelationInterval.RelationType.PLAYING.ordinal());

        assertEquals(0, interval.getEndedAt());
    }
}