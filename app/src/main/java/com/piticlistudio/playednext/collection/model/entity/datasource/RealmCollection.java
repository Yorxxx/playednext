package com.piticlistudio.playednext.collection.model.entity.datasource;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Representation on Realm of ICollection entity
 * @see ICollectionData
 * Created by jorge.garcia on 10/02/2017.
 */

public class RealmCollection extends RealmObject implements ICollectionData {

    @PrimaryKey
    public int id;

    @Required
    public String name;

    public RealmCollection() {
        // Empty
    }

    public RealmCollection(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Returns the id
     *
     * @return the id
     */
    @Override
    public int getId() {
        return id;
    }

    /**
     * Returns the name
     *
     * @return the name
     */
    @Override
    public String getName() {
        return name;
    }
}
