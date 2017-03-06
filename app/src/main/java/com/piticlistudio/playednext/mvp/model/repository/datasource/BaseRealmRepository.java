package com.piticlistudio.playednext.mvp.model.repository.datasource;


import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;

/**
 * A base realmRepository
 * Created by jorge.garcia on 16/01/2017.
 */
public abstract class BaseRealmRepository<R extends RealmObject> {

    private Class<R> realmTypeClass;

    public BaseRealmRepository(Class<R> realmTypeClass) {
        this.realmTypeClass = realmTypeClass;
    }

    /**
     * Returns a Realm instance.
     *
     * @return an Observable with a Realm instance.
     */
    protected static Observable<Realm> getManagedRealm() {
        final RealmConfiguration realmConfig = Realm.getDefaultInstance().getConfiguration();
        return Observable.using(() -> Realm.getInstance(realmConfig), Observable::just, realm -> {
            if (!realm.isClosed())
                realm.close();
        });

    }

    /**
     * Loads the model with the specified id.
     *
     * @param id the id to load.
     * @return an Observable that emits the model loaded
     */
//    @Override
    public Single<R> find(int id) {
        return getManagedRealm()
                .map(realm -> {
                    R data = realm.where(realmTypeClass).equalTo("id", id).findFirst();
                    if (data == null)
                        throw new RuntimeException("Not found");
                    return realm.copyFromRealm(data);
                })
                .firstOrError();
    }

    /**
     * Saves the data
     *
     * @param data the data to save.
     * @return an Observable that emits the saved item
     */
    public Single<R> store(R data) {
        return getManagedRealm()
                .map(realm -> {
                    realm.executeTransaction(realm1 -> {
                        realm1.copyToRealmOrUpdate(data);
                    });
                    return data;
                }).firstOrError();
    }
}

