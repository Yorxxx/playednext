package com.piticlistudio.playednext.releasedate.model.entity.datasource;

import com.fernandocejas.arrow.optional.Optional;

/**
 * Protocol definition
 * Created by jorge.garcia on 15/02/2017.
 */
public interface IReleaseDateData {

    /**
     * Returns the date of the release in human readable style
     * This is mainly needed because some release date are not concrete (ie: TBD, YYYY, YYYY-Q2...)
     * @return the human date
     */
    String getHumanDate();

    /**
     * Returns the Unix epoch time of the release
     * @return the release
     */
    Optional<Long> getDate();
}
