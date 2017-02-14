package com.piticlistudio.playednext.genre.model.entity.datasource;

import com.google.auto.value.AutoValue;
import com.piticlistudio.playednext.IGenreData;
import com.piticlistudio.playednext.utils.AutoGson;

/**
 * Implementation of a IGenreData by backend
 * Created by jorge.garcia on 14/02/2017.
 */
@AutoGson
@AutoValue
public abstract class NetGenre implements IGenreData {

    public static NetGenre create(int id, String name, String slug, String url, long createdAt, long updatedAt) {
        return new AutoValue_NetGenre(id, name, slug, url, createdAt, updatedAt);
    }

    public abstract int id();

    public abstract String name();

    public abstract String slug();

    public abstract String url();

    public abstract long created_at();

    public abstract long updated_at();

    /**
     * Returns the id
     *
     * @return the id
     */
    @Override
    public int getId() {
        return id();
    }

    /**
     * Returns the name
     *
     * @return the name
     */
    @Override
    public String getName() {
        return name();
    }
}
