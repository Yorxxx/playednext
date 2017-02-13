package com.piticlistudio.playednext.collection.model.repository.datasource;

import com.piticlistudio.playednext.collection.CollectionModule;
import com.piticlistudio.playednext.collection.model.entity.datasource.ICollectionData;
import com.piticlistudio.playednext.collection.model.entity.datasource.NetCollection;

import javax.inject.Inject;

import io.reactivex.Single;

/**
 * Repository implementation for Collection entities provided by the Net module
 * Created by jorge.garcia on 10/02/2017.
 */
public class NetCollectionRepositoryImpl implements ICollectionRepositoryDatasource<ICollectionData> {

    private final CollectionModule.NetService service;

    @Inject
    public NetCollectionRepositoryImpl(CollectionModule.NetService service) {
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
                    return (ICollectionData)responses.get(0);
                }).firstOrError();
    }
}
