package com.piticlistudio.playednext.boost.model.entity;

import com.google.auto.value.AutoValue;

/**
 * Item representing a Boost of relation
 * Created by jorge.garcia on 08/03/2017.
 */
@AutoValue
public abstract class BoostItem {

    public static BoostItem create(long value, int type) {
        return new AutoValue_BoostItem(value, type);
    }

    public abstract long value();

    public abstract int type();
}
