package com.piticlistudio.playednext.image.model.entity;

import com.fernandocejas.arrow.optional.Optional;
import com.piticlistudio.playednext.image.model.entity.datasource.RealmImageData;
import com.piticlistudio.playednext.mvp.model.entity.Mapper;

import javax.inject.Inject;

/**
 * Mapper for images
 * Created by jorge.garcia on 14/02/2017.
 */
public class RealmImageDataMapper implements Mapper<RealmImageData, ImageData> {

    @Inject
    public RealmImageDataMapper() {
    }

    /**
     * Transforms the data into an Optional model.
     *
     * @param data the data to transform
     * @return an Optional model.
     */
    @Override
    public Optional<RealmImageData> transform(ImageData data) {
        if (data == null)
            return Optional.absent();
        return Optional.of(new RealmImageData(data.id(), data.thumbUrl(), data.fullWidth(), data.fullHeight()));
    }
}
