package com.piticlistudio.playednext.genre.model.entity;

import com.fernandocejas.arrow.optional.Optional;
import com.piticlistudio.playednext.genre.model.entity.datasource.RealmGenre;
import com.piticlistudio.playednext.mvp.model.entity.Mapper;

import javax.inject.Inject;

/**
 * Maps a Genre into a RealmGenre
 * Created by jorge.garcia on 14/02/2017.
 */

public class RealmGenreMapper implements Mapper<RealmGenre, Genre> {

    @Inject
    public RealmGenreMapper() {
    }

    /**
     * Transforms the data into an Optional model.
     *
     * @param data the data to transform
     * @return an Optional model.
     */
    @Override
    public Optional<RealmGenre> transform(Genre data) {
        if (data == null)
            return Optional.absent();
        return Optional.of(new RealmGenre(data.id(), data.name()));
    }
}
