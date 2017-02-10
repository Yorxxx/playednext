package com.piticlistudio.playednext.collection.model.entity;

import com.google.auto.value.AutoValue;

/**
 * Domain entity that defines a collection
 * Created by jorge.garcia on 10/02/2017.
 */
@AutoValue
public abstract class Collection {

    public abstract int id();
    public abstract String name();

    public static Collection create(int id, String name) {
        return new AutoValue_Collection(id, name);
    }
}
