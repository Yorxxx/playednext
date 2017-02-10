package com.piticlistudio.playednext.collection.model.entity.datasource;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Test case
 * Created by jorge.garcia on 10/02/2017.
 */
public class NetCollectionTest {

    NetCollection data = NetCollection.create(10, "collection", "url", 1000, 2000, new ArrayList<>());

    @Test
    public void getId() throws Exception {
        assertEquals(data.id(), data.getId());
    }

    @Test
    public void getName() throws Exception {
        assertEquals(data.name(), data.getName());
    }

}