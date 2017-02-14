package com.piticlistudio.playednext.game.model.entity.datasource;

import com.fernandocejas.arrow.optional.Optional;
import com.piticlistudio.playednext.collection.model.entity.datasource.ICollectionData;
import com.piticlistudio.playednext.company.model.entity.datasource.ICompanyData;
import com.piticlistudio.playednext.genre.model.entity.datasource.IGenreData;
import com.piticlistudio.playednext.image.model.entity.datasource.IImageData;
import com.piticlistudio.playednext.mvp.model.entity.NetworkEntityIdRelation;

import java.util.List;

/**
 * Definition methods for game entities from datasources
 * Since net definitions does not allow aggregated queries, some methods return an entity which defines his identifier and self as Optional
 * Created by jorge.garcia on 10/02/2017.
 */

public interface IGameDatasource {

    /**
     * Returns the id
     *
     * @return the id
     */
    int getId();

    /**
     * Returns the name
     *
     * @return the name to return.
     */
    String getName();

    /**
     * Returns the summary
     *
     * @return the summary
     */
    String getSummary();

    /**
     * Returns the storyline
     *
     * @return the storyline
     */
    String getStoryline();

    /**
     * Returns the collection
     *
     * @return the collection
     */
    Optional<NetworkEntityIdRelation<ICollectionData>> getCollection();

    /**
     * Returns the cover
     *
     * @return the cover
     */
    Optional<IImageData> getCover();

    /**
     * Returns all available screenshots.
     *
     * @return the list of screenshots
     */
    List<IImageData> getScreenshots();

    /**
     * Returns the list of developers.
     *
     * @return the developers
     */
    List<NetworkEntityIdRelation<ICompanyData>> getDevelopers();

    /**
     * Returns the list of publishers
     *
     * @return the publishers
     */
    List<NetworkEntityIdRelation<ICompanyData>> getPublishers();

    /**
     * Returns the genres
     *
     * @return the genres
     */
    List<NetworkEntityIdRelation<IGenreData>> getGenres();
}
