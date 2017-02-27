package com.piticlistudio.playednext.relationinterval.model.entity;

import com.piticlistudio.playednext.BaseTest;

import org.junit.Test;

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
}