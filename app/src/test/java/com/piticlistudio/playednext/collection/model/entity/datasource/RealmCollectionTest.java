package com.piticlistudio.playednext.collection.model.entity.datasource;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test cases
 * Created by jorge.garcia on 10/02/2017.
 */
public class RealmCollectionTest {

    RealmCollection data = new RealmCollection(10, "collection");

    @Test
    public void given_constructorWithArguments_When_constructs_Then_ReturnsData() throws Exception {

        RealmCollection data = new RealmCollection(10, "collection");
        assertNotNull(data);
        assertEquals(10, data.getId());
        assertEquals("collection", data.getName());
    }

    @Test
    public void given_id_when_getId_Then_ReturnsId() throws Exception {

        data = new RealmCollection();
        data.setId(100);

        assertEquals(100, data.getId());
    }

    @Test
    public void given_name_when_getName_Then_ReturnsName() throws Exception {

        data = new RealmCollection();
        data.setName("name");

        assertEquals("name", data.getName());

    }
}