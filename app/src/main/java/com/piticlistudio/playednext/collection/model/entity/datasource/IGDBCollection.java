package com.piticlistudio.playednext.collection.model.entity.datasource;

import com.google.auto.value.AutoValue;
import com.piticlistudio.playednext.utils.AutoGson;

import java.util.List;

/**
 * Collection data representation on IGDB
 * Created by jorge.garcia on 10/02/2017.
 */
@AutoValue
@AutoGson
public abstract class IGDBCollection implements ICollectionData {

    public abstract int id();

    public abstract String name();

    public abstract String url();

    public abstract long created_at();

    public abstract long updated_at();

    public abstract List<Integer> games();

    public static IGDBCollection create(int id, String name, String url, long createdAt, long updatedAt, List<Integer> gameIds) {
        return new AutoValue_IGDBCollection(id, name, url, createdAt, updatedAt, gameIds);
    }

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
