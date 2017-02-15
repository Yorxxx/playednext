package com.piticlistudio.playednext.gamerelease.model.entity.datasource;


import com.fernandocejas.arrow.optional.Optional;
import com.piticlistudio.playednext.mvp.model.entity.NetworkEntityIdRelation;
import com.piticlistudio.playednext.platform.model.entity.datasource.IPlatformData;
import com.piticlistudio.playednext.platform.model.entity.datasource.RealmPlatform;
import com.piticlistudio.playednext.releasedate.model.entity.datasource.IReleaseDateData;
import com.piticlistudio.playednext.releasedate.model.entity.datasource.RealmReleaseDate;


import io.realm.RealmObject;

public class RealmGameRelease extends RealmObject implements IGameReleaseDateData {

    private RealmPlatform platform;
    private RealmReleaseDate release;

    public RealmGameRelease() {
    }

    public RealmGameRelease(RealmPlatform platform, RealmReleaseDate release) {
        this.platform = platform;
        this.release = release;
    }

    /**
     * Returns the platform associated to the release of this game
     *
     * @return the platform
     */
    @Override
    public NetworkEntityIdRelation<IPlatformData> getPlatform() {
        return new NetworkEntityIdRelation<IPlatformData>(platform.getId(), Optional.of(platform));
    }

    /**
     * Returns the release date
     *
     * @return the date
     */
    @Override
    public IReleaseDateData getDate() {
        return release;
    }
}
