package com.piticlistudio.playednext.game.model.repository.datasource;

import com.piticlistudio.playednext.game.model.entity.datasource.IGameDatasource;

import java.util.List;

import io.reactivex.Observable;

public interface IGamedataRepository {

    /**
     * Loads the game with the specified id.
     *
     * @param id the id to load.
     * @return an Observable that emits the game loaded
     */
    Observable<IGameDatasource> load(int id);

    /**
     * Searches game that matches the specified query name
     *
     * @param query  the name to query.
     * @param offset the offset of the query
     * @param limit  the max amount of items to return
     * @return a list of games matching the query.
     */
    Observable<List<IGameDatasource>> search(String query, int offset, int limit);

    /**
     * Saves the data
     *
     * @param data the data to save
     * @return an Observable that emits the saved data.
     */
    Observable<IGameDatasource> save(IGameDatasource data);
}
