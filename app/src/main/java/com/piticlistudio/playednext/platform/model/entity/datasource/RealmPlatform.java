package com.piticlistudio.playednext.platform.model.entity.datasource;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Realm implementation of a IPlatformData
 * Created by jorge.garcia on 14/02/2017.
 */

public class RealmPlatform extends RealmObject implements IPlatformData {

    @PrimaryKey
    private int id;

    @Required
    private String name;

    public RealmPlatform() {
        // Empty
    }

    public RealmPlatform(int id, String name) {
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
