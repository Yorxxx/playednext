package com.piticlistudio.playednext.game.model.repository.datasource;

import com.piticlistudio.playednext.game.model.GamedataModule;
import com.piticlistudio.playednext.game.model.entity.datasource.IGameDatasource;
import com.piticlistudio.playednext.game.model.entity.datasource.NetGame;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.functions.Function;

/**
 * A repository for NetGames
 * Created by jorge.garcia on 10/02/2017.
 */

public class NetGameRepositoryImpl implements IGamedatasourceRepository<IGameDatasource> {

    private final GamedataModule.NetService service;

    @Inject
    public NetGameRepositoryImpl(GamedataModule.NetService service) {
        this.service = service;
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
        return service.search(offset, query, "*", limit)
                .firstOrError()
                .retry(1)
                .flatMap(netGames -> Observable.fromIterable(netGames)
                        .map((Function<NetGame, IGameDatasource>) netGame -> netGame)
                        .toList());
    }

    /**
     * Loads the model with the specified id.
     *
     * @param id the id to load.
     * @return an Observable that emits the model loaded
     */
    @Override
    public Single<IGameDatasource> load(int id) {
        return service.load(id, "*")
                .retry(1)
                .map((Function<List<NetGame>, IGameDatasource>) netGames -> {
                    if (netGames.size() == 0)
                        throw new RuntimeException("Not found");
                    return netGames.get(0);
                }).firstOrError();
    }

    /**
     * Saves the data
     *
     * @param data the data to save
     * @return an Observable that emits the saved data
     */
    @Override
    public Single<IGameDatasource> save(IGameDatasource data) {
        return Single.error(new Exception("Forbidden"));
    }
}
