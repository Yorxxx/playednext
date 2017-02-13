package com.piticlistudio.playednext.company.model.entity;

import com.google.auto.value.AutoValue;

/**
 * Representation of an entity in the domain layer
 * Created by jorge.garcia on 13/02/2017.
 */
@AutoValue
public abstract class Company {

    public static Company create(int id, String name) {
        return new AutoValue_Company(id, name);
    }

    public abstract int id();

    public abstract String name();
}
