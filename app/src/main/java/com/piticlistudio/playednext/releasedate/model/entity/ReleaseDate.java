package com.piticlistudio.playednext.releasedate.model.entity;

import com.google.auto.value.AutoValue;

/**
 * Representation of a Release
 * Created by jorge.garcia on 15/02/2017.
 */
@AutoValue
public abstract class ReleaseDate {

    public static ReleaseDate create(long date, String human) {
        return new AutoValue_ReleaseDate(date, human);
    }

    public abstract long date();

    public abstract String humanDate();
}
