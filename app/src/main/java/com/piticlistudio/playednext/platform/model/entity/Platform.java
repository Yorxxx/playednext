package com.piticlistudio.playednext.platform.model.entity;

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
}
