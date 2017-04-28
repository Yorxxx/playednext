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

    @Test
    public void Given_MSX_When_getAcronym_Then_ReturnsMSX() throws Exception {
        assertEquals("MSX", utils.getAcronym("MSX"));
    }

    @Test
    public void Given_MSX_When_getColor_Then_ReturnsBlack() throws Exception {
        assertEquals(Color.BLACK, utils.getColor("MSX"));
    }

    @Test
    public void Given_SegaMegadrive_When_getAcronym_Then_ReturnsMD() throws Exception {
        assertEquals("MD", utils.getAcronym("Sega Mega Drive/Genesis"));
    }

    @Test
    public void Given_SegaMegadrive_When_getColor_Then_ReturnsAppropiateColor() throws Exception {
        assertEquals(Color.parseColor("#2B2728"), utils.getColor("Sega Mega Drive/Genesis"));
    }

    @Test
    public void Given_Sega32X_When_getAcronym_Then_Returns32X() throws Exception {
        assertEquals("32X", utils.getAcronym("Sega 32X"));
    }

    @Test
    public void Given_Sega32X_When_getColor_Then_ReturnsAppropiateColor() throws Exception {
        assertEquals(Color.parseColor("#2B2728"), utils.getColor("Sega 32X"));
    }

    @Test
    public void Given_SegaSaturn_When_getAcronym_Then_ReturnsSAT() throws Exception {
        assertEquals("SAT", utils.getAcronym("Sega Saturn"));
    }

    @Test
    public void Given_SegaSaturn_When_getColor_Then_ReturnsAppropiateColor() throws Exception {
        assertEquals(Color.parseColor("#797979"), utils.getColor("Sega Saturn"));
    }

    @Test
    public void Given_GameBoy_When_getAcronym_Then_ReturnsGB() throws Exception {
        assertEquals("GB", utils.getAcronym("Game Boy"));
    }

    @Test
    public void Given_GameBoy_When_getColor_Then_ReturnsLightGray() throws Exception {
        assertEquals(Color.parseColor("#76818C"), utils.getColor("Game Boy"));
    }

    @Test
    public void Given_Android_When_getAcronym_Then_ReturnsAnd() throws Exception {
        assertEquals("AND", utils.getAcronym("Android"));
    }

    @Test
    public void Given_Android_When_getColor_Then_ReturnsGreen() throws Exception {
        assertEquals(Color.parseColor("#4CAF50"), utils.getColor("Android"));
    }

    @Test
    public void Given_Gamegear_When_getAcronym_Then_ReturnsGG() throws Exception {
        assertEquals("GG", utils.getAcronym("Sega Game Gear"));
    }

    @Test
    public void Given_Gamegear_When_getColor_Then_ReturnsBlack() throws Exception {
        assertEquals(Color.parseColor("#2B2728"), utils.getColor("Sega Game Gear"));
    }

    @Test
    public void Given_XboxLiveArcade_When_getAcronym_Then_ReturnsXBLA() throws Exception {
        assertEquals("XBLA", utils.getAcronym("Xbox Live Arcade"));
    }

    @Test
    public void Given_XboxLiveArcade_When_getColor_Then_ReturnsXboxColor() throws Exception {
        assertEquals(Color.parseColor("#90C048"), utils.getColor("Xbox Live Arcade"));
    }

    @Test
    public void Given_Nintendo3DS_When_getAcronym_Then_Returns3DS() throws Exception {
        assertEquals("3DS", utils.getAcronym("Nintendo 3DS"));
    }

    @Test
    public void Given_Nintendo3DS_When_getColor_Then_Returns3DSColor() throws Exception {
        assertEquals(Color.parseColor("#d5000c"), utils.getColor("Nintendo 3DS"));
    }

    @Test
    public void Given_PlayStationPortable_When_getAcronym_Then_ReturnsPSP() throws Exception {
        assertEquals("PSP", utils.getAcronym("PlayStation Portable"));
    }

    @Test
    public void Given_PlayStationPortable_When_getColor_Then_ReturnsPSPColor() throws Exception {
        assertEquals(Color.parseColor("#373944"), utils.getColor("PlayStation Portable"));
    }

    @Test
    public void Given_iOS_When_getAcronym_Then_ReturnsIos() throws Exception {
        assertEquals("IOS", utils.getAcronym("iOS"));
    }

    @Test
    public void Given_iOS_When_getColor_Then_ReturnsWhite() throws Exception {
        assertEquals(Color.WHITE, utils.getColor("iOS"));
    }

    @Test
    public void Given_WiiU_When_getAcronym_Then_ReturnsWiiU() throws Exception {
        assertEquals("WIIU", utils.getAcronym("Wii U"));
    }

    @Test
    public void Given_WiiU_When_getColor_Then_ReturnsWiiUColor() throws Exception {
        assertEquals(Color.parseColor("#1C9DFF"), utils.getColor("Wii U"));
    }

    @Test
    public void Given_NGage_When_GetAcronym_Then_ReturnsNGAGE() throws Exception {
        assertEquals("NGAGE", utils.getAcronym("N-Gage"));
    }

    @Test
    public void Given_NGage_When_GetColor_Then_ReturnsNGageColor() throws Exception {
        assertEquals(Color.parseColor("#a8a8a8"), utils.getColor("N-Gage"));
    }

    @Test
    public void Given_TapwaveZodiac_When_GetAcronym_Then_ReturnsZod() throws Exception {
        assertEquals("ZOD", utils.getAcronym("Tapwave Zodiac"));
    }

    @Test
    public void Given_TapwaveZodiac_When_GetColor_Then_ReturnsColor() throws Exception {
        assertEquals(Color.parseColor("#939393"), utils.getColor("Tapwave Zodiac"));
    }

    @Test
    public void Given_PlayStationNetwork_When_GetAcronym_Then_ReturnsPSN() throws Exception {
        assertEquals("PSN", utils.getAcronym("PlayStation Network"));
    }

    @Test
    public void Given_PlayStationNetwork_When_GetColor_Then_ReturnsPSNColor() throws Exception {
        assertEquals(Color.parseColor("#192730"), utils.getColor("PlayStation Network"));
    }

    @Test
    public void Given_PlayStationVita_When_GetAcronym_Then_ReturnsVita() throws Exception {
        assertEquals("VITA", utils.getAcronym("PlayStation Vita"));
    }

    @Test
    public void Given_PlayStationVita_When_getColor_Then_ReturnsVitaColor() throws Exception {
        assertEquals(Color.parseColor("#003287"), utils.getColor("PlayStation Vita"));
    }

    @Test
    public void Given_VirtualConsole_When_GetAcronym_Then_ReturnsVC() throws Exception {
        assertEquals("VC", utils.getAcronym("Virtual Console (Nintendo)"));
    }

    @Test
    public void Given_VirtualConsole_When_GetColor_Then_ReturnsVCColor() throws Exception {
        assertEquals(Color.RED, utils.getColor("Virtual Console (Nintendo)"));
    }

    @Test
    public void Given_PlayStation4_When_getAcronym_Then_ReturnsPS4() throws Exception {
        assertEquals("PS4", utils.getAcronym("PlayStation 4"));
    }

    @Test
    public void Given_PlayStation4_When_GetColor_Then_ReturnsPS4Color() throws Exception {
        assertEquals(Color.parseColor("#025bd9"), utils.getColor("PlayStation 4"));
    }

    @Test
    public void Given_XboxOne_When_getAcronym_Then_ReturnsXboxOne() throws Exception {
        assertEquals("ONE", utils.getAcronym("Xbox One"));
    }

    @Test
    public void Given_XboxOne_When_getColor_Then_ReturnsOneColor() throws Exception {
        assertEquals(Color.parseColor("#3b7e14"), utils.getColor("Xbox One"));
    }

    @Test
    public void Given_3DOInteractiveMultiplayer_When_getAcronym_Then_Returns3D0() throws Exception {
        assertEquals("3D0", utils.getAcronym("3DO Interactive Multiplayer"));
    }

    @Test
    public void Given_3DOInteractiveMultiplayer_When_GetColor_Then_Returns3D0Color() throws Exception {
        assertEquals(Color.parseColor("#424242"), utils.getColor("3DO Interactive Multiplayer"));
    }

    @Test
    public void given_Arcade_When_getAcronym_Then_ReturnsArcade() throws Exception {
        assertEquals("ARCADE", utils.getAcronym("Arcade"));
    }

    @Test
    public void given_Arcade_When_getColor_Then_ReturnsArcadeColor() throws Exception {
        assertEquals(Color.parseColor("#343434"), utils.getColor("Arcade"));
    }

    @Test
    public void given_WonderSwan_When_getAcronym_Then_ReturnsWS() throws Exception {
        assertEquals("WS", utils.getAcronym("WonderSwan"));
    }

    @Test
    public void given_WonderSwan_When_getColor_Then_ReturnsWonderSwanColor() throws Exception {
        assertEquals(Color.parseColor("#8D8D8D"), utils.getColor("WonderSwan"));
    }

    @Test
    public void given_SuperFamicom_When_getAcronym_Then_ReturnsSFC() throws Exception {
        assertEquals("SFC", utils.getAcronym("Super Famicom"));
    }

    @Test
    public void given_SuperFamicom_When_getColor_Then_ReturnsSFColor() throws Exception {
        assertEquals(Color.parseColor("#A99EE8"), utils.getColor("Super Famicom"));
    }

    @Test
    public void given_Atari2600_When_getAcronym_Then_ReturnsA2600() throws Exception {
        assertEquals("A2600", utils.getAcronym("Atari 2600"));
    }

    @Test
    public void given_Atari2600_When_GetColor_Then_ReturnsAtariColor() throws Exception {
        assertEquals(Color.parseColor("#212121"), utils.getColor("Atari 2600"));
    }

    @Test
    public void given_Atari7800_When_getAcronym_Then_ReturnsA7800() throws Exception {
        assertEquals("A7800", utils.getAcronym("Atari 7800"));
    }

    @Test
    public void given_Atari7800_When_getColor_Then_ReturnsAtariColor() throws Exception {
        assertEquals(Color.parseColor("#131313"), utils.getColor("Atari 7800"));
    }

    @Test
    public void given_AtariLynx_When_getAcronym_Then_ReturnsLynx() throws Exception {
        assertEquals("LYNX", utils.getAcronym("Atari Lynx"));
    }

    @Test
    public void given_AtariLynx_When_getAcronym_Then_ReturnsLynxColor() throws Exception {
        assertEquals(Color.parseColor("#7A7A7A"), utils.getColor("Atari Lynx"));
    }

    @Test
    public void given_AtariJaguar_When_getAcronym_Then_ReturnsJaguar() throws Exception {
        assertEquals("JAGUAR", utils.getAcronym("Atari Jaguar"));
    }

    @Test
    public void given_AtariJaguar_When_getColor_Then_ReturnsBlack() throws Exception {
        assertEquals(Color.BLACK, utils.getColor("Atari Jaguar"));
    }

    @Test
    public void given_SegaMasterSystem_When_getAcronym_Then_ReturnsMS() throws Exception {
        assertEquals("MS", utils.getAcronym("Sega Master System"));
    }

    @Test
    public void given_SegaMasterSystem_When_getColor_Then_ReturnsGray() throws Exception {
        assertEquals(Color.GRAY, utils.getColor("Sega Master System"));
    }
}