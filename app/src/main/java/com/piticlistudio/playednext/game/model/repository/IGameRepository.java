package com.piticlistudio.playednext.game.model.repository;


import com.piticlistudio.playednext.game.model.entity.Game;

import java.util.List;

import io.reactivex.Observable;

/**
 * Repository definitions.
 * Created by jorge.garcia on 16/01/2017.
 */

public interface IGameRepository {

    /**
     * Loads the game with the specified id.
     *
     * @param id the id to load.
     * @return an Observable that emits the game loaded
     */
    Observable<Game> load(int id);

    /**
     * Searches game that matches the specified query name
     *
     * @param query  the name to query.
     * @param offset the offset of the query
     * @param limit  the max amount of items to return
     * @return a list of games matching the query.
     */
    Observable<List<Game>> search(String query, int offset, int limit);
}

