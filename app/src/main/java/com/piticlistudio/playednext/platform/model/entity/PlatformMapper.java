package com.piticlistudio.playednext.platform.model.entity;

import com.fernandocejas.arrow.optional.Optional;
import com.piticlistudio.playednext.mvp.model.entity.Mapper;
import com.piticlistudio.playednext.platform.model.entity.datasource.IPlatformData;

import javax.inject.Inject;

/**
 * Maps an IPlatformData entity into a Platform entity
 * Created by jorge.garcia on 15/02/2017.
 */
public class PlatformMapper implements Mapper<Platform, IPlatformData> {

    @Inject
    public PlatformMapper() {
    }

    /**
     * Transforms the data into an Optional model.
     *
     * @param data the data to transform
     * @return an Optional model.
     */
    @Override
    public Optional<Platform> transform(IPlatformData data) {
        if (data == null || data.getName() == null)
            return Optional.absent();
        return Optional.of(Platform.create(data.getId(), data.getName()));
    }
}
