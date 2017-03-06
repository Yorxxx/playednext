package com.piticlistudio.playednext.gamerelease.model.entity.datasource;

import com.piticlistudio.playednext.mvp.model.entity.NetworkEntityIdRelation;
import com.piticlistudio.playednext.platform.model.entity.datasource.IPlatformData;
import com.piticlistudio.playednext.platform.model.entity.datasource.RealmPlatform;
import com.piticlistudio.playednext.releasedate.model.entity.datasource.RealmReleaseDate;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
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
    public void given_RealmGameRelease_When_GetPlatform_Then_ReturnsPlatform() throws Exception {

        RealmGameRelease data = new RealmGameRelease();
        data.setPlatform(platform);
        data.setRelease(date);

        // Act
        NetworkEntityIdRelation<IPlatformData> result = data.getPlatform();

        assertEquals(platform.getId(), result.id);
        assertTrue(result.data.isPresent());
        assertEquals(platform, result.data.get());
    }

    @Test
    public void given_RealmGameRelease_When_GetDate_Then_ReturnsDate() throws Exception {

        assertEquals(date, data.getDate());
    }
}