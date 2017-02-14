package com.piticlistudio.playednext.genre.model.repository.datasource;

import com.piticlistudio.playednext.genre.GenreModule;
import com.piticlistudio.playednext.genre.model.entity.datasource.IGenreData;

import javax.inject.Inject;

import io.reactivex.Single;

/**
 * Repository for IGenreData on Net
 * Created by jorge.garcia on 14/02/2017.
 */

public class NetGenreRepositoryImpl implements IGenreRepositoryDatasource<IGenreData> {

    private final GenreModule.NetService service;

    @Inject
    public NetGenreRepositoryImpl(GenreModule.NetService service) {
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
     * @return an Observable that emits the saved data
     */
    @Override
    public Single<IGenreData> save(IGenreData data) {
        return Single.error(new Exception("Forbidden"));
    }
}
