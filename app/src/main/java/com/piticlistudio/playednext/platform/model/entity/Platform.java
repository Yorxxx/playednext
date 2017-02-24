package com.piticlistudio.playednext.platform.model.entity;

import android.graphics.Color;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Platform {

    public static Platform create(int id, String name) {
        return new AutoValue_Platform(id, name);
    }

    public abstract int id();

    public abstract String name();

    public String getAcronym() {
        if (name().split("\\s+").length == 1)
            return name();

        // Some predefined values
        if (name().equals("Xbox One"))
            return "One";
        if (name().equals("PC (Microsoft Windows)"))
            return "PC Win";
        if (name().equals("PlayStation 4"))
            return "Ps4";
        if (name().equals("PlayStation Network"))
            return "PSN";
        if (name().equals("Xbox Live Arcade"))
            return "XBLA";
        if (name().equals("Super Nintendo Entertainment System (SNES)"))
            return "SNES";
        if (name().equals("Nintendo GameCube"))
            return "NGC";
        if (name().equals("Game Boy Advance"))
            return "GBA";
        if (name().equals("Virtual Console (Nintendo)"))
            return "VC";
        if (name().equals("Nintendo Switch"))
            return "Switch";

        if (name().length() <= 8)
            return name();
        String[] values = name().split("\\s+");
        StringBuilder sb = new StringBuilder();
        if (values.length == 2) {
            sb.append(values[0].charAt(0));
            if (!values[0].equals("Nintendo"))
                sb.append(" ");
            sb.append(values[1]);
            return sb.toString();
        }
        return name();
    }

    public int getColor() {
        if (name().equals("Xbox One") || name().equals("Xbox Live Arcade"))
            return Color.parseColor("#3b7e14");
        if (name().equals("PC (Microsoft Windows)"))
            return Color.parseColor("#78909C");
        if (name().equals("PlayStation 4") || name().equals("PlayStation Network"))
            return Color.parseColor("#025bd9");
        if (name().equals("Mac"))
            return Color.parseColor("#CCE0E0");
        if (name().equals("Super Nintendo Entertainment System (SNES)") || name().equals("Super Famicom"))
            return Color.parseColor("#DAE0F0");
        if (name().equals("Nintendo GameCube"))
            return Color.parseColor("#6509cb");
        if (name().equals("Game Boy Advance"))
            return Color.parseColor("#682c9e");
        if (name().equals("Nintendo 64"))
            return Color.parseColor("#C1BED1");
        if (name().equals("Nintendo 3DS"))
            return Color.parseColor("#d5000c");
        if (name().equals("Nintendo DS"))
            return Color.parseColor("#3085B0");
        return Color.WHITE;
    }
}
