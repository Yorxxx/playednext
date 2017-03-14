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

    @Test
    public void Given_Amiga_When_getAcronym_Then_ReturnsAmiga() throws Exception {
        assertEquals("AMIGA", utils.getAcronym("Amiga"));
    }

    @Test
    public void Given_Amiga_When_getColor_Then_ReturnsGray() throws Exception {
        assertEquals(Color.parseColor("#d3d3d3"), utils.getColor("Amiga"));
    }

    @Test
    public void Given_NintendoEntertainmentSystem_When_getAcronym_Then_ReturnsNES() throws Exception {
        assertEquals("NES", utils.getAcronym("Nintendo Entertainment System (NES)"));
    }

    @Test
    public void Given_NintendoEntertainmentSystem_When_getColor_Then_ReturnsGray() throws Exception {
        assertEquals(Color.parseColor("#D8D8D8"), utils.getColor("Nintendo Entertainment System (NES)"));
    }

    @Test
    public void Given_SuperNintendoEntertainmentSystem_When_getAcronym_Then_ReturnsSNES() throws Exception {
        assertEquals("SNES", utils.getAcronym("Super Nintendo Entertainment System (SNES)"));
    }

    @Test
    public void Given_SuperNintendoEntertainmentSystem_When_getColor_Then_ReturnsGray() throws Exception {
        assertEquals(Color.parseColor("#DAE0F0"), utils.getColor("Super Nintendo Entertainment System (SNES)"));
    }

    @Test
    public void Given_NintendoDS_When_getAcronym_Then_ReturnsNDS() throws Exception {
        assertEquals("NDS", utils.getAcronym("Nintendo DS"));
    }

    @Test
    public void Given_NintendoDS_When_getColor_Then_ReturnsAppropiateColor() throws Exception {
        assertEquals(Color.parseColor("#3085B0"), utils.getColor("Nintendo DS"));
    }

    @Test
    public void Given_GameCube_When_getAcronym_Then_ReturnsNGC() throws Exception {
        assertEquals("NGC", utils.getAcronym("Nintendo GameCube"));
    }

    @Test
    public void Given_GameCube_When_getColor_Then_ReturnsPurple() throws Exception {
        assertEquals(Color.parseColor("#6509cb"), utils.getColor("Nintendo GameCube"));
    }

    @Test
    public void Given_GameboyColor_When_getAcronym_Then_ReturnsGBC() throws Exception {
        assertEquals("GBC", utils.getAcronym("Game Boy Color"));
    }

    @Test
    public void Given_GameboyColor_When_getColor_Then_ReturnsYellow() throws Exception {
        assertEquals(Color.parseColor("#ffcc00"), utils.getColor("Game Boy Color"));
    }

    @Test
    public void Given_Dreamcast_When_getAcronym_Then_ReturnsDC() throws Exception {
        assertEquals("DC", utils.getAcronym("Dreamcast"));
    }

    @Test
    public void Given_Dreamcast_when_getColor_Then_ReturnsOrange() throws Exception {
        assertEquals(Color.parseColor("#FF6A00"), utils.getColor("Dreamcast"));
    }

    @Test
    public void Given_GameboyAdvance_When_getAcronym_Then_ReturnsGBA() throws Exception {
        assertEquals("GBA", utils.getAcronym("Game Boy Advance"));
    }

    @Test
    public void Given_GameboyAdvance_When_getColor_Then_ReturnsPurple() throws Exception {
        assertEquals(Color.parseColor("#682c9e"), utils.getColor("Game Boy Advance"));
    }

    @Test
    public void Given_AmstradCPC_When_getAcronym_Then_ReturnsACPC() throws Exception {
        assertEquals("ACPC", utils.getAcronym("Amstrad CPC"));
    }

    @Test
    public void Given_AmstradCPC_When_getColor_Then_ReturnsGray() throws Exception {
        assertEquals(Color.parseColor("#dbdbdb"), utils.getColor("Amstrad CPC"));
    }

    @Test
    public void Given_ZXSpectrum_When_getAcronym_Then_ReturnsZXS() throws Exception {
        assertEquals("ZXS", utils.getAcronym("ZX Spectrum"));
    }

    @Test
    public void Given_ZXSpectrum_When_getColor_Then_Returns_Black() throws Exception {
        assertEquals(Color.BLACK, utils.getColor("ZX Spectrum"));
    }
}