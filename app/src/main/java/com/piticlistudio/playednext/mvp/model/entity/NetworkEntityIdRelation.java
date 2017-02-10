package com.piticlistudio.playednext.mvp.model.entity;

import com.fernandocejas.arrow.optional.Optional;

/**
 * Data defining a Network response model.
 * Most of the responses returned by the repository, just consists of a single identifier. We need to query that id to obtain the data.
 * This model contains the id and the data separated.
 * Created by jorge.garcia on 12/01/2017.
 */
public class NetworkEntityIdRelation<T> {

    public int id;
    public Optional<T> data;

    public NetworkEntityIdRelation(int id, Optional<T> data) {
        this.id = id;
        this.data = data;
    }
}
