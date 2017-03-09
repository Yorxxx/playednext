package com.piticlistudio.playednext.boost.model.entity;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test cases for BoostTypes
 * Created by jorge.garcia on 09/03/2017.
 */
public class BoostTypesTest {

    @Test
    public void given_recentlyReleased_When_getValues_Then_Returns500Boost() throws Exception {

        assertEquals(500, BoostTypes.RECENTLY_RELEASED.value);
        assertEquals(1, BoostTypes.RECENTLY_RELEASED.id);
    }

    @Test
    public void given_TEN_YEARS_CELEBRATION_when_getValues_Then_Returns2000Boost() throws Exception {
        assertEquals(2000, BoostTypes.TEN_YEARS_CELEBRATION.value);
        assertEquals(2, BoostTypes.TEN_YEARS_CELEBRATION.id);
    }

    @Test
    public void given_waitingTime_When_getValues_Then_ReturnsZero() throws Exception {
        assertEquals(0, BoostTypes.WAITING_TIME.value);
        assertEquals(3, BoostTypes.WAITING_TIME.id);
    }

    @Test
    public void given_completedOnce_When_getValues_Then_ReturnsNegativeBoost() throws Exception {
        assertEquals(-100, BoostTypes.COMPLETED_COUNT.value);
        assertEquals(4, BoostTypes.COMPLETED_COUNT.id);
    }
}