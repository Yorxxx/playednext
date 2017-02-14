package com.piticlistudio.playednext.platform.model.entity;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test cases
 * Created by jorge.garcia on 14/02/2017.
 */
public class PlatformTest {
    @Test
    public void create() throws Exception {
        Platform data = Platform.create(50, "name");

        assertNotNull(data);
        assertEquals(50, data.id());
        assertEquals("name", data.name());
    }

}