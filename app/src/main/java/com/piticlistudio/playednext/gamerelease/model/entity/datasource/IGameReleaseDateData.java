package com.piticlistudio.playednext.gamerelease.model.entity.datasource;

import com.piticlistudio.playednext.mvp.model.entity.NetworkEntityIdRelation;
import com.piticlistudio.playednext.platform.model.entity.datasource.IPlatformData;
import com.piticlistudio.playednext.releasedate.model.entity.datasource.IReleaseDateData;

public interface IGameReleaseDateData {

    /**
     * Returns the platform associated to the release of this game
     *
     * @return the platform
     */
    NetworkEntityIdRelation<IPlatformData> getPlatform();

    /**
     * Returns the release date
     *
     * @return the date
     */
    IReleaseDateData getDate();
}
