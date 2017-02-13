package com.piticlistudio.playednext.image.model.entity.datasource;

import com.google.auto.value.AutoValue;
import com.piticlistudio.playednext.utils.AutoGson;

/**
 * Representation of IImageData by net module
 *
 * @see IImageData
 * Created by jorge.garcia on 13/02/2017.
 */
@AutoValue
@AutoGson
public abstract class NetImageData implements IImageData {

    public static NetImageData create(String url, int width, int height, String id) {
        return new AutoValue_NetImageData(url, width, height, id);
    }

    public abstract String url();

    public abstract int width();

    public abstract int height();

    public abstract String cloudinary_id();

    /**
     * Returns the id
     *
     * @return the id
     */
    @Override
    public String getId() {
        return cloudinary_id();
    }

    /**
     * Returns the width
     *
     * @return the width
     */
    @Override
    public int getWidth() {
        return width();
    }

    /**
     * Returns the height
     *
     * @return the height
     */
    @Override
    public int getHeight() {
        return height();
    }

    /**
     * Returns the url
     *
     * @return the url
     */
    @Override
    public String getUrl() {
        return url();
    }
}
