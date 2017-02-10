package com.piticlistudio.playednext.game.model.repository.datasource;

import com.piticlistudio.playednext.game.model.entity.datasource.IGameDatasource;
import com.piticlistudio.playednext.game.model.entity.datasource.RealmGame;
import com.piticlistudio.playednext.mvp.model.repository.datasource.BaseRealmRepository;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.functions.Function;
import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Repository for Realm entities
 * Created by jorge.garcia on 10/02/2017.
 */
public class RealmGameRepositoryImpl implements IGamedataRepository {

    /**
     * Returns a Realm instance.
     *
     * @return an Observable with a Realm instance.
     */
    protected static Observable<Realm> getManagedRealm() {
        return Observable.using(
                Realm::getDefaultInstance,
                Observable::just,
                realm -> {
                    if (!realm.isClosed())
                        realm.close();
                }
        );
    }

    /**
     * Loads the model with the specified id.
     *
     * @param id the id to load.
     * @return an Observable that emits the model loaded
     */
    @Override
    public Single<IGameDatasource> load(int id) {
        return getManagedRealm()
                .map(realm -> {
                    RealmGame data = realm.where(RealmGame.class).equalTo("id", id).findFirst();
                    return realm.copyFromRealm(data);
                })
                .map((Function<RealmGame, IGameDatasource>) realmGame -> realmGame)
                .firstOrError();
    }

    /**
     * Searches game that matches the specified query name
     *
     * @param query  the name to query.
     * @param offset the offset of the query
     * @param limit  the max amount of items to return
     * @return a list of games matching the query.
     */
    @Override
    public Single<List<IGameDatasource>> search(String query, int offset, int limit) {
        return getManagedRealm()
                .map(realm -> {
                    String _query = query;
                    if (_query == null)
                        _query = "";
                    RealmResults<RealmGame> result = realm.where(RealmGame.class).beginsWith("name", _query, Case.INSENSITIVE).findAll();
                    List<IGameDatasource> data = new ArrayList<>();
                    for (int i = 0; i < result.size(); i++) {
                        if (i >= offset && data.size() < limit) {
                            data.add(result.get(i));
                        }
                    }
                    return data;
                })
                .firstOrError();
    }
}
