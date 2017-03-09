package com.piticlistudio.playednext.gamerelation.model.entity;

import com.fernandocejas.arrow.optional.Optional;
import com.google.auto.value.AutoValue;
import com.piticlistudio.playednext.boost.model.entity.BoostItem;
import com.piticlistudio.playednext.boost.model.entity.IBoostable;
import com.piticlistudio.playednext.game.model.entity.Game;
import com.piticlistudio.playednext.relationinterval.model.entity.RelationInterval;

import java.util.ArrayList;
import java.util.List;

/**
 * Entity defining a relation with a game
 * Created by jorge.garcia on 24/02/2017.
 */
@AutoValue
public abstract class GameRelation implements IBoostable {

    private long updatedAt;
    private List<RelationInterval> statuses = new ArrayList<>();
    private List<BoostItem> boosts = new ArrayList<>();

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

    /**
     * Returns if the current entity has the boost enabled
     *
     * @return true if is enabled. False otherwise
     */
    @Override
    public boolean isBoostEnabled() {
        return getCurrent().isPresent() && getCurrent().get().type() == RelationInterval.RelationType.PENDING;
    }

    /**
     * Returns the last release
     *
     * @return the last release
     */
    @Override
    public long getLastRelease() {
        return game().getLastRelease();
    }

    /**
     * Returns the first release
     *
     * @return the first release
     */
    @Override
    public long getFirstRelease() {
        return game().getFirstRelease();
    }

    /**
     * Returns since when is the item being on the todo list
     *
     * @return the timestamp
     */
    @Override
    public long getWaitingStartedAt() {
        if (getCurrent().isPresent() && getCurrent().get().type() == RelationInterval.RelationType.PENDING) {
            return getCurrent().get().startAt();
        }
        return 0;
    }

    /**
     * Returns the number of times the item has been completed
     *
     * @return the number of times
     */
    @Override
    public int getCompletedCount() {
        int count = 0;
        for (RelationInterval statuse : statuses) {
            if (statuse.type() == RelationInterval.RelationType.DONE)
                count++;
        }
        return count;
    }

    public List<BoostItem> getBoosts() {
        return boosts;
    }

    public void setBoosts(List<BoostItem> boosts) {
        this.boosts = boosts;
    }

    public long getBoostValue() {
        long value = 0;
        for (BoostItem boost : boosts) {
            value += boost.value();
        }
        return value;
    }
}
