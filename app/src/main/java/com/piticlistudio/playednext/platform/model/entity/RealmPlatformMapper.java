package com.piticlistudio.playednext.platform.model.entity;

import com.fernandocejas.arrow.optional.Optional;
import com.piticlistudio.playednext.mvp.model.entity.Mapper;
import com.piticlistudio.playednext.platform.model.entity.datasource.RealmPlatform;

import javax.inject.Inject;

/**
 * Maps a Platform entity into a RealmPlatform
 * Created by jorge.garcia on 15/02/2017.
 */

public class RealmPlatformMapper implements Mapper<RealmPlatform, Platform> {

    @Inject
    public RealmPlatformMapper() {
    }

    /**
     * Transforms the data into an Optional model.
     *
     * @param data the data to transform
     * @return an Optional model.
     */
    @Override
    public Optional<RealmPlatform> transform(Platform data) {
        if (data == null)
            return Optional.absent();
        return Optional.of(new RealmPlatform(data.id(), data.name()));
    }
}
