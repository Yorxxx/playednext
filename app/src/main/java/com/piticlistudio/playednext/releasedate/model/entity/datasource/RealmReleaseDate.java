package com.piticlistudio.playednext.releasedate.model.entity.datasource;

import com.fernandocejas.arrow.optional.Optional;

import io.realm.RealmObject;
import io.realm.annotations.Required;

/**
 * Realm representation of IReleaseDate
 * Created by jorge.garcia on 15/02/2017.
 */

public class RealmReleaseDate extends RealmObject implements IReleaseDateData {

    @Required
    private String human;

    private long date;

    public RealmReleaseDate() {
    }

    public RealmReleaseDate(String human, long date) {
        this.human = human;
        this.date = date;
    }

    /**
     * Returns the date of the release in human readable style
     * This is mainly needed because some release date are not concrete (ie: TBD, YYYY, YYYY-Q2...)
     *
     * @return the human date
     */
    @Override
    public String getHumanDate() {
        return human;
    }

    /**
     * Returns the Unix epoch time of the release
     *
     * @return the release
     */
    @Override
    public Optional<Long> getDate() {
        if (date == 0)
            return Optional.absent();
        return Optional.of(date);
    }

    public void setDate(long date) {
        this.date = date;
    }
}
