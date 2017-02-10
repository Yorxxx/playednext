package com.piticlistudio.playednext.game.model.entity;

import com.fernandocejas.arrow.optional.Optional;
import com.piticlistudio.playednext.collection.model.entity.Collection;
import com.piticlistudio.playednext.collection.model.entity.CollectionMapper;
import com.piticlistudio.playednext.game.model.entity.datasource.IGameDatasource;
import com.piticlistudio.playednext.mvp.model.entity.Mapper;

import javax.inject.Inject;

public class GameMapper implements Mapper<Game, IGameDatasource> {

    private final CollectionMapper collectionMapper;

    @Inject
    public GameMapper(CollectionMapper collectionMapper) {
        this.collectionMapper = collectionMapper;
    }

    /**
     * Transforms the data into an Optional model.
     *
     * @param data the data to transform
     * @return an Optional model.
     */
    @Override
    public Optional<Game> transform(IGameDatasource data) {
        if (data == null || data.getName() == null)
            return Optional.absent();
        Game result = Game.create(data.getId(), data.getName());
        result.storyline = data.getStoryline();
        result.summary = data.getSummary();

        if (data.getCollection().isPresent() && data.getCollection().get().data.isPresent()) {
            result.collection = collectionMapper.transform(data.getCollection().get().getData());
        }

        return Optional.of(result);
    }
}
