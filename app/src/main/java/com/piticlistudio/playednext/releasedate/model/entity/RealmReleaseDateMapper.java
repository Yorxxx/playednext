package com.piticlistudio.playednext.releasedate.model.entity;

import com.fernandocejas.arrow.optional.Optional;
import com.piticlistudio.playednext.mvp.model.entity.Mapper;
import com.piticlistudio.playednext.releasedate.model.entity.datasource.RealmReleaseDate;

import javax.inject.Inject;

/**
 * Maps a ReleaseDate into a RealmReleaseDate
 * Created by jorge.garcia on 15/02/2017.
 */

public class RealmReleaseDateMapper implements Mapper<RealmReleaseDate, ReleaseDate> {

    @Inject
    public RealmReleaseDateMapper() {
    }

    /**
     * Transforms the data into an Optional model.
     *
     * @param data the data to transform
     * @return an Optional model.
     */
    @Override
    public Optional<RealmReleaseDate> transform(ReleaseDate data) {
        if (data == null)
            return Optional.absent();
        return Optional.of(new RealmReleaseDate(data.humanDate(), data.date()));
    }
}
