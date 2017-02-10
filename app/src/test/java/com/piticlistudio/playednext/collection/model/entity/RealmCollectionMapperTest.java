package com.piticlistudio.playednext.collection.model.entity;

import com.fernandocejas.arrow.optional.Optional;
import com.piticlistudio.playednext.collection.model.entity.datasource.RealmCollection;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.junit.Assert.*;

/**
 * Test cases
 * Created by jorge.garcia on 10/02/2017.
 */
public class RealmCollectionMapperTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @InjectMocks
    RealmCollectionMapper mapper;

    @Test
    public void transform() throws Exception {
        Collection data = null;
        Optional<RealmCollection> result = mapper.transform(data);

        // Assert
        assertNotNull(result);
        assertFalse(result.isPresent());

        data = Collection.create(0, "name");
        result = mapper.transform(data);

        // Assert
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertEquals(data.id(), result.get().getId());
        assertEquals(data.name(), result.get().getName());
    }

}