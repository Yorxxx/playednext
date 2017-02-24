package com.piticlistudio.playednext.gamerelation.model.entity;

import com.fernandocejas.arrow.optional.Optional;
import com.piticlistudio.playednext.game.model.entity.RealmGameMapper;
import com.piticlistudio.playednext.game.model.entity.datasource.RealmGame;
import com.piticlistudio.playednext.gamerelation.model.entity.datasource.RealmGameRelation;
import com.piticlistudio.playednext.mvp.model.entity.Mapper;

import javax.inject.Inject;

/**
 * Maps a GameRelation into a RealmGameRelation
 * Created by jorge.garcia on 24/02/2017.
 */
public class RealmGameRelationMapper implements Mapper<RealmGameRelation, GameRelation> {

    private final RealmGameMapper gameMapper;

    @Inject
    public RealmGameRelationMapper(RealmGameMapper gameMapper) {
        this.gameMapper = gameMapper;
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
        return Optional.of(result);
    }
}
