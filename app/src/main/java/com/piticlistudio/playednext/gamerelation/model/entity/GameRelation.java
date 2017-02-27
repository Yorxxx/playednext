package com.piticlistudio.playednext.gamerelation.model.entity;

import com.google.auto.value.AutoValue;
import com.piticlistudio.playednext.game.model.entity.Game;
import com.piticlistudio.playednext.relationinterval.model.entity.RelationInterval;

import java.util.ArrayList;
import java.util.List;

/**
 * Entity defining a relation with a game
 * Created by jorge.garcia on 24/02/2017.
 */
@AutoValue
public abstract class GameRelation {

    public abstract int id();

    public abstract Game game();

    public abstract long createdAt();

    private long updatedAt;

    private List<RelationInterval> statuses = new ArrayList<>();

    public static GameRelation create(Game game, long createdAt) {
        GameRelation data = new AutoValue_GameRelation(game.id(), game, createdAt);
        data.setUpdatedAt(createdAt);
        return data;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<RelationInterval> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<RelationInterval> statuses) {
        this.statuses = statuses;
    }
}
