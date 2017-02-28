package com.piticlistudio.playednext.relationinterval.model.entity.datasource;

import com.fernandocejas.arrow.optional.Optional;
import com.piticlistudio.playednext.mvp.model.entity.Mapper;
import com.piticlistudio.playednext.relationinterval.model.entity.RelationInterval;

/**
 * Mapper for Relation intervals
 * Created by jorge.garcia on 27/02/2017.
 */

public class RelationIntervalMapper implements Mapper<RelationInterval, IRelationStatus> {

    /**
     * Transforms the data into an Optional model.
     *
     * @param data the data to transform
     * @return an Optional model.
     */
    @Override
    public Optional<RelationInterval> transform(IRelationStatus data) {
        if (data == null || data.getStartedAt() == 0)
            return Optional.absent();
        RelationInterval result = RelationInterval.create(data.getId(), data.getType(), data.getStartedAt());
        result.setEndAt(data.getEndedAt());
        return Optional.of(result);
    }
}
