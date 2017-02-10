package com.piticlistudio.playednext.game.model.entity.datasource;

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
}
