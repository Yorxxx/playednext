package com.piticlistudio.playednext.collection.model.repository;

import com.piticlistudio.playednext.collection.model.entity.Collection;
import com.piticlistudio.playednext.collection.model.entity.CollectionMapper;
import com.piticlistudio.playednext.collection.model.entity.datasource.ICollectionData;
import com.piticlistudio.playednext.collection.model.repository.datasource.ICollectionRepositoryDatasource;
import com.piticlistudio.playednext.mvp.model.repository.BaseRepository;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Collection repository
 * Retrieves data from its associated datasources, and returns them as domain entities.
 * Created by jorge.garcia on 10/02/2017.
 */

public class CollectionRepository extends BaseRepository<Collection, ICollectionData> implements ICollectionRepository {

    @Inject
    public CollectionRepository(@Named("db") ICollectionRepositoryDatasource dbImpl,
                                @Named("net") ICollectionRepositoryDatasource netImpl,
                                CollectionMapper mapper) {
        super(dbImpl, netImpl, mapper);
    }
}
