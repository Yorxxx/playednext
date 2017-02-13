package com.piticlistudio.playednext.game.model.entity.datasource;

import com.fernandocejas.arrow.optional.Optional;
import com.google.auto.value.AutoValue;
import com.piticlistudio.playednext.collection.model.entity.datasource.ICollectionData;
import com.piticlistudio.playednext.company.model.entity.datasource.ICompanyData;
import com.piticlistudio.playednext.image.model.entity.ImageData;
import com.piticlistudio.playednext.image.model.entity.datasource.IImageData;
import com.piticlistudio.playednext.image.model.entity.datasource.NetImageData;
import com.piticlistudio.playednext.mvp.model.entity.NetworkEntityIdRelation;
import com.piticlistudio.playednext.utils.AutoGson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Representation of a IGameDatasource provided by Net Module
 * Created by jorge.garcia on 10/02/2017.
 */
@AutoValue
@AutoGson
public abstract class NetGame implements IGameDatasource {

    public abstract int id();
    public abstract String name();
    public abstract String slug();
    public abstract String url();
    public abstract long created_at();
    public abstract long updated_at();
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
    public NetImageData cover;
    public List<NetImageData> screenshots;

    public static NetGame create(int id, String name, String slug, String url, long createdAt, long updatedAt) {
        return new AutoValue_NetGame(id, name, slug, url, createdAt, updatedAt);
    }

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
}
