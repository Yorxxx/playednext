package com.piticlistudio.playednext.game.model.entity;

import com.google.auto.value.AutoValue;

/**
 * Domain entity representing a Game
 * Created by jorge.garcia on 10/02/2017.
 */
@AutoValue
public abstract class Game {

    public String summary;
    public String storyline;

    public static Game create(int id, String title) {
        return new AutoValue_Game(id, title);
    }

    public abstract int id();

    public abstract String title();
}
