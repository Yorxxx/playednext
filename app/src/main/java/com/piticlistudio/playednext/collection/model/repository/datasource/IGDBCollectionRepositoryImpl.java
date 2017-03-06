package com.piticlistudio.playednext.collection.model.repository.datasource;

import com.piticlistudio.playednext.collection.CollectionModule;
import com.piticlistudio.playednext.collection.model.entity.datasource.ICollectionData;

import javax.inject.Inject;

import io.reactivex.Single;

/**
 * Repository implementation for Collection entities provided by the Net module
 * Created by jorge.garcia on 10/02/2017.
 */
public class IGDBCollectionRepositoryImpl implements ICollectionRepositoryDatasource {

    private final CollectionModule.IGDBService service;

    @Inject
    public IGDBCollectionRepositoryImpl(CollectionModule.IGDBService service) {
        this.service = service;
    }

    /**
     * Loads the model with the specified id.
     *
     * @param id the id to load.
     * @return an Observable that emits the model loaded
     */
    @Override
    public Single<ICollectionData> load(int id) {
        return service.load(id, "*")
                .retry(1)
                .map(responses -> {
                    if (responses.size() == 0)
                        throw new RuntimeException("Not found");
                    return (ICollectionData) responses.get(0);
                }).firstOrError();
    }

    /**
     * Saves the data
     *
     * @param data the data to save
     * @return an Observable that emits the saved data
     */
    @Override
    public Single<ICollectionData> save(ICollectionData data) {
        return Single.error(new Exception("Forbidden"));
    }
}
