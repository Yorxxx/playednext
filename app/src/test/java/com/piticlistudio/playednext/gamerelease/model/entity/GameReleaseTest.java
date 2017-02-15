package com.piticlistudio.playednext.gamerelease.model.entity;

import com.piticlistudio.playednext.platform.model.entity.Platform;
import com.piticlistudio.playednext.releasedate.model.entity.ReleaseDate;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test cases
 * Created by jorge.garcia on 15/02/2017.
 */
public class GameReleaseTest {

    @Test
    public void create() throws Exception {
        Platform platform = Platform.create(50, "name");
        ReleaseDate date = ReleaseDate.create(1000, "human");
        GameRelease data = GameRelease.create(platform, date);

        // Assert
        assertNotNull(data);
        assertEquals(platform, data.platform());
        assertEquals(date, data.releaseDate());
    }

}