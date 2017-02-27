package com.piticlistudio.playednext.relationinterval.model.entity.datasource;


import com.piticlistudio.playednext.relationinterval.model.entity.RelationInterval;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class RealmRelationInterval extends RealmObject implements IRelationStatus {

    @PrimaryKey
    private int id;

    private int type;

    private long startedAt;
    private long endedAt;

    public RealmRelationInterval() {
        // Empty
    }

    public RealmRelationInterval(int id, int type, long startedAt, long endedAt) {
        this.id = id;
        this.type = type;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the type
     *
     * @return the type;
     */
    @Override
    public RelationInterval.RelationType getType() {
        return RelationInterval.RelationType.values()[type];
    }

    public void setType(int type) {
        this.type = type;
    }

    /**
     * Returns the date when this status started
     *
     * @return the time in ms.
     */
    @Override
    public long getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(long startedAt) {
        this.startedAt = startedAt;
    }

    public long getEndedAt() {
        return endedAt;
    }

    public void setEndedAt(long endedAt) {
        this.endedAt = endedAt;
    }


}
