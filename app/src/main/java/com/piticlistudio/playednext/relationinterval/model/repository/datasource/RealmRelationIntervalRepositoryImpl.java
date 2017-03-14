package com.piticlistudio.playednext.relationinterval.model.repository.datasource;

import com.piticlistudio.playednext.mvp.model.repository.datasource.BaseRealmRepository;
import com.piticlistudio.playednext.relationinterval.model.entity.datasource.RealmRelationInterval;

import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.Single;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class RealmRelationIntervalRepositoryImpl extends BaseRealmRepository<RealmRelationInterval> implements
        IRelationIntervalRepositoryDatasource<RealmRelationInterval> {

    private AtomicInteger primaryKeyValue;

    public RealmRelationIntervalRepositoryImpl() {
        super(RealmRelationInterval.class);
    }

    /**
     * Returns a new id to be used for a new relation
     *
     * @return a new Id
     */
    @Override
    public int getAutoincrementId() {
        if (primaryKeyValue == null) {
            int nextId = 0;
            final RealmConfiguration realmConfig = Realm.getDefaultInstance().getConfiguration();
            Realm realm = Realm.getInstance(realmConfig);
            Number currentMax = realm.where(RealmRelationInterval.class).max("id");
            if (currentMax != null) {
                nextId = currentMax.intValue() + 1;
            }
            primaryKeyValue = new AtomicInteger(nextId);
            realm.close();
        }
        return primaryKeyValue.getAndIncrement();
    }

    /**
     * Loads the model with the specified id.
     *
     * @param id the id to load.
     * @return an Observable that emits the model loaded
     */
    @Override
    public Single<RealmRelationInterval> load(int id) {
        return super.find(id);
    }

    /**
     * Saves the data
     *
     * @param data the data to save
     * @return an Observable that emits the saved data
     */
    @Override
    public Single<RealmRelationInterval> save(RealmRelationInterval data) {
        return super.store(data);
    }
}
