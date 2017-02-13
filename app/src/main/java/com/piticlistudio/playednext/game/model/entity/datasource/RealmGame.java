package com.piticlistudio.playednext.game.model.entity.datasource;

import com.fernandocejas.arrow.optional.Optional;
import com.piticlistudio.playednext.collection.model.entity.datasource.ICollectionData;
import com.piticlistudio.playednext.collection.model.entity.datasource.RealmCollection;
import com.piticlistudio.playednext.company.model.entity.datasource.ICompanyData;
import com.piticlistudio.playednext.company.model.entity.datasource.RealmCompany;
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
 *
 * @see IGameDatasource
 * Created by jorge.garcia on 10/02/2017.
 */

public class RealmGame extends RealmObject implements IGameDatasource {

    public RealmCollection collection;
    @PrimaryKey
    private int id;
    @Required
    private String name;
    private String summary;
    private String storyline;
    private RealmImageData cover;
    private RealmList<RealmImageData> screenshots;
    private RealmList<RealmCompany> developers = new RealmList<>();

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

    public void setDevelopers(RealmList<RealmCompany> developers) {
        this.developers = developers;
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
}
