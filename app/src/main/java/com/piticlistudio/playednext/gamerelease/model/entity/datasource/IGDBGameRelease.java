package com.piticlistudio.playednext.gamerelease.model.entity.datasource;

import com.fernandocejas.arrow.optional.Optional;
import com.google.auto.value.AutoValue;
import com.piticlistudio.playednext.mvp.model.entity.NetworkEntityIdRelation;
import com.piticlistudio.playednext.platform.model.entity.datasource.IPlatformData;
import com.piticlistudio.playednext.releasedate.model.entity.datasource.IReleaseDateData;
import com.piticlistudio.playednext.releasedate.model.entity.datasource.NetReleaseDate;
import com.piticlistudio.playednext.utils.AutoGson;

@AutoGson
@AutoValue
public abstract class IGDBGameRelease implements IGameReleaseDateData {

    public static IGDBGameRelease create(int platform, long date, String human) {
        return new AutoValue_IGDBGameRelease(platform, date, human);
    }

    public abstract int platform();

    public abstract long date();

    public abstract String human();

    /**
     * Returns the platform associated to the release of this game
     *
     * @return the platform
     */
    @Override
    public NetworkEntityIdRelation<IPlatformData> getPlatform() {
        return new NetworkEntityIdRelation<>(platform(), Optional.absent());
    }

    /**
     * Returns the release date
     *
     * @return the date
     */
    @Override
    public IReleaseDateData getDate() {
        return NetReleaseDate.create(date(), human());
    }
}
