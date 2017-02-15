package com.piticlistudio.playednext.gamerelease.model.entity.datasource;

import com.piticlistudio.playednext.mvp.model.entity.NetworkEntityIdRelation;
import com.piticlistudio.playednext.platform.model.entity.datasource.IPlatformData;
import com.piticlistudio.playednext.platform.model.entity.datasource.RealmPlatform;
import com.piticlistudio.playednext.releasedate.model.entity.datasource.RealmReleaseDate;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Test cases
 * Created by jorge.garcia on 15/02/2017.
 */
public class RealmGameReleaseTest {

    private final RealmPlatform platform = new RealmPlatform(50, "platform");
    private final RealmReleaseDate date = new RealmReleaseDate("human", 2000);
    private final RealmGameRelease data = new RealmGameRelease(platform, date);


    @Test
    public void getPlatform() throws Exception {

        NetworkEntityIdRelation<IPlatformData> result = data.getPlatform();

        assertNotNull(result);
        assertEquals(platform.getId(), result.id);
        assertTrue(result.data.isPresent());
        assertEquals(platform, result.data.get());
    }

    @Test
    public void getDate() throws Exception {

        assertEquals(date, data.getDate());
    }

}