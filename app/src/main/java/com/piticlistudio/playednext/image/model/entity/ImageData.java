package com.piticlistudio.playednext.image.model.entity;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;

/**
 * Entity representing an image info
 * Created by jorge.garcia on 13/02/2017.
 */
@AutoValue
public abstract class ImageData {

    public static ImageData create(String id, int width, int height, String url) {
        return new AutoValue_ImageData(id, width, height, url);
    }

    public abstract String id();

    public abstract int fullWidth();

    public abstract int fullHeight();

    public abstract String thumbUrl();

    /**
     * Returns the full url.
     *
     * @return the full or null.
     */
    @Nullable
    public String getFullUrl() {
        if (thumbUrl() == null) {
            return null;
        }
        return getThumbUrl().replace("/t_thumb", "");
    }

    /**
     * Returns the full url.
     *
     * @return the full or null.
     */
    public String getThumbUrl() {
        if (thumbUrl() == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        if (!thumbUrl().startsWith("http:"))
            sb.append("http:");
        if (!thumbUrl().startsWith("//") && !thumbUrl().startsWith("http://"))
            sb.append("//");
        sb.append(thumbUrl());
        return sb.toString();
    }
}
