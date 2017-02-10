package com.piticlistudio.playednext.collection.model.repository.datasource;

import com.piticlistudio.playednext.collection.model.entity.datasource.RealmCollection;
import com.piticlistudio.playednext.mvp.model.repository.datasource.BaseRealmRepository;

public class RealmCollectionRepositoryImpl extends BaseRealmRepository<RealmCollection> implements ICollectionRepositoryDatasource<RealmCollection> {

    public RealmCollectionRepositoryImpl() {
        super(RealmCollection.class);
    }
}
