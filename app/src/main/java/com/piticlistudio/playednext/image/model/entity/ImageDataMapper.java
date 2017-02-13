package com.piticlistudio.playednext.image.model.entity;

import com.fernandocejas.arrow.optional.Optional;
import com.piticlistudio.playednext.image.model.entity.datasource.IImageData;
import com.piticlistudio.playednext.mvp.model.entity.Mapper;

import javax.inject.Inject;

/**
 * Maps an IImageData into an ImageData
 *
 * @see IImageData
 * Created by jorge.garcia on 13/02/2017.
 */

public class ImageDataMapper implements Mapper<ImageData, IImageData> {

    @Inject
    public ImageDataMapper() {
    }

    /**
     * Transforms the data into an Optional model.
     *
     * @param data the data to transform
     * @return an Optional model.
     */
    @Override
    public Optional<ImageData> transform(IImageData data) {
        if (data == null || data.getId() == null || data.getUrl() == null)
            return Optional.absent();
        return Optional.of(ImageData.create(data.getId(), data.getWidth(), data.getHeight(), data.getUrl()));
    }
}
