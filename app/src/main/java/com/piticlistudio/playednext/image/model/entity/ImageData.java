package com.piticlistudio.playednext.image.model.entity;

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
}
