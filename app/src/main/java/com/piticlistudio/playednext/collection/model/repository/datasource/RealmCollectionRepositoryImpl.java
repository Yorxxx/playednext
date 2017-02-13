package com.piticlistudio.playednext.collection.model.repository.datasource;

import com.piticlistudio.playednext.collection.model.entity.datasource.ICollectionData;
import com.piticlistudio.playednext.collection.model.entity.datasource.RealmCollection;
import com.piticlistudio.playednext.mvp.model.repository.datasource.BaseRealmRepository;

import io.reactivex.Single;

public class RealmCollectionRepositoryImpl extends BaseRealmRepository<RealmCollection> implements ICollectionRepositoryDatasource<ICollectionData> {

    public RealmCollectionRepositoryImpl() {
        super(RealmCollection.class);
    }

    /**
     * Loads the model with the specified id.
     *
     * @param id the id to load.
     * @return an Observable that emits the model loaded
     */
    @Override
    public Single<ICollectionData> load(int id) {
        return super.find(id)
                .map(data -> data);
    }
}
