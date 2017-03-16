package com.piticlistudio.playednext.gamerelation.model.entity;

import com.fernandocejas.arrow.optional.Optional;
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

    private long updatedAt;
    private List<RelationInterval> statuses = new ArrayList<>();

    public static GameRelation create(Game game, long createdAt) {
        GameRelation data = new AutoValue_GameRelation(game.id(), game, createdAt);
        data.setUpdatedAt(createdAt);
        return data;
    }

    public abstract int id();

    public abstract Game game();

    public abstract long createdAt();

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<RelationInterval> getStatuses() {
        if (statuses == null)
            statuses = new ArrayList<>();
        return statuses;
    }

    public void setStatuses(List<RelationInterval> statuses) {
        this.statuses = statuses;
    }

    /**
     * Returns the current relation (or last)
     *
     * @return the current valid relation status
     */
    public Optional<RelationInterval> getCurrent() {
        if (this.statuses == null || this.statuses.isEmpty())
            return Optional.absent();
        if (this.statuses.size() == 1) {
            if (this.statuses.get(0).getEndAt() > 0)
                return Optional.absent();
            return Optional.of(this.statuses.get(0));
        }
        for (RelationInterval statuse : this.statuses) {
            if (statuse.getEndAt() == 0)
                return Optional.of(statuse);
        }
        return Optional.absent();
    }
}
