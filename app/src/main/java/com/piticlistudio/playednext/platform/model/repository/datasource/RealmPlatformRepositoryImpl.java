package com.piticlistudio.playednext.platform.model.repository.datasource;

import com.piticlistudio.playednext.mvp.model.repository.datasource.BaseRealmRepository;
import com.piticlistudio.playednext.platform.model.entity.datasource.IPlatformData;
import com.piticlistudio.playednext.platform.model.entity.datasource.RealmPlatform;

import io.reactivex.Single;

/**
 * Implementation of IPlatformRepositoryDatasource on Realm
 * Created by jorge.garcia on 15/02/2017.
 */

public class RealmPlatformRepositoryImpl extends BaseRealmRepository<RealmPlatform> implements IPlatformRepositoryDatasource<IPlatformData> {

    public RealmPlatformRepositoryImpl() {
        super(RealmPlatform.class);
    }

    /**
     * Loads the model with the specified id.
     *
     * @param id the id to load.
     * @return an Observable that emits the model loaded
     */
    @Override
    public Single<IPlatformData> load(int id) {
        return super.find(id)
                .map(data -> data);
    }

    /**
     * Saves the data
     *
     * @param data the data to save
     * @return an Observable that emits the saved data
     */
    @Override
    public Single<IPlatformData> save(IPlatformData data) {
        return super.store((RealmPlatform) data)
                .map(value -> value);
    }
}
