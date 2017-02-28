package com.piticlistudio.playednext.relationinterval.model.repository;

import com.piticlistudio.playednext.relationinterval.model.entity.RelationInterval;

public interface IRelationIntervalRepository {

    /**
     * Creates a new instance
     *
     * @param relationType the type of the instance to create
     * @return a created instance
     */
    RelationInterval create(RelationInterval.RelationType relationType);
}
