package com.piticlistudio.playednext.gamerelease.model.entity.datasource;

import com.piticlistudio.playednext.mvp.model.entity.NetworkEntityIdRelation;
import com.piticlistudio.playednext.platform.model.entity.datasource.IPlatformData;
import com.piticlistudio.playednext.releasedate.model.entity.datasource.IReleaseDateData;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test cases
 * Created by jorge.garcia on 15/02/2017.
 */
public class IGDBGameReleaseTest {

    @Test
    public void create() throws Exception {

        IGDBGameRelease data = IGDBGameRelease.create(10, 1000, "human");

        assertNotNull(data);
        assertEquals(10, data.platform());
        assertEquals(1000, data.date());
        assertEquals("human", data.human());
    }

    @Test
    public void getPlatform() throws Exception {

        IGDBGameRelease data = IGDBGameRelease.create(10, 1000, "human");

        NetworkEntityIdRelation<IPlatformData> result = data.getPlatform();

        assertNotNull(result);
        assertEquals(10, result.id);
        assertFalse(result.data.isPresent());
    }

    @Test
    public void getDate() throws Exception {

        IGDBGameRelease data = IGDBGameRelease.create(10, 1000, "human");

        IReleaseDateData date = data.getDate();

        assertNotNull(date);
        assertTrue(date.getDate().isPresent());
        assertEquals(1000, (long)date.getDate().get());
        assertEquals("human", date.getHumanDate());
    }

}