package com.piticlistudio.playednext.platform.model.entity;

import android.graphics.Color;

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

    @Test
    public void getAcronym() throws Exception {

        Platform data = Platform.create(50, "name");
        assertNotNull(data.getAcronym());
        assertEquals("name", data.getAcronym());

        data = Platform.create(50, "Amiga");
        assertNotNull(data.getAcronym());
        assertEquals("Amiga", data.getAcronym());

        data = Platform.create(10, "Xbox One");
        assertNotNull(data.getAcronym());
        assertEquals("One", data.getAcronym());

        data = Platform.create(10, "PC (Microsoft Windows)");
        assertNotNull(data.getAcronym());
        assertEquals("PC Win", data.getAcronym());

        data = Platform.create(10, "PlayStation 4");
        assertNotNull(data.getAcronym());
        assertEquals("Ps4", data.getAcronym());

        data = Platform.create(10, "Amstrad CPC");
        assertNotNull(data.getAcronym());
        assertEquals("A CPC", data.getAcronym());

        data = Platform.create(10, "Atari 8-bit");
        assertNotNull(data.getAcronym());
        assertEquals("A 8-bit", data.getAcronym());

        data = Platform.create(10, "Atari ST/STE");
        assertNotNull(data.getAcronym());
        assertEquals("A ST/STE", data.getAcronym());

        data = Platform.create(10, "Commodore C64/128");
        assertNotNull(data.getAcronym());
        assertEquals("C C64/128", data.getAcronym());

        data = Platform.create(10, "PC DOS");
        assertNotNull(data.getAcronym());
        assertEquals("PC DOS", data.getAcronym());

        data = Platform.create(10, "Apple II");
        assertNotNull(data.getAcronym());
        assertEquals("Apple II", data.getAcronym());
    }

    @Test
    public void getColor() throws Exception {

        Platform data = Platform.create(50, "name");
        assertEquals(Color.WHITE, data.getColor());

        data = Platform.create(10, "Xbox One");
        assertNotEquals(Color.WHITE, data);

        data = Platform.create(10, "PC (Microsoft Windows)");
        assertNotEquals(Color.WHITE, data);

        data = Platform.create(10, "PlayStation 4");
        assertNotEquals(Color.WHITE, data);
    }
}