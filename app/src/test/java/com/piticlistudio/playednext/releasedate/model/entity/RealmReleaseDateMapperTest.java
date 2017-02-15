package com.piticlistudio.playednext.releasedate.model.entity;

import com.fernandocejas.arrow.optional.Optional;
import com.piticlistudio.playednext.BaseTest;
import com.piticlistudio.playednext.releasedate.model.entity.datasource.RealmReleaseDate;

import org.junit.Test;
import org.mockito.InjectMocks;

import static org.junit.Assert.*;

/**
 * Test cases
 * Created by jorge.garcia on 15/02/2017.
 */
public class RealmReleaseDateMapperTest extends BaseTest {

    @InjectMocks
    RealmReleaseDateMapper mapper;

    @Test
    public void transform() throws Exception {

        ReleaseDate date = null;

        // Act
        Optional<RealmReleaseDate> result = mapper.transform(date);

        // Assert
        assertNotNull(result);
        assertFalse(result.isPresent());

        date = ReleaseDate.create(0, "human");

        // Act
        result = mapper.transform(date);

        // Assert
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertFalse(result.get().getDate().isPresent());
        assertEquals("human", result.get().getHumanDate());
    }

}