package com.piticlistudio.playednext.gamerelation.model.entity.datasource;

import com.piticlistudio.playednext.game.model.entity.datasource.IGameDatasource;
import com.piticlistudio.playednext.game.model.entity.datasource.RealmGame;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * GameRelation representation on Realm
 * Created by jorge.garcia on 24/02/2017.
 */
public class RealmGameRelation extends RealmObject implements IGameRelationDatasource {

    @PrimaryKey
    private int id;

    private RealmGame game;

    private long updatedAt;
    private long createdAt;

    public RealmGameRelation() {
        // Empty
    }

    public RealmGameRelation(int id, RealmGame game, long createdAt, long updatedAt) {
        this.id = id;
        this.game = game;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
    }

    /**
     * Returns the id
     *
     * @return the id.
     */
    @Override
    public int getId() {
        return id;
    }

    /**
     * Returns the game
     *
     * @return the game associated to the game
     */
    @Override
    public IGameDatasource getGame() {
        return game;
    }

    /**
     * Returns the creation in Unix timestamp
     *
     * @return the creation date.
     */
    @Override
    public long getCreatedAt() {
        return createdAt;
    }

    /**
     * Returns the update time in Unix Epoch
     *
     * @return the update time.
     */
    @Override
    public long getUpdatedAt() {
        return updatedAt;
    }
}
