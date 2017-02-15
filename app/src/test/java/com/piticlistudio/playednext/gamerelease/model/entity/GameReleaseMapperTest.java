package com.piticlistudio.playednext.gamerelease.model.entity;

import com.fernandocejas.arrow.optional.Optional;
import com.piticlistudio.playednext.BaseTest;
import com.piticlistudio.playednext.gamerelease.model.entity.datasource.IGDBGameRelease;
import com.piticlistudio.playednext.gamerelease.model.entity.datasource.RealmGameRelease;
import com.piticlistudio.playednext.platform.model.entity.Platform;
import com.piticlistudio.playednext.platform.model.entity.PlatformMapper;
import com.piticlistudio.playednext.platform.model.entity.datasource.NetPlatform;
import com.piticlistudio.playednext.platform.model.entity.datasource.RealmPlatform;
import com.piticlistudio.playednext.releasedate.model.entity.ReleaseDate;
import com.piticlistudio.playednext.releasedate.model.entity.ReleaseDateMapper;
import com.piticlistudio.playednext.releasedate.model.entity.datasource.NetReleaseDate;
import com.piticlistudio.playednext.releasedate.model.entity.datasource.RealmReleaseDate;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;

/**
 * Test cases
 * Created by jorge.garcia on 15/02/2017.
 */
public class GameReleaseMapperTest extends BaseTest {

    @Mock
    ReleaseDateMapper dateMapper;

    @Mock
    PlatformMapper platformMapper;

    @InjectMocks
    GameReleaseMapper mapper;

    @Test
    public void transform_null() throws Exception {

        IGDBGameRelease release = null;

        // Act
        Optional<GameRelease> result = mapper.transform(release);

        // Assert
        assertNotNull(result);
        assertFalse(result.isPresent());
    }

    @Test
    public void transform_platformMapError() throws Exception {

        doReturn(Optional.absent()).when(platformMapper).transform(any());

        IGDBGameRelease release = IGDBGameRelease.create(1, 1000, "human");

        // Act
        Optional<GameRelease> result = mapper.transform(release);

        // Assert
        assertNotNull(result);
        assertFalse(result.isPresent());

        RealmGameRelease release2 = new RealmGameRelease(new RealmPlatform(1, "name"), new RealmReleaseDate("human", 1000));

        // Act
        result = mapper.transform(release2);

        // Assert
        assertNotNull(result);
        assertFalse(result.isPresent());
    }

    @Test
    public void transform_dateMapError() throws Exception {

        doReturn(Optional.of(Platform.create(50, "platform"))).when(platformMapper).transform(any());
        doReturn(Optional.absent()).when(dateMapper).transform(any());

        RealmGameRelease release = new RealmGameRelease(new RealmPlatform(1, "name"), new RealmReleaseDate("human", 1000));

        // Act
        Optional<GameRelease> result = mapper.transform(release);

        // Assert
        assertNotNull(result);
        assertFalse(result.isPresent());
    }

    @Test
    public void transform() throws Exception {

        final Platform platform = Platform.create(50, "platform");
        final ReleaseDate date = ReleaseDate.create(1000, "human");
        doReturn(Optional.of(platform)).when(platformMapper).transform(any());
        doReturn(Optional.of(date)).when(dateMapper).transform(any());

        RealmGameRelease release = new RealmGameRelease(new RealmPlatform(1, "name"), new RealmReleaseDate("human", 1000));

        // Act
        Optional<GameRelease> result = mapper.transform(release);

        // Assert
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertEquals(platform, result.get().platform());
        assertEquals(date, result.get().releaseDate());
    }
}