package com.piticlistudio.playednext.relationinterval.model.entity;

import com.fernandocejas.arrow.optional.Optional;
import com.piticlistudio.playednext.mvp.model.entity.Mapper;
import com.piticlistudio.playednext.relationinterval.model.entity.datasource.RealmRelationInterval;

/**
 * Mapper for RealmRelation interval
 * Created by jorge.garcia on 27/02/2017.
 */

public class RealmRelationIntervalMapper implements Mapper<RealmRelationInterval, RelationInterval> {

    /**
     * Transforms the data into an Optional model.
     *
     * @param data the data to transform
     * @return an Optional model.
     */
    @Override
    public Optional<RealmRelationInterval> transform(RelationInterval data) {
        if (data == null)
            return Optional.absent();

        RealmRelationInterval result = new RealmRelationInterval();
        result.setId(data.id());
        result.setType(data.type().ordinal());
        result.setStartedAt(data.startAt());
        result.setEndedAt(data.getEndAt());
        return Optional.of(result);
    }
}
