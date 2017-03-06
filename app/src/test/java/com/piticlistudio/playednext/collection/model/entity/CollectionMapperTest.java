package com.piticlistudio.playednext.collection.model.entity;

import com.fernandocejas.arrow.optional.Optional;
import com.piticlistudio.playednext.collection.model.entity.datasource.ICollectionData;
import com.piticlistudio.playednext.collection.model.entity.datasource.IGDBCollection;
import com.piticlistudio.playednext.collection.model.entity.datasource.RealmCollection;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
    public void given_nullData_when_transform_Then_ReturnsAbsent() throws Exception {

        ICollectionData collection = null;
        Optional<Collection> result = mapper.transform(collection);

        // Assert
        assertNotNull(result);
        assertFalse(result.isPresent());
    }

    @Test
    public void given_missingNameData_when_tranform_Then_ReturnsAbsent() throws Exception {

        ICollectionData collection = new RealmCollection();
        Optional<Collection> result = mapper.transform(collection);

        // Assert
        assertNotNull(result);
        assertFalse(result.isPresent());
    }

    @Test
    public void given_validData_When_transform_Then_ReturnsMappedResult() throws Exception {

        // Act
        ICollectionData igdbCollection = IGDBCollection.create(0, "name", "url", 1000, 2000, new ArrayList<>());
        Optional<Collection> result = mapper.transform(igdbCollection);

        // Assert
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertEquals(igdbCollection.getId(), result.get().id());
        assertEquals(igdbCollection.getName(), result.get().name());
    }
}