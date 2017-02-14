package com.piticlistudio.playednext.game.model.entity;

import com.fernandocejas.arrow.optional.Optional;
import com.piticlistudio.playednext.game.model.entity.datasource.RealmGame;
import com.piticlistudio.playednext.image.model.entity.RealmImageDataMapper;
import com.piticlistudio.playednext.image.model.entity.datasource.RealmImageData;
import com.piticlistudio.playednext.mvp.model.entity.Mapper;

import javax.inject.Inject;

/**
 * Maps a Game entity into a RealGame entity
 * Created by jorge.garcia on 14/02/2017.
 */

public class RealmGameMapper implements Mapper<RealmGame, Game> {

    private final RealmImageDataMapper imageMapper;

    @Inject
    public RealmGameMapper(RealmImageDataMapper imageMapper) {
        this.imageMapper = imageMapper;
    }

    /**
     * Transforms the data into an Optional model.
     *
     * @param data the data to transform
     * @return an Optional model.
     */
    @Override
    public Optional<RealmGame> transform(Game data) {
        if (data == null)
            return Optional.absent();
        RealmGame result = new RealmGame();
        result.setId(data.id());
        result.setName(data.title());
        result.setStoryline(data.storyline);
        result.setSummary(data.summary);

        if (data.cover != null && data.cover.isPresent()) {
            Optional<RealmImageData> image = imageMapper.transform(data.cover.get());
            if (image.isPresent())
                result.setCover(image.get());
        }

        return Optional.of(result);
    }
}
