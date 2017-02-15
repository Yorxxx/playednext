package com.piticlistudio.playednext.platform.model.entity;

import com.fernandocejas.arrow.optional.Optional;
import com.piticlistudio.playednext.BaseTest;
import com.piticlistudio.playednext.platform.model.entity.datasource.RealmPlatform;

import org.junit.Test;
import org.mockito.InjectMocks;

import static org.junit.Assert.*;

/**
 * Test cases for PlatformMapper
 * Created by jorge.garcia on 15/02/2017.
 */
public class PlatformMapperTest extends BaseTest {

    @InjectMocks
    PlatformMapper mapper;

    @Test
    public void transform() throws Exception {

        RealmPlatform data = null;

        // Act
        Optional<Platform> result = mapper.transform(data);

        // Assert
        assertNotNull(result);
        assertFalse(result.isPresent());

        data = new RealmPlatform(10, null);

        // Act
        result = mapper.transform(data);

        // Assert
        assertNotNull(result);
        assertFalse(result.isPresent());

        // Arrange
        data.setName("name");

        // Act
        result = mapper.transform(data);

        // Assert
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertEquals(data.getId(), result.get().id());
        assertEquals(data.getName(), result.get().name());
    }

}