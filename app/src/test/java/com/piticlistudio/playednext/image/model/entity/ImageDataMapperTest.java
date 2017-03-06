package com.piticlistudio.playednext.image.model.entity;

import com.fernandocejas.arrow.optional.Optional;
import com.piticlistudio.playednext.BaseTest;
import com.piticlistudio.playednext.image.model.entity.datasource.IImageData;
import com.piticlistudio.playednext.image.model.entity.datasource.RealmImageData;

import org.junit.Test;
import org.mockito.InjectMocks;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Test cases for ImageDataMapper
 * Created by jorge.garcia on 13/02/2017.
 */
public class ImageDataMapperTest extends BaseTest {

    @InjectMocks
    ImageDataMapper mapper;

    @Test
    public void given_nullData_When_Transform_Then_ReturnsAbsent() throws Exception {
        IImageData data = null;
        // Act
        Optional<ImageData> result = mapper.transform(data);

        // Assert
        assertNotNull(result);
        assertFalse(result.isPresent());
    }

    @Test
    public void given_missingIdData_When_Transform_Then_ReturnsAbsent() throws Exception {

        IImageData data = new RealmImageData(null, "url", 200, 200);
        // Act
        Optional<ImageData> result = mapper.transform(data);

        // Assert
        assertNotNull(result);
        assertFalse(result.isPresent());
    }

    @Test
    public void given_missingUrlData_When_Transform_Then_ReturnsAbsent() throws Exception {
        IImageData data = new RealmImageData("10", null, 200, 200);
        // Act
        Optional<ImageData> result = mapper.transform(data);

        // Assert
        assertNotNull(result);
        assertFalse(result.isPresent());
    }

    @Test
    public void given_validData_When_Transform_Then_MapsData() throws Exception {
        IImageData data = new RealmImageData("id", "url", 200, 200);

        // Act
        Optional<ImageData> result = mapper.transform(data);

        // Assert
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertEquals(data.getId(), result.get().id());
        assertEquals(data.getUrl(), result.get().thumbUrl());
        assertEquals(data.getHeight(), result.get().fullHeight());
        assertEquals(data.getWidth(), result.get().fullWidth());
    }
}