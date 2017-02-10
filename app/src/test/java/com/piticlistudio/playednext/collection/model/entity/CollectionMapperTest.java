package com.piticlistudio.playednext.collection.model.entity;

import com.fernandocejas.arrow.optional.Optional;
import com.piticlistudio.playednext.collection.model.entity.datasource.NetCollection;
import com.piticlistudio.playednext.collection.model.entity.datasource.RealmCollection;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Test cases
 * Created by jorge.garcia on 10/02/2017.
 */
public class CollectionMapperTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @InjectMocks
    CollectionMapper mapper;

    @Test
    public void transform() throws Exception {

        Optional<Collection> result = mapper.transform(new RealmCollection());

        // Assert
        assertNotNull(result);
        assertFalse(result.isPresent());

        RealmCollection collection = new RealmCollection(0, "name");
        result = mapper.transform(collection);

        // Assert
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertEquals(collection.getId(), result.get().id());
        assertEquals(collection.getName(), result.get().name());

        // Act
        NetCollection netCollection = NetCollection.create(0, "name", "url", 1000, 2000, new ArrayList<>());
        result = mapper.transform(netCollection);

        // Assert
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertEquals(netCollection.id(), result.get().id());
        assertEquals(netCollection.name(), result.get().name());
    }

}