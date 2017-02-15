package com.piticlistudio.playednext.gamerelease.model.entity;

import com.google.auto.value.AutoValue;
import com.piticlistudio.playednext.platform.model.entity.Platform;
import com.piticlistudio.playednext.releasedate.model.entity.ReleaseDate;

/**
 * Entity that defines the release of a game
 * Created by jorge.garcia on 15/02/2017.
 */
@AutoValue
public abstract class GameRelease {

    public static GameRelease create(Platform platform, ReleaseDate releaseDate) {
        return new AutoValue_GameRelease(platform, releaseDate);
    }

    public abstract Platform platform();

    public abstract ReleaseDate releaseDate();
}
