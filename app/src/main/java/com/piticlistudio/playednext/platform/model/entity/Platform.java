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
        return name();
    }

    public int getColor() {
        if (name().equals("Xbox One"))
            return Color.parseColor("#3b7e14");
        if (name().equals("PC (Microsoft Windows)"))
            return Color.parseColor("#78909C");
        if (name().equals("PlayStation 4"))
            return Color.parseColor("#025bd9");
        return Color.WHITE;
    }
}
