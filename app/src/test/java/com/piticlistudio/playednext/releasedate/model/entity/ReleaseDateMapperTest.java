package com.piticlistudio.playednext.releasedate.model.entity;

import com.fernandocejas.arrow.optional.Optional;
import com.piticlistudio.playednext.BaseTest;
import com.piticlistudio.playednext.releasedate.model.entity.datasource.IReleaseDateData;
import com.piticlistudio.playednext.releasedate.model.entity.datasource.RealmReleaseDate;

import org.junit.Test;
import org.mockito.InjectMocks;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Test cases for ReleaseDateMapper
 * Created by jorge.garcia on 15/02/2017.
 */
public class ReleaseDateMapperTest extends BaseTest {

    @InjectMocks
    ReleaseDateMapper mapper;

    @Test
    public void given_nullData_When_transform_Then_ReturnsAbsent() throws Exception {

        IReleaseDateData data = null;

        // Act
        Optional<ReleaseDate> result = mapper.transform(data);

        // Assert
        assertNotNull(result);
        assertFalse(result.isPresent());
    }

    @Test
    public void given_nullHumanDate_When_Transform_Then_ReturnsAbsent() throws Exception {

        IReleaseDateData data = new RealmReleaseDate(null, 0);

        // Act
        Optional<ReleaseDate> result = mapper.transform(data);

        // Assert
        assertNotNull(result);
        assertFalse(result.isPresent());
    }

    @Test
    public void given_nonPresentDate_When_Transform_Then_ReturnsAbsent() throws Exception {

        IReleaseDateData data = new RealmReleaseDate("human", 0);

        // Act
        Optional<ReleaseDate> result = mapper.transform(data);

        // Assert
        assertNotNull(result);
        assertFalse(result.isPresent());
    }

    @Test
    public void given_validData_When_Transform_Then_ReturnsMappedData() throws Exception {

        IReleaseDateData data = new RealmReleaseDate("human", 1000);

        // Act
        Optional<ReleaseDate> result = mapper.transform(data);

        // Assert
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertEquals("human", result.get().humanDate());
        assertEquals(1000, result.get().date());
    }
}