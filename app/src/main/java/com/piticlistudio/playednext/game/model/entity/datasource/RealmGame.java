package com.piticlistudio.playednext.game.model.entity.datasource;

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
    public int id;
    @Required
    public String name;
    public String summary;
    public String storyline;

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
}
