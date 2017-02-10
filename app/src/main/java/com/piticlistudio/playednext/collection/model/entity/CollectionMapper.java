package com.piticlistudio.playednext.collection.model.entity;

import com.fernandocejas.arrow.optional.Optional;
import com.piticlistudio.playednext.collection.model.entity.datasource.ICollectionData;
import com.piticlistudio.playednext.mvp.model.entity.Mapper;

import javax.inject.Inject;

/**
 * Class that allows to map an ICollectionData entity into a Collection entity
 * Created by jorge.garcia on 10/02/2017.
 */

public class CollectionMapper implements Mapper<Collection, ICollectionData> {

    @Inject
    public CollectionMapper() {
    }

    /**
     * Transforms the data into an Optional model.
     *
     * @param data the data to transform
     * @return an Optional model.
     */
    @Override
    public Optional<Collection> transform(ICollectionData data) {
        if (data == null || data.getName() == null) {
            return Optional.absent();
        }
        return Optional.of(Collection.create(data.getId(), data.getName()));
    }
}
