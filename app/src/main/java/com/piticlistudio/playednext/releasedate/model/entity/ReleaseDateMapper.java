package com.piticlistudio.playednext.releasedate.model.entity;

import com.fernandocejas.arrow.optional.Optional;
import com.piticlistudio.playednext.mvp.model.entity.Mapper;
import com.piticlistudio.playednext.releasedate.model.entity.datasource.IReleaseDateData;

import javax.inject.Inject;

/**
 * Maps an IReleaseDate into a ReleaseDate
 * Created by jorge.garcia on 15/02/2017.
 */

public class ReleaseDateMapper implements Mapper<ReleaseDate, IReleaseDateData> {

    @Inject
    public ReleaseDateMapper() {
    }

    /**
     * Transforms the data into an Optional model.
     *
     * @param data the data to transform
     * @return an Optional model.
     */
    @Override
    public Optional<ReleaseDate> transform(IReleaseDateData data) {
        if (data == null || data.getHumanDate() == null)
            return Optional.absent();
        if (data.getDate().isPresent()) {
            return Optional.of(ReleaseDate.create(data.getDate().get(), data.getHumanDate()));
        } else {
            return Optional.of(ReleaseDate.create(0, data.getHumanDate()));
        }
    }
}
