package com.piticlistudio.playednext.relationinterval.model.entity.datasource;


import com.piticlistudio.playednext.relationinterval.model.entity.RelationInterval;

/**
 * Definition methods
 * Created by jorge.garcia on 30/01/2017.
 */
public interface IRelationStatus {

    /**
     * Returns the id
     *
     * @return the id
     */
    int getId();

    /**
     * Returns the type
     *
     * @return the type;
     */
    RelationInterval.RelationType getType();

    /**
     * Returns the date when this status started
     *
     * @return the time in ms.
     */
    long getStartedAt();

    /**
     * Returns the date when this status ended
     *
     * @return the time in ms
     */
    long getEndedAt();
}
