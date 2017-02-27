package com.piticlistudio.playednext.gamerelation.model.entity.datasource;

import com.piticlistudio.playednext.game.model.entity.datasource.IGameDatasource;
import com.piticlistudio.playednext.relationinterval.model.entity.datasource.IRelationStatus;

import java.util.List;

/**
 * Definition methods for GameRelation datasource entities
 * Created by jorge.garcia on 24/02/2017.
 */
public interface IGameRelationDatasource {

    /**
     * Returns the id
     *
     * @return the id.
     */
    int getId();

    /**
     * Returns the game
     *
     * @return the game associated to the game
     */
    IGameDatasource getGame();

    /**
     * Returns the list of different status for this relation.
     *
     * @return the list of statuses this relation has had.
     */
    List<IRelationStatus> getStatus();

    /**
     * Returns the creation in Unix timestamp
     *
     * @return the creation date.
     */
    long getCreatedAt();

    /**
     * Returns the update time in Unix Epoch
     *
     * @return the update time.
     */
    long getUpdatedAt();
}
