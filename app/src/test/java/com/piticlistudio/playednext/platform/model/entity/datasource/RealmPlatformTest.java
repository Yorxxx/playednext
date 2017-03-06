package com.piticlistudio.playednext.platform.model.entity.datasource;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test cases for RealmPlatform
 * Created by jorge.garcia on 14/02/2017.
 */
public class RealmPlatformTest {

    RealmPlatform data = new RealmPlatform(50, "name");

    @Test
    public void getId() throws Exception {

        data = new RealmPlatform();
        data.setId(100);

        assertEquals(100, data.getId());
    }

    @Test
    public void getName() throws Exception {
        assertEquals("name", data.getName());
    }

}