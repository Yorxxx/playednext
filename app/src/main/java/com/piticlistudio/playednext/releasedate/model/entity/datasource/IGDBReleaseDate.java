package com.piticlistudio.playednext.releasedate.model.entity.datasource;

import com.fernandocejas.arrow.optional.Optional;
import com.google.auto.value.AutoValue;
import com.piticlistudio.playednext.utils.AutoGson;

/**
 * Implementation of IReleaseDateData by IGDB
 * Created by jorge.garcia on 15/02/2017.
 */
@AutoGson
@AutoValue
public abstract class IGDBReleaseDate implements IReleaseDateData {

    public static IGDBReleaseDate create(long date, String human) {
        return new AutoValue_IGDBReleaseDate(date, human);
    }

    public abstract long date();

    public abstract String human();

    /**
     * Returns the date of the release in human readable style
     * This is mainly needed because some release date are not concrete (ie: TBD, YYYY, YYYY-Q2...)
     *
     * @return the human date
     */
    @Override
    public String getHumanDate() {
        return human();
    }

    /**
     * Returns the Unix epoch time of the release
     *
     * @return the release
     */
    @Override
    public Optional<Long> getDate() {
        if (date() == 0)
            return Optional.absent();
        return Optional.of(date());
    }
}
