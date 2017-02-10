package com.piticlistudio.playednext.mvp.model.entity;


import com.fernandocejas.arrow.optional.Optional;

public interface Mapper<M, T> {

    /**
     * Transforms the data into an Optional model.
     *
     * @param data the data to transform
     * @return an Optional model.
     */
    Optional<M> transform(T data);
}
