package com.piticlistudio.playednext.relationinterval.model.entity;


import com.google.auto.value.AutoValue;

/**
 * Entity defining a relation status interval
 * Created by jorge.garcia on 30/01/2017.
 */
@AutoValue
public abstract class RelationInterval {

    private long endAt;

    public static RelationInterval create(int id, RelationType type, long createdAt) {
        return new AutoValue_RelationInterval(id, type, createdAt);
    }

    public abstract int id();

    public abstract RelationType type();

    public abstract long startAt();

    public long getEndAt() {
        return endAt;
    }

    public void setEndAt(long endAt) {
        this.endAt = endAt;
    }

    public enum RelationType {
        NONE, PENDING, PLAYING, DONE
    }
}
