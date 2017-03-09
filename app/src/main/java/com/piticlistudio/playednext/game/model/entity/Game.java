package com.piticlistudio.playednext.game.model.entity;

import android.support.annotation.Nullable;

import com.fernandocejas.arrow.optional.Optional;
import com.google.auto.value.AutoValue;
import com.piticlistudio.playednext.boost.model.entity.IBoostable;
import com.piticlistudio.playednext.collection.model.entity.Collection;
import com.piticlistudio.playednext.company.model.entity.Company;
import com.piticlistudio.playednext.gamerelease.model.entity.GameRelease;
import com.piticlistudio.playednext.genre.model.entity.Genre;
import com.piticlistudio.playednext.image.model.entity.ImageData;
import com.piticlistudio.playednext.platform.model.entity.Platform;

import java.util.ArrayList;
import java.util.List;

/**
 * Domain entity representing a Game
 * Created by jorge.garcia on 10/02/2017.
 */
@AutoValue
public abstract class Game implements IBoostable {

    public String summary;
    public String storyline;
    public Optional<Collection> collection = Optional.absent();
    public Optional<ImageData> cover = Optional.absent();
    public List<ImageData> screenshots = new ArrayList<>();
    public List<Company> developers = new ArrayList<>();
    public List<Company> publishers = new ArrayList<>();
    public List<Genre> genres = new ArrayList<>();
    public List<GameRelease> releases = new ArrayList<>();
    public List<Platform> platforms = new ArrayList<>();

    public static Game create(int id, String title) {
        return new AutoValue_Game(id, title);
    }

    public abstract int id();

    public abstract String title();

    /**
     * Returns the thumb cover url.
     *
     * @return the url or null
     */
    @Nullable
    public String getThumbCoverUrl() {
        if (cover != null && cover.isPresent())
            return cover.get().getThumbUrl();
        return null;
    }

    /**
     * Returns if the current entity has the boost enabled
     *
     * @return true if is enabled. False otherwise
     */
    @Override
    public boolean isBoostEnabled() {
        return true;
    }

    /**
     * Returns the last release
     *
     * @return the last release
     */
    @Override
    public long getLastRelease() {
        long max = 0;
        for (GameRelease release : releases) {
            if (release.releaseDate().date() > max)
                max = release.releaseDate().date();
        }
        return max;
    }

    /**
     * Returns the first release
     *
     * @return the first release
     */
    @Override
    public long getFirstRelease() {
        long min = Long.MAX_VALUE;
        for (GameRelease release : releases) {
            if (release.releaseDate().date() < min)
                min = release.releaseDate().date();
        }
        return min;
    }

    /**
     * Returns since when is the item being on the todo list
     *
     * @return the timestamp
     */
    @Override
    public long getWaitingStartedAt() {
        return 0;
    }
}
