package com.piticlistudio.playednext.genre.model.repository.datasource;

import com.piticlistudio.playednext.genre.GenreModule;
import com.piticlistudio.playednext.genre.model.entity.datasource.IGenreData;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * Repository for IGenreData on Net
 * Created by jorge.garcia on 14/02/2017.
 */

public class IGDBGenreRepositoryImpl implements IGenreRepositoryDatasource {

    private final GenreModule.NetService service;

    @Inject
    public IGDBGenreRepositoryImpl(GenreModule.NetService service) {
        this.service = service;
    }

    /**
     * Loads the model with the specified id.
     *
     * @param id the id to load.
     * @return an Observable that emits the model loaded
     */
    @Override
    public Single<IGenreData> load(int id) {
        return service.load(id, "*")
                .retry(1)
                .map(responses -> {
                    if (responses.size() == 0)
                        throw new RuntimeException("Not found");
                    return (IGenreData) responses.get(0);
                }).firstOrError();
    }

    /**
     * Saves the data
     *
     * @param data the data to save
     * @return an Observable that emits completion or failure
     */
    @Override
    public Completable save(IGenreData data) {
        return Completable.error(new Exception("Forbidden"));
    }
}
