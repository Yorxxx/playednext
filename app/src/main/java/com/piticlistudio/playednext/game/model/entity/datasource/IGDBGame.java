package com.piticlistudio.playednext.game.model.entity.datasource;

import com.fernandocejas.arrow.optional.Optional;
import com.google.auto.value.AutoValue;
import com.piticlistudio.playednext.collection.model.entity.datasource.ICollectionData;
import com.piticlistudio.playednext.company.model.entity.datasource.ICompanyData;
import com.piticlistudio.playednext.gamerelease.model.entity.datasource.IGDBGameRelease;
import com.piticlistudio.playednext.gamerelease.model.entity.datasource.IGameReleaseDateData;
import com.piticlistudio.playednext.genre.model.entity.datasource.IGenreData;
import com.piticlistudio.playednext.image.model.entity.datasource.IGDBImageData;
import com.piticlistudio.playednext.image.model.entity.datasource.IImageData;
import com.piticlistudio.playednext.mvp.model.entity.NetworkEntityIdRelation;
import com.piticlistudio.playednext.platform.model.entity.datasource.IPlatformData;
import com.piticlistudio.playednext.utils.AutoGson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Representation of a IGameDatasource provided by IGDB
 * Created by jorge.garcia on 10/02/2017.
 */
@AutoValue
@AutoGson
public abstract class IGDBGame implements IGameDatasource {

    public String summary;
    public String storyline;
    public int collection;
    public int franchise = -1;
    public int hypes = -1;
    public double rating = -1;
    public double popularity = -1;
    public double aggregated_rating = -1;
    public int getRating_count = -1;
    public int rating_count;
    public int game = -1;
    public List<Integer> developers = new ArrayList<>();
    public List<Integer> publishers = new ArrayList<>();
    public List<Integer> game_engines = new ArrayList<>();
    public int category;
    public List<Integer> player_perspectives = new ArrayList<>();
    public List<Integer> game_modes = new ArrayList<>();
    public List<Integer> keywords = new ArrayList<>();
    public List<Integer> themes = new ArrayList<>();
    public List<Integer> genres;
    public long first_release_date;
    public IGDBImageData cover;
    public List<IGDBImageData> screenshots;
    public List<IGDBGameRelease> release_dates = new ArrayList<>();

    public static IGDBGame create(int id, String name, String slug, String url, long createdAt, long updatedAt) {
        return new AutoValue_IGDBGame(id, name, slug, url, createdAt, updatedAt);
    }

    public abstract int id();

    public abstract String name();

    public abstract String slug();

    public abstract String url();

    public abstract long created_at();

    public abstract long updated_at();

    /**
     * Returns the id
     *
     * @return the id
     */
    @Override
    public int getId() {
        return id();
    }

    /**
     * Returns the name
     *
     * @return the name to return.
     */
    @Override
    public String getName() {
        return name();
    }

    /**
     * Returns the summary
     *
     * @return the summary
     */
    @Override
    public String getSummary() {
        return summary;
    }

    /**
     * Returns the storyline
     *
     * @return the storyline
     */
    @Override
    public String getStoryline() {
        return storyline;
    }

    /**
     * Returns the collection
     *
     * @return the collection
     */
    @Override
    public Optional<NetworkEntityIdRelation<ICollectionData>> getCollection() {
        if (collection > 0)
            return Optional.of(new NetworkEntityIdRelation<>(collection, Optional.absent()));
        return Optional.absent();
    }

    /**
     * Returns the cover
     *
     * @return the cover
     */
    @Override
    public Optional<IImageData> getCover() {
        return Optional.fromNullable(cover);
    }

    /**
     * Returns all available screenshots.
     *
     * @return the list of screenshots
     */
    @Override
    public List<IImageData> getScreenshots() {
        if (screenshots == null)
            return new ArrayList<>();
        return new ArrayList<>(screenshots);
    }

    /**
     * Returns the list of developers.
     *
     * @return the developers
     */
    @Override
    public List<NetworkEntityIdRelation<ICompanyData>> getDevelopers() {
        List<NetworkEntityIdRelation<ICompanyData>> data = new ArrayList<>();
        if (developers != null) {
            for (Integer developer : developers) {
                data.add(new NetworkEntityIdRelation<>(developer, Optional.absent()));
            }
        }
        return data;
    }

    /**
     * Returns the list of publishers
     *
     * @return the publishers
     */
    @Override
    public List<NetworkEntityIdRelation<ICompanyData>> getPublishers() {
        List<NetworkEntityIdRelation<ICompanyData>> data = new ArrayList<>();
        if (publishers != null) {
            for (Integer publisher : publishers) {
                data.add(new NetworkEntityIdRelation<>(publisher, Optional.absent()));
            }
        }
        return data;
    }

    /**
     * Returns the genres
     *
     * @return the genres
     */
    @Override
    public List<NetworkEntityIdRelation<IGenreData>> getGenres() {
        List<NetworkEntityIdRelation<IGenreData>> data = new ArrayList<>();
        if (genres != null) {
            for (Integer genre : genres) {
                data.add(new NetworkEntityIdRelation<>(genre, Optional.absent()));
            }
        }
        return data;
    }

    /**
     * Returns the releases
     *
     * @return the releases
     */
    @Override
    public List<IGameReleaseDateData> getReleases() {
        if (release_dates == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(release_dates);
    }

    /**
     * Returns the platforms
     *
     * @return the platforms
     */
    @Override
    public List<NetworkEntityIdRelation<IPlatformData>> getPlatforms() {
        if (release_dates == null || release_dates.isEmpty()) {
            return new ArrayList<>();
        }
        List<NetworkEntityIdRelation<IPlatformData>> data = new ArrayList<>();
        HashSet<Integer> platformsSet = new HashSet<>();
        for (IGDBGameRelease release_date : release_dates) {
            if (!platformsSet.contains(release_date.platform()))
                data.add(release_date.getPlatform());
            platformsSet.add(release_date.platform());
        }
        return data;
    }
}
