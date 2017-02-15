package com.piticlistudio.playednext.gamerelease.model.entity;

import com.fernandocejas.arrow.optional.Optional;
import com.piticlistudio.playednext.gamerelease.model.entity.datasource.IGameReleaseDateData;
import com.piticlistudio.playednext.mvp.model.entity.Mapper;
import com.piticlistudio.playednext.platform.model.entity.Platform;
import com.piticlistudio.playednext.platform.model.entity.PlatformMapper;
import com.piticlistudio.playednext.releasedate.model.entity.ReleaseDate;
import com.piticlistudio.playednext.releasedate.model.entity.ReleaseDateMapper;

import javax.inject.Inject;


public class GameReleaseMapper implements Mapper<GameRelease, IGameReleaseDateData> {

    private final ReleaseDateMapper dateMapper;
    private final PlatformMapper platformMapper;

    @Inject
    public GameReleaseMapper(ReleaseDateMapper dateMapper, PlatformMapper platformMapper) {
        this.dateMapper = dateMapper;
        this.platformMapper = platformMapper;
    }

    /**
     * Transforms the data into an Optional model.
     *
     * @param data the data to transform
     * @return an Optional model.
     */
    @Override
    public Optional<GameRelease> transform(IGameReleaseDateData data) {
        if (data == null)
            return Optional.absent();
        if (!data.getPlatform().data.isPresent())
            return Optional.absent();
        else {
            Optional<Platform> platformOptional = platformMapper.transform(data.getPlatform().getData());
            if (!platformOptional.isPresent())
                return Optional.absent();

            Optional<ReleaseDate> releaseDateOptional = dateMapper.transform(data.getDate());
            if (!releaseDateOptional.isPresent())
                return Optional.absent();
            return Optional.of(GameRelease.create(platformOptional.get(), releaseDateOptional.get()));
        }
    }
}
