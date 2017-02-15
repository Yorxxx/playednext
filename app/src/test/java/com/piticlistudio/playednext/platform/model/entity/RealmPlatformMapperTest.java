package com.piticlistudio.playednext.platform.model.entity;

import com.fernandocejas.arrow.optional.Optional;
import com.piticlistudio.playednext.BaseTest;
import com.piticlistudio.playednext.platform.model.entity.datasource.RealmPlatform;

import org.junit.Test;
import org.mockito.InjectMocks;

import static org.junit.Assert.*;

/**
 * Test cases for RealmPlatformMapper
 * Created by jorge.garcia on 15/02/2017.
 */
public class RealmPlatformMapperTest extends BaseTest {

    @InjectMocks
    RealmPlatformMapper mapper;

    @Test
    public void transform() throws Exception {

        Platform data = null;

        // Act
        Optional<RealmPlatform> result = mapper.transform(data);

        // Assert
        assertNotNull(result);
        assertFalse(result.isPresent());

        // Arrange
        data = Platform.create(10, "name");

        // Act
        result = mapper.transform(data);

        // Assert
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertEquals(data.id(), result.get().getId());
        assertEquals(data.name(), result.get().getName());
    }

}