package com.piticlistudio.playednext.collection.model.entity;

import com.fernandocejas.arrow.optional.Optional;
import com.piticlistudio.playednext.collection.model.entity.datasource.RealmCollection;
import com.piticlistudio.playednext.mvp.model.entity.Mapper;

import javax.inject.Inject;

/**
 * Class that allows to map an Collection entity into a RealmCollection entity
 * Created by jorge.garcia on 10/02/2017.
 */
public class RealmCollectionMapper implements Mapper<RealmCollection, Collection> {

    @Inject
    public RealmCollectionMapper() {
    }

    /**
     * Transforms the data into an Optional model.
     *
     * @param data the data to transform
     * @return an Optional model.
     */
    @Override
    public Optional<RealmCollection> transform(Collection data) {
        if (data == null) {
            return Optional.absent();
        }
        RealmCollection result = new RealmCollection(data.id(), data.name());
        return Optional.of(result);
    }
}
