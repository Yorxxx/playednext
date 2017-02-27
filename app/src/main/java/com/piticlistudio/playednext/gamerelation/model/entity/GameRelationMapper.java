package com.piticlistudio.playednext.gamerelation.model.entity;

import com.fernandocejas.arrow.optional.Optional;
import com.piticlistudio.playednext.game.model.entity.Game;
import com.piticlistudio.playednext.game.model.entity.GameMapper;
import com.piticlistudio.playednext.gamerelation.model.entity.datasource.IGameRelationDatasource;
import com.piticlistudio.playednext.mvp.model.entity.Mapper;
import com.piticlistudio.playednext.relationinterval.model.entity.RelationInterval;
import com.piticlistudio.playednext.relationinterval.model.entity.datasource.IRelationStatus;
import com.piticlistudio.playednext.relationinterval.model.entity.datasource.RelationIntervalMapper;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Mapper for IGameRelationDatasource to GameRelation entities
 * Created by jorge.garcia on 24/02/2017.
 */

public class GameRelationMapper implements Mapper<GameRelation, IGameRelationDatasource> {

    private final GameMapper gameMapper;
    private final RelationIntervalMapper intervalMapper;

    @Inject
    public GameRelationMapper(GameMapper gameMapper, RelationIntervalMapper intervalMapper) {
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
    public Optional<GameRelation> transform(IGameRelationDatasource data) {
        if (data == null || data.getGame() == null || data.getId() == 0)
            return Optional.absent();

        Optional<Game> gameOptional = gameMapper.transform(data.getGame());
        if (!gameOptional.isPresent())
            return Optional.absent();
        GameRelation result = GameRelation.create(gameOptional.get(), data.getCreatedAt());
        result.setUpdatedAt(data.getUpdatedAt());
        List<RelationInterval> status = new ArrayList<>();
        for (IRelationStatus iRelationStatus : data.getStatus()) {
            Optional<RelationInterval> interval = intervalMapper.transform(iRelationStatus);
            if (interval.isPresent())
                status.add(interval.get());
        }
        result.setStatuses(status);
        return Optional.of(result);
    }
}
