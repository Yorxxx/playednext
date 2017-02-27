package com.piticlistudio.playednext.gamerelation.model.repository;

import com.piticlistudio.playednext.gamerelation.model.entity.GameRelation;

import io.reactivex.Observable;

/**
 * Repository definitions
 * Created by jorge.garcia on 27/02/2017.
 */

public interface IGameRelationRepository {

    /**
     * Loads the relation with the specified id.
     *
     * @param id the id to load.
     * @return an Observable that emits the relation loaded
     */
    Observable<GameRelation> load(int id);

    /**
     * Saves the data
     *
     * @param data the data to save
     * @return the saved data.
     */
    Observable<GameRelation> save(GameRelation data);
}
