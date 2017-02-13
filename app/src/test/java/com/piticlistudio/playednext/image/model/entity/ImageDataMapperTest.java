package com.piticlistudio.playednext.image.model.entity;

import com.fernandocejas.arrow.optional.Optional;
import com.piticlistudio.playednext.BaseTest;
import com.piticlistudio.playednext.image.model.entity.datasource.RealmImageData;

import org.junit.Test;
import org.mockito.InjectMocks;

import static org.junit.Assert.*;

/**
 * Test cases for ImageDataMapper
 * Created by jorge.garcia on 13/02/2017.
 */
public class ImageDataMapperTest extends BaseTest {

    @InjectMocks
    ImageDataMapper mapper;

    @Test
    public void transform() throws Exception {

        RealmImageData data = null;
        // Act
        Optional<ImageData> result = mapper.transform(data);

        // Assert
        assertNotNull(result);
        assertFalse(result.isPresent());

        // Arrange
        data = new RealmImageData(null, "url", 200, 200);

        // Act
        result = mapper.transform(data);

        // Assert
        assertNotNull(result);
        assertFalse(result.isPresent());


        // Arrange
        data = new RealmImageData("id", "url", 200, 200);

        // Act
        result = mapper.transform(data);

        // Assert
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertEquals(data.getId(), result.get().id());
        assertEquals(data.getUrl(), result.get().thumbUrl());
        assertEquals(data.getHeight(), result.get().fullHeight());
        assertEquals(data.getWidth(), result.get().fullWidth());
    }

}