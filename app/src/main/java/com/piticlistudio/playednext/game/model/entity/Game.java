package com.piticlistudio.playednext.game.model.entity;

import com.fernandocejas.arrow.optional.Optional;
import com.google.auto.value.AutoValue;
import com.piticlistudio.playednext.collection.model.entity.Collection;
import com.piticlistudio.playednext.image.model.entity.ImageData;

import java.util.ArrayList;
import java.util.List;

/**
 * Domain entity representing a Game
 * Created by jorge.garcia on 10/02/2017.
 */
@AutoValue
public abstract class Game {

    public String summary;
    public String storyline;
    public Optional<Collection> collection = Optional.absent();
    public Optional<ImageData> cover = Optional.absent();
    public List<ImageData> screenshots = new ArrayList<>();

    public static Game create(int id, String title) {
        return new AutoValue_Game(id, title);
    }

    public abstract int id();

    public abstract String title();
}
