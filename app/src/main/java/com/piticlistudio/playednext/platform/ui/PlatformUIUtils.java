package com.piticlistudio.playednext.platform.ui;

import android.graphics.Color;

import com.piticlistudio.playednext.platform.model.entity.Platform;

/**
 * Created by jorge.garcia on 21/02/2017.
 */

public class PlatformUIUtils {

    /**
     * Returns the color associated to the platform.
     *
     * @param platform the platform to retrieve its color.
     * @return the color associated.
     */
    public static int getColor(Platform platform) {

        String name = platform.name();
        if (name == null)
            return Color.parseColor("#47fe79");
        if (name.equalsIgnoreCase("3DO"))
            return Color.parseColor("#424242");
        else if (name.equalsIgnoreCase("Amiga"))
            return Color.parseColor("#ab4a3d");
        else if (name.equalsIgnoreCase("Android"))
            return Color.parseColor("#4CAF50");
        else if (name.equalsIgnoreCase("Arcade"))
            return Color.parseColor("#7A7865");
        else if (name.equalsIgnoreCase("Atari 2600"))
            return Color.parseColor("#FFA000");
        else if (name.equalsIgnoreCase("Atari Jaguar"))
            return Color.parseColor("#212121");
        else if (name.equalsIgnoreCase("Atari Lynx"))
            return Color.parseColor("#757575");
        else if (name.equalsIgnoreCase("Mac OS"))
            return Color.parseColor("#CCE0E0");
        else if (name.equalsIgnoreCase("Microsoft Xbox"))
            return Color.parseColor("#353535");
        else if (name.equalsIgnoreCase("Microsoft Xbox 360"))
            return Color.parseColor("#90C048");
        else if (name.equalsIgnoreCase("Microsoft Xbox One"))
            return Color.parseColor("#3b7e14");
        else if (name.equalsIgnoreCase("Neo Geo Pocket"))
            return Color.parseColor("#78909C");
        else if (name.equalsIgnoreCase("Neo Geo Pocket Color"))
            return Color.parseColor("#0091EA");
        else if (name.equalsIgnoreCase("NeoGeo"))
            return Color.parseColor("#ffc011");
        else if (name.equalsIgnoreCase("Nintendo 3DS"))
            return Color.parseColor("#d5000c");
        else if (name.equalsIgnoreCase("Nintendo 64"))
            return Color.parseColor("#C1BED1");
        else if (name.equalsIgnoreCase("Nintendo DS"))
            return Color.parseColor("#3085B0");
        else if (name.equalsIgnoreCase("Nintendo Entertainment System (NES)"))
            return Color.parseColor("#D8D8D8");
        else if (name.equalsIgnoreCase("Nintendo Game Boy"))
            return Color.parseColor("#76818C");
        else if (name.equalsIgnoreCase("Nintendo Game Boy Advance"))
            return Color.parseColor("#682c9e");
        else if (name.equalsIgnoreCase("Nintendo Game Boy Color"))
            return Color.parseColor("#ffcc00");
        else if (name.equalsIgnoreCase("Nintendo Gamecube"))
            return Color.parseColor("#6509cb");
        else if (name.equalsIgnoreCase("Nintendo Virtual Boy"))
            return Color.parseColor("#D32F2F");
        else if (name.equalsIgnoreCase("Nintendo Wii"))
            return Color.parseColor("#d9d9d9");
        else if (name.equalsIgnoreCase("Nintendo Wii U"))
            return Color.parseColor("#1C9DFF");
        else if (name.equalsIgnoreCase("PC"))
            return Color.parseColor("#78909C");
        else if (name.equalsIgnoreCase("Sega 32X"))
            return Color.parseColor("#2B2728");
        else if (name.equalsIgnoreCase("Sega CD"))
            return Color.parseColor("#2B2728");
        else if (name.equalsIgnoreCase("Sega Dreamcast"))
            return Color.parseColor("#FF6A00");
        else if (name.equalsIgnoreCase("Sega Game Gear"))
            return Color.parseColor("#2B2728");
        else if (name.equalsIgnoreCase("Sega Genesis"))
            return Color.parseColor("#2B2728");
        else if (name.equalsIgnoreCase("Sega Mega drive"))
            return Color.parseColor("#2B2728");
        else if (name.equalsIgnoreCase("Sega Master System"))
            return Color.parseColor("#616161");
        else if (name.equalsIgnoreCase("Sega Saturn"))
            return Color.parseColor("#797979");
        else if (name.equalsIgnoreCase("Sony Playstation"))
            return Color.parseColor("#E3E3E3");
        else if (name.equalsIgnoreCase("Sony Playstation 2"))
            return Color.parseColor("#0000CC");
        if (name.equalsIgnoreCase("Sony Playstation 3"))
            return Color.parseColor("#192730");
        else if (name.equalsIgnoreCase("Sony Playstation 4"))
            return Color.parseColor("#025bd9");
        else if (name.equalsIgnoreCase("Sony Playstation Vita"))
            return Color.parseColor("#003287");
        else if (name.equalsIgnoreCase("Sony PSP"))
            return Color.parseColor("#373944");
        else if (name.equalsIgnoreCase("Super Nintendo (SNES)"))
            return Color.parseColor("#DAE0F0");
        return Color.parseColor("#47fe79");
    }
}
