package com.piticlistudio.playednext.gamerelease.model.entity;

import com.fernandocejas.arrow.optional.Optional;
import com.piticlistudio.playednext.gamerelease.model.entity.datasource.RealmGameRelease;
import com.piticlistudio.playednext.mvp.model.entity.Mapper;
import com.piticlistudio.playednext.platform.model.entity.RealmPlatformMapper;
import com.piticlistudio.playednext.platform.model.entity.datasource.RealmPlatform;
import com.piticlistudio.playednext.releasedate.model.entity.RealmReleaseDateMapper;
import com.piticlistudio.playednext.releasedate.model.entity.datasource.RealmReleaseDate;

import javax.inject.Inject;


public class RealmGameReleaseMapper implements Mapper<RealmGameRelease, GameRelease> {

    private final RealmPlatformMapper platformMapper;
    private final RealmReleaseDateMapper dateMapper;

    @Inject
    public RealmGameReleaseMapper(RealmPlatformMapper platformMapper, RealmReleaseDateMapper dateMapper) {
        this.platformMapper = platformMapper;
        this.dateMapper = dateMapper;
    }

    /**
     * Transforms the data into an Optional model.
     *
     * @param data the data to transform
     * @return an Optional model.
     */
    @Override
    public Optional<RealmGameRelease> transform(GameRelease data) {
        if (data == null)
            return Optional.absent();

        Optional<RealmPlatform> platformOptional = platformMapper.transform(data.platform());
        if (!platformOptional.isPresent())
            return Optional.absent();
        Optional<RealmReleaseDate> realmReleaseDateOptional = dateMapper.transform(data.releaseDate());
        if (!realmReleaseDateOptional.isPresent())
            return Optional.absent();
        return Optional.of(new RealmGameRelease(platformOptional.get(), realmReleaseDateOptional.get()));
    }
}
