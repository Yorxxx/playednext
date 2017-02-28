package com.piticlistudio.playednext.platform.model.entity;

import android.graphics.Color;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by jorge.garcia on 21/02/2017.
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

        data = Platform.create(10, "Super Nintendo Entertainment System (SNES)");
        assertNotNull(data.getAcronym());
        assertEquals("SNES", data.getAcronym());

        data = Platform.create(10, "Super Famicom");
        assertNotNull(data.getAcronym());
        assertEquals("S Famicom", data.getAcronym());

        data = Platform.create(10, "Nintendo GameCube");
        assertNotNull(data.getAcronym());
        assertEquals("NGC", data.getAcronym());

        data = Platform.create(10, "Game Boy Advance");
        assertNotNull(data.getAcronym());
        assertEquals("GBA", data.getAcronym());

        data = Platform.create(10, "Virtual Console (Nintendo)");
        assertNotNull(data.getAcronym());
        assertEquals("VC", data.getAcronym());

        data = Platform.create(10, "Nintendo 64");
        assertNotNull(data.getAcronym());
        assertEquals("N64", data.getAcronym());

        data = Platform.create(10, "Nintendo 3DS");
        assertNotNull(data.getAcronym());
        assertEquals("N3DS", data.getAcronym());

        data = Platform.create(10, "Nintendo DS");
        assertNotNull(data.getAcronym());
        assertEquals("NDS", data.getAcronym());

        data = Platform.create(10, "Nintendo Switch");
        assertNotNull(data.getAcronym());
        assertEquals("Switch", data.getAcronym());

        data = Platform.create(10, "PlayStation 3");
        assertNotNull(data.getAcronym());
        assertEquals("Ps3", data.getAcronym());

        data = Platform.create(10, "PlayStation Vita");
        assertNotNull(data.getAcronym());
        assertEquals("Vita", data.getAcronym());

        data = Platform.create(10, "Xbox 360");
        assertNotNull(data.getAcronym());
        assertEquals("X360", data.getAcronym());

        data = Platform.create(10, "Xbox");
        assertNotNull(data.getAcronym());
        assertEquals("Xbox", data.getAcronym());

        data = Platform.create(10, "Sega Mega Drive/Genesis");
        assertNotNull(data.getAcronym());
        assertEquals("MD", data.getAcronym());

        data = Platform.create(10, "Nintendo Entertainment System (NES)");
        assertNotNull(data.getAcronym());
        assertEquals("NES", data.getAcronym());

        data = Platform.create(10, "Game Boy");
        assertNotNull(data.getAcronym());
        assertEquals("GB", data.getAcronym());
    }

    @Test
    public void getColor() throws Exception {

        Platform data = Platform.create(50, "name");
        assertEquals(Color.WHITE, data.getColor());

        data = Platform.create(10, "Xbox One");
        assertNotEquals(Color.WHITE, data.getColor());

        data = Platform.create(10, "PC (Microsoft Windows)");
        assertNotEquals(Color.WHITE, data.getColor());

        data = Platform.create(10, "PlayStation 4");
        assertNotEquals(Color.WHITE, data.getColor());

        data = Platform.create(10, "Super Nintendo Entertainment System (SNES)");
        assertNotEquals(Color.WHITE, data.getColor());

        data = Platform.create(10, "Super Famicom");
        assertNotEquals(Color.WHITE, data.getColor());

        data = Platform.create(10, "Nintendo GameCube");
        assertNotEquals(Color.WHITE, data.getColor());

        data = Platform.create(10, "Game Boy Advance");
        assertNotEquals(Color.WHITE, data.getColor());

        data = Platform.create(10, "Virtual Console (Nintendo)");
        assertEquals(Color.WHITE, data.getColor());

        data = Platform.create(10, "Nintendo 64");
        assertNotEquals(Color.WHITE, data.getColor());

        data = Platform.create(10, "Nintendo 3DS");
        assertNotEquals(Color.WHITE, data.getColor());

        data = Platform.create(10, "Nintendo DS");
        assertNotEquals(Color.WHITE, data.getColor());

        data = Platform.create(10, "Nintendo Switch");
        assertNotEquals(Color.WHITE, data.getColor());

        data = Platform.create(10, "PlayStation 3");
        assertNotEquals(Color.WHITE, data.getColor());

        data = Platform.create(10, "PlayStation Vita");
        assertNotEquals(Color.WHITE, data.getColor());

        data = Platform.create(10, "Xbox 360");
        assertNotEquals(Color.WHITE, data.getColor());

        data = Platform.create(10, "Xbox");
        assertNotEquals(Color.WHITE, data.getColor());

        data = Platform.create(10, "Sega Mega Drive/Genesis");
        assertNotEquals(Color.WHITE, data.getColor());

        data = Platform.create(10, "Nintendo Entertainment System (NES)");
        assertNotEquals(Color.WHITE, data.getColor());

        data = Platform.create(10, "Game Boy");
        assertNotEquals(Color.WHITE, data.getColor());
    }

}