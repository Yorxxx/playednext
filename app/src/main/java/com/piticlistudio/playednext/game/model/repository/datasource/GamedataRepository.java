package com.piticlistudio.playednext.game.model.repository.datasource;

import com.piticlistudio.playednext.game.model.entity.datasource.IGameDatasource;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;

/**
 * A Repository for Gamedatasource
 * Created by jorge.garcia on 10/02/2017.
 */

public class GamedataRepository implements IGamedataRepository {

    private final IGamedatasourceRepository<IGameDatasource> dbImpl;
    private final IGamedatasourceRepository<IGameDatasource> netImpl;

    @Inject
    public GamedataRepository(@Named("db") IGamedatasourceRepository<IGameDatasource> dbImpl,
                              @Named("net") IGamedatasourceRepository<IGameDatasource> netImpl) {
        this.dbImpl = dbImpl;
        this.netImpl = netImpl;
    }

    /**
     * Loads the game with the specified id.
     *
     * @param id the id to load.
     * @return an Observable that emits the game loaded
     */
    @Override
    public Observable<IGameDatasource> load(int id) {
        return dbImpl.load(id)
                .onErrorResumeNext(__ -> netImpl.load(id))
                .toObservable();
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
    public Observable<List<IGameDatasource>> search(String query, int offset, int limit) {
        return netImpl.search(query, offset, limit)
                .toObservable();
    }

    /**
     * Saves the data
     *
     * @param data the data to save
     * @return an Observable that emits the saved data.
     */
    @Override
    public Observable<IGameDatasource> save(IGameDatasource data) {
        return dbImpl.save(data).toObservable();
    }
}
