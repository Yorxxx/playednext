package com.piticlistudio.playednext.gamerelation.model.entity;

import com.fernandocejas.arrow.optional.Optional;
import com.piticlistudio.playednext.game.model.entity.RealmGameMapper;
import com.piticlistudio.playednext.game.model.entity.datasource.RealmGame;
import com.piticlistudio.playednext.gamerelation.model.entity.datasource.RealmGameRelation;
import com.piticlistudio.playednext.mvp.model.entity.Mapper;
import com.piticlistudio.playednext.relationinterval.model.entity.RealmRelationIntervalMapper;
import com.piticlistudio.playednext.relationinterval.model.entity.RelationInterval;
import com.piticlistudio.playednext.relationinterval.model.entity.datasource.RealmRelationInterval;

import javax.inject.Inject;

import io.realm.RealmList;

/**
 * Maps a GameRelation into a RealmGameRelation
 * Created by jorge.garcia on 24/02/2017.
 */
public class RealmGameRelationMapper implements Mapper<RealmGameRelation, GameRelation> {

    private final RealmGameMapper gameMapper;
    private final RealmRelationIntervalMapper intervalMapper;

    @Inject
    public RealmGameRelationMapper(RealmGameMapper gameMapper, RealmRelationIntervalMapper intervalMapper) {
        this.gameMapper = gameMapper;
        this.intervalMapper = intervalMapper;
    }

    /**
     * Transforms the data into an Optional model.
     *
     * @param data the data to transform
     * @return an Optional model.
     */
    @Override
    public Optional<RealmGameRelation> transform(GameRelation data) {
        if (data == null)
            return Optional.absent();

        Optional<RealmGame> realmGameOptional = gameMapper.transform(data.game());
        if (!realmGameOptional.isPresent())
            return Optional.absent();
        RealmGameRelation result = new RealmGameRelation(data.id(), realmGameOptional.get(), data.createdAt(), data.getUpdatedAt());
        RealmList<RealmRelationInterval> status = new RealmList<>();
        for (RelationInterval interval : data.getStatuses()) {
            Optional<RealmRelationInterval> realmInterval = intervalMapper.transform(interval);
            if (realmInterval.isPresent())
                status.add(realmInterval.get());
        }
        result.setStatuses(status);
        return Optional.of(result);
    }
}
