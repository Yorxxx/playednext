package com.piticlistudio.playednext.genre.model.entity.datasource;

import com.piticlistudio.playednext.IGenreData;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Entity representing a IGenreData on Realm
 * Created by jorge.garcia on 14/02/2017.
 */
public class RealmGenre extends RealmObject implements IGenreData {

    @PrimaryKey
    private int id;

    @Required
    private String name;

    public RealmGenre() {
        // Empty
    }

    public RealmGenre(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
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
     * @return the name
     */
    @Override
    public String getName() {
        return name;
    }
}
