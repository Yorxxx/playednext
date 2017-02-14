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
 * Test cases for GenreMapper
 * Created by jorge.garcia on 14/02/2017.
 */
public class GenreMapperTest extends BaseTest {

    @InjectMocks
    GenreMapper mapper;

    @Test
    public void transform() throws Exception {

        RealmGenre data = null;

        // Act
        Optional<Genre> result = mapper.transform(data);

        // Assert
        assertNotNull(result);
        assertFalse(result.isPresent());

        data = new RealmGenre(50, null);

        // Act
        result = mapper.transform(data);

        // Assert
        assertNotNull(result);
        assertFalse(result.isPresent());

        data.setName("name");

        // Act
        result = mapper.transform(data);

        // Assert
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertEquals(data.getName(), result.get().name());
        assertEquals(data.getId(), result.get().id());
    }

}