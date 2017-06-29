package com.piticlistudio.playednext.game.model.entity.datasource;

import android.app.AlarmManager;

import com.fernandocejas.arrow.optional.Optional;
import com.piticlistudio.playednext.collection.model.entity.datasource.ICollectionData;
import com.piticlistudio.playednext.collection.model.entity.datasource.RealmCollection;
import com.piticlistudio.playednext.company.model.entity.datasource.ICompanyData;
import com.piticlistudio.playednext.company.model.entity.datasource.RealmCompany;
import com.piticlistudio.playednext.gamerelease.model.entity.datasource.IGameReleaseDateData;
import com.piticlistudio.playednext.gamerelease.model.entity.datasource.RealmGameRelease;
import com.piticlistudio.playednext.genre.model.entity.datasource.IGenreData;
import com.piticlistudio.playednext.genre.model.entity.datasource.RealmGenre;
import com.piticlistudio.playednext.image.model.entity.datasource.IImageData;
import com.piticlistudio.playednext.image.model.entity.datasource.RealmImageData;
import com.piticlistudio.playednext.mvp.model.entity.NetworkEntityIdRelation;
import com.piticlistudio.playednext.platform.model.entity.datasource.IPlatformData;
import com.piticlistudio.playednext.platform.model.entity.datasource.RealmPlatform;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Representation of a IGameDataSource on Realm
 *
 * @see IGameDatasource
 * Created by jorge.garcia on 10/02/2017.
 */

public class RealmGame extends RealmObject implements IGameDatasource {

    private RealmCollection collection;
    @PrimaryKey
    private int id;
    @Required
    private String name;
    private String summary;
    private String storyline;
    private RealmImageData cover;
    private RealmList<RealmImageData> screenshots;
    private RealmList<RealmCompany> developers = new RealmList<>();
    private RealmList<RealmCompany> publishers = new RealmList<>();
    private RealmList<RealmGenre> genres = new RealmList<>();
    private RealmList<RealmGameRelease> releases = new RealmList<>();
    private RealmList<RealmPlatform> platforms = new RealmList<>();
    private long syncedAt;

    public RealmGame() {
    }

    /**
     * Returns the id
     *
     * @return the id
     */
    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the name
     *
     * @return the name to return.
     */
    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public void setSummary(String summary) {
        this.summary = summary;
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

    public void setStoryline(String storyline) {
        this.storyline = storyline;
    }

    /**
     * Returns the collection
     *
     * @return the collection
     */
    @Override
    public Optional<NetworkEntityIdRelation<ICollectionData>> getCollection() {
        if (collection == null)
            return Optional.absent();
        return Optional.of(new NetworkEntityIdRelation<>(collection.getId(), Optional.of(collection)));
    }

    public void setCollection(RealmCollection collection) {
        this.collection = collection;
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

    public void setCover(RealmImageData cover) {
        this.cover = cover;
    }

    /**
     * Returns all available screenshots.
     *
     * @return the list of screenshots
     */
    @Override
    public List<IImageData> getScreenshots() {
        if (this.screenshots == null)
            return new ArrayList<>();
        return new ArrayList<>(screenshots);
    }

    public void setScreenshots(RealmList<RealmImageData> screenshots) {
        this.screenshots = screenshots;
    }

    /**
     * Returns the list of developers.
     *
     * @return the developers
     */
    @Override
    public List<NetworkEntityIdRelation<ICompanyData>> getDevelopers() {
        List<NetworkEntityIdRelation<ICompanyData>> data = new ArrayList<>();
        if (this.developers != null) {
            for (RealmCompany developer : developers) {
                data.add(new NetworkEntityIdRelation<>(developer.getId(), Optional.of(developer)));
            }
        }
        return data;
    }

    public void setDevelopers(RealmList<RealmCompany> developers) {
        this.developers = developers;
    }

    /**
     * Returns the list of publishers
     *
     * @return the publishers
     */
    @Override
    public List<NetworkEntityIdRelation<ICompanyData>> getPublishers() {
        List<NetworkEntityIdRelation<ICompanyData>> data = new ArrayList<>();
        if (this.publishers != null) {
            for (RealmCompany publisher : publishers) {
                data.add(new NetworkEntityIdRelation<>(publisher.getId(), Optional.of(publisher)));
            }
        }
        return data;
    }

    public void setPublishers(RealmList<RealmCompany> publishers) {
        this.publishers = publishers;
    }

    /**
     * Returns the genres
     *
     * @return the genres
     */
    @Override
    public List<NetworkEntityIdRelation<IGenreData>> getGenres() {
        List<NetworkEntityIdRelation<IGenreData>> data = new ArrayList<>();
        if (this.genres != null) {
            for (RealmGenre genre : genres) {
                data.add(new NetworkEntityIdRelation<>(genre.getId(), Optional.of(genre)));
            }
        }
        return data;
    }

    public void setGenres(RealmList<RealmGenre> genres) {
        this.genres = genres;
    }

    /**
     * Returns the releases
     *
     * @return the releases
     */
    @Override
    public List<IGameReleaseDateData> getReleases() {
        if (releases == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(releases);
    }

    public void setReleases(RealmList<RealmGameRelease> releases) {
        this.releases = releases;
    }

    /**
     * Returns the platforms
     *
     * @return the platforms
     */
    @Override
    public List<NetworkEntityIdRelation<IPlatformData>> getPlatforms() {
        if (platforms == null) {
            return new ArrayList<>();
        }
        List<NetworkEntityIdRelation<IPlatformData>> data = new ArrayList<>();
        for (RealmPlatform platform : platforms) {
            data.add(new NetworkEntityIdRelation<>(platform.getId(), Optional.of(platform)));
        }
        return data;
    }

    public void setPlatforms(RealmList<RealmPlatform> platforms) {
        this.platforms = platforms;
    }

    public void setSyncedAt(long syncedAt) {
        this.syncedAt = syncedAt;
    }

    /**
     * Returns when this source has been synced with latest data
     *
     * @return the time in Unix timestamp
     */
    @Override
    public long syncedAt() {
        return syncedAt;
    }
}
