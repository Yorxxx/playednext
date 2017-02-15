package com.piticlistudio.playednext.platform.model.repository.datasource;

import com.piticlistudio.playednext.platform.PlatformModule;
import com.piticlistudio.playednext.platform.model.entity.datasource.IPlatformData;

import javax.inject.Inject;

import io.reactivex.Single;

/**
 * Repository for IPlatformData on IGDB
 * Created by jorge.garcia on 14/02/2017.
 */

public class IGDBPlatformRepositoryImpl implements IPlatformRepositoryDatasource<IPlatformData> {

    private final PlatformModule.NetService service;

    @Inject
    public IGDBPlatformRepositoryImpl(PlatformModule.NetService service) {
        this.service = service;
    }

    /**
     * Loads the model with the specified id.
     *
     * @param id the id to load.
     * @return an Observable that emits the model loaded
     */
    @Override
    public Single<IPlatformData> load(int id) {
        return service.load(id, "*")
                .retry(1)
                .map(responses -> {
                    if (responses.size() == 0)
                        throw new RuntimeException("Not found");
                    return (IPlatformData) responses.get(0);
                }).firstOrError();
    }

    /**
     * Saves the data
     *
     * @param data the data to save
     * @return an Observable that emits the saved data
     */
    @Override
    public Single<IPlatformData> save(IPlatformData data) {
        return Single.error(new Exception("Forbidden"));
    }
}
