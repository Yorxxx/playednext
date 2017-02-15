package com.piticlistudio.playednext.gamerelease.model.entity;

import com.fernandocejas.arrow.optional.Optional;
import com.piticlistudio.playednext.BaseTest;
import com.piticlistudio.playednext.gamerelease.model.entity.datasource.RealmGameRelease;
import com.piticlistudio.playednext.platform.model.entity.Platform;
import com.piticlistudio.playednext.platform.model.entity.RealmPlatformMapper;
import com.piticlistudio.playednext.platform.model.entity.datasource.RealmPlatform;
import com.piticlistudio.playednext.releasedate.model.entity.RealmReleaseDateMapper;
import com.piticlistudio.playednext.releasedate.model.entity.ReleaseDate;
import com.piticlistudio.playednext.releasedate.model.entity.datasource.RealmReleaseDate;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;

/**
 * Test cases
 * Created by jorge.garcia on 15/02/2017.
 */
public class RealmGameReleaseMapperTest extends BaseTest {

    @Mock
    RealmPlatformMapper platformMapper;

    @Mock
    RealmReleaseDateMapper dateMapper;

    @InjectMocks
    RealmGameReleaseMapper mapper;

    @Test
    public void transform_null() throws Exception {

        GameRelease data = null;

        // Act
        Optional<RealmGameRelease> result = mapper.transform(data);

        // Assert
        assertNotNull(result);
        assertFalse(result.isPresent());
    }

    @Test
    public void transform_platformMapError() throws Exception {

        Platform platform = Platform.create(50, "name");
        ReleaseDate date = ReleaseDate.create(1000, "human");
        GameRelease data = GameRelease.create(platform, date);

        doReturn(Optional.absent()).when(platformMapper).transform(platform);

        // Act
        // Act
        Optional<RealmGameRelease> result = mapper.transform(data);

        // Assert
        assertNotNull(result);
        assertFalse(result.isPresent());
    }

    @Test
    public void transform_dateMapError() throws Exception {

        Platform platform = Platform.create(50, "name");
        ReleaseDate date = ReleaseDate.create(1000, "human");
        GameRelease data = GameRelease.create(platform, date);

        RealmPlatform realmPlatform = new RealmPlatform(50, "name");
        doReturn(Optional.of(realmPlatform)).when(platformMapper).transform(platform);
        doReturn(Optional.absent()).when(dateMapper).transform(date);

        // Act
        // Act
        Optional<RealmGameRelease> result = mapper.transform(data);

        // Assert
        assertNotNull(result);
        assertFalse(result.isPresent());
    }

    @Test
    public void transform() throws Exception {

        Platform platform = Platform.create(50, "name");
        ReleaseDate date = ReleaseDate.create(1000, "human");
        GameRelease data = GameRelease.create(platform, date);

        RealmPlatform realmPlatform = new RealmPlatform(50, "name");
        RealmReleaseDate realmDate = new RealmReleaseDate("human", 1000);
        doReturn(Optional.of(realmPlatform)).when(platformMapper).transform(platform);
        doReturn(Optional.of(realmDate)).when(dateMapper).transform(date);

        // Act
        // Act
        Optional<RealmGameRelease> result = mapper.transform(data);

        // Assert
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertEquals(realmDate, result.get().getDate());
        assertTrue(result.get().getPlatform().data.isPresent());
        assertEquals(realmPlatform, result.get().getPlatform().getData());
        assertEquals(realmPlatform.getId(), result.get().getPlatform().id);
    }
}