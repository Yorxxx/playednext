package com.piticlistudio.playednext.gamerelation.model.entity;

import com.google.auto.value.AutoValue;
import com.piticlistudio.playednext.game.model.entity.Game;

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
}
