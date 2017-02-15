package com.piticlistudio.playednext.releasedate.model.entity;

import com.fernandocejas.arrow.optional.Optional;
import com.piticlistudio.playednext.BaseTest;
import com.piticlistudio.playednext.releasedate.model.entity.datasource.RealmReleaseDate;

import org.junit.Test;
import org.mockito.InjectMocks;

import static org.junit.Assert.*;

/**
 * Test cases for ReleaseDateMapper
 * Created by jorge.garcia on 15/02/2017.
 */
public class ReleaseDateMapperTest extends BaseTest {

    @InjectMocks
    ReleaseDateMapper mapper;

    @Test
    public void transform() throws Exception {

        RealmReleaseDate data = null;

        // Act
        Optional<ReleaseDate> result = mapper.transform(data);

        // Assert
        assertNotNull(result);
        assertFalse(result.isPresent());

        // Arrange
        data = new RealmReleaseDate(null, 0);

        // Act
        result = mapper.transform(data);

        // Assert
        assertNotNull(result);
        assertFalse(result.isPresent());

        // Arrange
        data = new RealmReleaseDate("human", 50);

        // Act
        result = mapper.transform(data);

        // Assert
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertEquals("human", result.get().humanDate());
        assertEquals(50, result.get().date());
    }

}