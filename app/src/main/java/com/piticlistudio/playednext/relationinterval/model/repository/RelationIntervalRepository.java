package com.piticlistudio.playednext.relationinterval.model.repository;

import android.util.Log;

import com.piticlistudio.playednext.relationinterval.model.entity.RelationInterval;
import com.piticlistudio.playednext.relationinterval.model.repository.datasource.RealmRelationIntervalRepositoryImpl;

import javax.inject.Inject;

/**
 * Repository for Relation intervals
 * Created by jorge.garcia on 06/02/2017.
 */
public class RelationIntervalRepository implements IRelationIntervalRepository {

    private final RealmRelationIntervalRepositoryImpl localImpl;

    @Inject
    public RelationIntervalRepository(RealmRelationIntervalRepositoryImpl localImpl) {
        this.localImpl = localImpl;
    }

    /**
     * Creates a new instance
     *
     * @param relationType the type of the instance to create
     * @return a created instance
     */
    @Override
    public RelationInterval create(RelationInterval.RelationType relationType) {
        int id = localImpl.getAutoincrementId();
        return RelationInterval.create(id, relationType, System.currentTimeMillis());
    }
}
