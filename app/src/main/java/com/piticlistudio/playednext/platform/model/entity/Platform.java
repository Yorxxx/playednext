package com.piticlistudio.playednext.platform.model.entity;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Platform {

    public static Platform create(int id, String name) {
        return new AutoValue_Platform(id, name);
    }

    public abstract int id();

    public abstract String name();
}
