package com.piticlistudio.playednext.genre.model.repository;

import com.piticlistudio.playednext.genre.model.entity.Genre;
import com.piticlistudio.playednext.genre.model.entity.GenreMapper;
import com.piticlistudio.playednext.genre.model.entity.datasource.IGenreData;
import com.piticlistudio.playednext.genre.model.repository.datasource.IGenreRepositoryDatasource;
import com.piticlistudio.playednext.mvp.model.repository.BaseRepository;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Repository for Genre entities
 * Created by jorge.garcia on 14/02/2017.
 */

public class GenreRepository extends BaseRepository<Genre, IGenreData> implements IGenreRepository {

    @Inject
    public GenreRepository(@Named("db") IGenreRepositoryDatasource<IGenreData> dbImpl,
                           @Named("net") IGenreRepositoryDatasource<IGenreData> netImpl,
                           GenreMapper mapper) {
        super(dbImpl, netImpl, mapper);
    }
}
