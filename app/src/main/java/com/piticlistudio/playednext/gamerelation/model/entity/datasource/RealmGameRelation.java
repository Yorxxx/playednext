package com.piticlistudio.playednext.gamerelation.model.entity.datasource;

import com.piticlistudio.playednext.game.model.entity.datasource.IGameDatasource;
import com.piticlistudio.playednext.game.model.entity.datasource.RealmGame;
import com.piticlistudio.playednext.relationinterval.model.entity.datasource.IRelationStatus;
import com.piticlistudio.playednext.relationinterval.model.entity.datasource.RealmRelationInterval;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;
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
    private RealmList<RealmRelationInterval> statuses = new RealmList<>();
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

    public void setId(int id) {
        this.id = id;
    }

    public void setGame(RealmGame game) {
        this.game = game;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Returns the list of different status for this relation.
     *
     * @return the list of statuses this relation has had.
     */
    @Override
    public List<IRelationStatus> getStatus() {
        return new ArrayList<>(statuses);
    }

    public RealmList<RealmRelationInterval> getStatuses() {
        return statuses;
    }

    public void setStatuses(RealmList<RealmRelationInterval> statuses) {
        this.statuses = statuses;
    }
}
