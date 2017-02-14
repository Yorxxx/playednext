package com.piticlistudio.playednext.collection.model.entity.datasource;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test cases
 * Created by jorge.garcia on 10/02/2017.
 */
public class RealmCollectionTest {

    RealmCollection data = new RealmCollection(10, "collection");

    @Test
    public void getId() throws Exception {
        assertEquals(10, data.getId());
    }

    @Test
    public void getName() throws Exception {
        assertEquals("collection", data.getName());
    }

}