package com.piticlistudio.playednext.platform.ui;

import android.graphics.Color;
import android.support.test.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;

import static org.junit.Assert.*;

/**
 * Test cases for PlatformUIUtils
 * Created by jorge.garcia on 09/03/2017.
 */
public class PlatformUIUtilsTest {

    private PlatformUIUtils utils = new PlatformUIUtils();

    @Before
    public void setUp() throws Exception {
        InputStream is = InstrumentationRegistry.getTargetContext().getAssets().open("platformsui.json");
        utils.parse(is);
    }

    @Test
    public void given_linux_When_getAcronym_Then_ReturnsLNX() throws Exception {
        assertEquals("LNX", utils.getAcronym("linux"));
    }

    @Test
    public void given_linux_when_getColor_Then_ReturnsYellow() throws Exception {
        assertEquals(Color.parseColor("#ffcc00"), utils.getColor("linux"));
    }

    @Test
    public void given_Linux_When_getAcronym_Then_ReturnsLNX() throws Exception {
        assertEquals("LNX", utils.getAcronym("Linux"));
    }

    @Test
    public void given_Nintendo64_when_getAcronym_Then_ReturnsN64() throws Exception {
        assertEquals("N64", utils.getAcronym("Nintendo 64"));
    }

    @Test
    public void given_Nintendo64_when_getColor_then_ReturnsBlack() throws Exception {
        assertEquals(Color.parseColor("#C1BED1"), utils.getColor("Nintendo 64"));
    }

    @Test
    public void given_Wii_When_getAcronym_Then_ReturnsWii() throws Exception {
        assertEquals("WII", utils.getAcronym("Wii"));
    }

    @Test
    public void given_Wii_When_GetColor_Then_ReturnsWhite() throws Exception {
        assertEquals(Color.parseColor("#d9d9d9"), utils.getColor("Wii"));
    }

    @Test
    public void given_Windows_When_GetAcronym_Then_ReturnsPC() throws Exception {
        assertEquals("PC", utils.getAcronym("PC (Microsoft Windows)"));
    }

    @Test
    public void given_Windows_When_getColor_Then_ReturnsGrayColor() throws Exception {
        assertEquals(Color.parseColor("#78909C"), utils.getColor("PC (Microsoft Windows)"));
    }

    @Test
    public void given_PlayStation_When_getAcronym_Then_ReturnsPSOne() throws Exception {
        assertEquals("PSONE", utils.getAcronym("PlayStation"));
    }

    @Test
    public void given_PlayStation_When_getAcronym_Then_ReturnsGray() throws Exception {
        assertEquals(Color.parseColor("#E3E3E3"), utils.getColor("PlayStation"));
    }

    @Test
    public void given_PlayStation2_When_GetAcronym_Then_ReturnsPs2() throws Exception {
        assertEquals("PS2", utils.getAcronym("PlayStation 2"));
    }

    @Test
    public void given_PlayStation2_When_GetColor_Then_ReturnsBlue() throws Exception {
        assertEquals(Color.parseColor("#0000CC"), utils.getColor("PlayStation 2"));
    }

    @Test
    public void given_PlayStation3_When_GetAcronym_Then_ReturnsPS3() throws Exception {
        assertEquals("PS3", utils.getAcronym("PlayStation 3"));
    }

    @Test
    public void given_PlayStation3_When_GetColor_Then_ReturnsBlack() throws Exception {
        assertEquals(Color.parseColor("#192730"), utils.getColor("PlayStation 3"));
    }

    @Test
    public void given_Xbox_When_getAcronym_Then_ReturnsXBX() throws Exception {
        assertEquals("XBX", utils.getAcronym("Xbox"));
    }

    @Test
    public void given_Xbox_When_getColor_Then_ReturnsGreen() throws Exception {
        assertEquals(Color.parseColor("#353535"), utils.getColor("Xbox"));
    }

    @Test
    public void given_Xbox360_When_getAcronym_Then_ReturnsX360() throws Exception {
        assertEquals("X360", utils.getAcronym("Xbox 360"));
    }

    @Test
    public void given_Xbox360_When_GetColor_Then_ReturnsLightGreen() throws Exception {
        assertEquals(Color.parseColor("#90C048"), utils.getColor("Xbox 360"));
    }

    @Test
    public void given_PCDOS_when_getAcronym_Then_ReturnsDOS() throws Exception {
        assertEquals("DOS", utils.getAcronym("PC DOS"));
    }

    @Test
    public void given_PCDOS_when_getColor_Then_ReturnsBlack() throws Exception {
        assertEquals(Color.BLACK, utils.getColor("PC DOS"));
    }

    @Test
    public void given_Mac_when_getAcronym_Then_ReturnsOSX() throws Exception {
        assertEquals("OSX", utils.getAcronym("Mac"));
    }

    @Test
    public void given_Mac_When_getColor_then_ReturnsWhite() throws Exception {
        assertEquals(Color.WHITE, utils.getColor("Mac"));
    }

    @Test
    public void given_CommodoreC64128_when_getAcronym_Then_ReturnsC64() throws Exception {
        assertEquals("C64", utils.getAcronym("Commodore C64/128"));
    }

    @Test
    public void given_Commodore64_When_getColor_Then_ReturnsGray() throws Exception {
        assertEquals(Color.parseColor("#6C8995"), utils.getColor("Commodore C64/128"));
    }
}