package com.piticlistudio.playednext.game.model.entity.datasource;

import com.fernandocejas.arrow.optional.Optional;
import com.piticlistudio.playednext.collection.model.entity.datasource.ICollectionData;
import com.piticlistudio.playednext.collection.model.entity.datasource.RealmCollection;
import com.piticlistudio.playednext.image.model.entity.datasource.IImageData;
import com.piticlistudio.playednext.image.model.entity.datasource.RealmImageData;
import com.piticlistudio.playednext.mvp.model.entity.NetworkEntityIdRelation;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Representation of a IGameDataSource on Realm
 * @see IGameDatasource
 * Created by jorge.garcia on 10/02/2017.
 */

public class RealmGame extends RealmObject implements IGameDatasource {

    @PrimaryKey
    private int id;
    @Required
    private String name;
    private String summary;
    private String storyline;
    public RealmCollection collection;
    private RealmImageData cover;
    private RealmList<RealmImageData> screenshots;

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

    /**
     * Returns the name
     *
     * @return the name to return.
     */
    @Override
    public String getName() {
        return name;
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

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setStoryline(String storyline) {
        this.storyline = storyline;
    }

    public void setCollection(RealmCollection collection) {
        this.collection = collection;
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

    public void setCover(RealmImageData cover) {
        this.cover = cover;
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

    public void setScreenshots(RealmList<RealmImageData> screenshots) {
        this.screenshots = screenshots;
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
}
