package com.piticlistudio.playednext.genre.model.entity;

import com.google.auto.value.AutoValue;

/**
 * Genre entity
 * Created by jorge.garcia on 14/02/2017.
 */
@AutoValue
public abstract class Genre {

    public static Genre create(int id, String name) {
        return new AutoValue_Genre(id, name);
    }

    public abstract int id();

    public abstract String name();
}
