package com.piticlistudio.playednext.platform.model.entity.datasource;

import com.google.auto.value.AutoValue;
import com.piticlistudio.playednext.utils.AutoGson;

@AutoGson
@AutoValue
public abstract class NetPlatform implements IPlatformData {

    public static NetPlatform create(int id, String name, String slug, String url, long createdAt, long updatedAt) {
        return new AutoValue_NetPlatform(id, name, slug, url, createdAt, updatedAt);
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
