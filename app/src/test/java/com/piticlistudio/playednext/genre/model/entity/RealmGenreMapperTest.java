package com.piticlistudio.playednext.genre.model.entity;

import com.fernandocejas.arrow.optional.Optional;
import com.piticlistudio.playednext.BaseTest;
import com.piticlistudio.playednext.genre.model.entity.datasource.RealmGenre;

import org.junit.Test;
import org.mockito.InjectMocks;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Test cases for RealmGenreMapper
 * Created by jorge.garcia on 14/02/2017.
 */
public class RealmGenreMapperTest extends BaseTest {

    @InjectMocks
    RealmGenreMapper mapper;

    @Test
    public void transform() throws Exception {

        Genre data = null;

        // Act
        Optional<RealmGenre> result = mapper.transform(data);

        // Assert
        assertNotNull(result);
        assertFalse(result.isPresent());

        data = Genre.create(50, "name");

        // Act
        result = mapper.transform(data);

        // Assert
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertEquals(data.id(), result.get().getId());
        assertEquals(data.name(), result.get().getName());
    }

}