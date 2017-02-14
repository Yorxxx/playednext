package com.piticlistudio.playednext.image.model.entity;

import com.fernandocejas.arrow.optional.Optional;
import com.piticlistudio.playednext.BaseTest;
import com.piticlistudio.playednext.image.model.entity.datasource.RealmImageData;

import org.junit.Test;
import org.mockito.InjectMocks;

import static org.junit.Assert.*;

/**
 * Test cases
 * Created by jorge.garcia on 14/02/2017.
 */
public class RealmImageDataMapperTest extends BaseTest {

    @InjectMocks
    RealmImageDataMapper imageMapper;

    @Test
    public void transform() throws Exception {

        ImageData data = null;

        // Act
        Optional<RealmImageData> result = imageMapper.transform(data);

        // Assert
        assertNotNull(result);
        assertFalse(result.isPresent());

        data = ImageData.create("10", 50, 100, "url");

        // Act
        result = imageMapper.transform(data);

        // Assert
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertEquals(data.id(), result.get().getId());
        assertEquals(data.fullHeight(), result.get().getHeight());
        assertEquals(data.fullWidth(), result.get().getWidth());
        assertEquals(data.thumbUrl(), result.get().getUrl());
    }

}