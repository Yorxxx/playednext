package com.piticlistudio.playednext.collection.model.repository;

import com.fernandocejas.arrow.optional.Optional;
import com.piticlistudio.playednext.collection.model.entity.Collection;
import com.piticlistudio.playednext.collection.model.entity.CollectionMapper;
import com.piticlistudio.playednext.collection.model.entity.datasource.NetCollection;
import com.piticlistudio.playednext.collection.model.entity.datasource.RealmCollection;
import com.piticlistudio.playednext.collection.model.repository.datasource.ICollectionRepositoryDatasource;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;

/**
 * Collection repository
 * Retrieves data from its associated datasources, and returns them as domain entities.
 * Created by jorge.garcia on 10/02/2017.
 */

public class CollectionRepository implements ICollectionRepository {

    private final ICollectionRepositoryDatasource<RealmCollection> dbImpl;
    private final ICollectionRepositoryDatasource<NetCollection> netImpl;
    private final CollectionMapper mapper;

    @Inject
    public CollectionRepository(@Named("db") ICollectionRepositoryDatasource<RealmCollection> dbImpl,
                                @Named("net") ICollectionRepositoryDatasource<NetCollection> netImpl,
                                CollectionMapper mapper) {
        this.dbImpl = dbImpl;
        this.netImpl = netImpl;
        this.mapper = mapper;
    }

    /**
     * Loads the collection with the specified id.
     *
     * @param id the id to load.
     * @return an Observable that emits the collection loaded
     */
    @Override
    public Observable<Collection> load(int id) {
        return dbImpl.load(id)
                .map(data -> {
                    Optional<Collection> result = mapper.transform(data);
                    if (!result.isPresent())
                        throw new RuntimeException("Cannot map");
                    return result.get();
                })
                .onErrorResumeNext(__ -> {
                    return netImpl.load(id)
                            .map(data -> {
                                Optional<Collection> result = mapper.transform(data);
                                if (!result.isPresent())
                                    throw new RuntimeException("Cannot map");
                                return result.get();
                            });
                })
                .toObservable();
    }
}
