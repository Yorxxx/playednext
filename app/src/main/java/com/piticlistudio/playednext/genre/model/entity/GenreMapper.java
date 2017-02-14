package com.piticlistudio.playednext.genre.model.entity;

import com.fernandocejas.arrow.optional.Optional;
import com.piticlistudio.playednext.genre.model.entity.datasource.IGenreData;
import com.piticlistudio.playednext.mvp.model.entity.Mapper;

import javax.inject.Inject;

/**
 * Maps a IGenreData entity into a Genre model
 * Created by jorge.garcia on 14/02/2017.
 */

public class GenreMapper implements Mapper<Genre, IGenreData> {

    @Inject
    public GenreMapper() {
    }

    /**
     * Transforms the data into an Optional model.
     *
     * @param data the data to transform
     * @return an Optional model.
     */
    @Override
    public Optional<Genre> transform(IGenreData data) {
        if (data == null || data.getName() == null)
            return Optional.absent();
        return Optional.of(Genre.create(data.getId(), data.getName()));
    }
}
