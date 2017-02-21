package com.piticlistudio.playednext.game.ui.search.view.adapter;

import android.view.View;

import com.airbnb.epoxy.EpoxyAdapter;
import com.piticlistudio.playednext.game.model.entity.Game;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Adapter for view displaying the game results.
 * Created by jorge.garcia on 02/12/2016.
 */
public class GameSearchAdapter extends EpoxyAdapter {

    private final Picasso picasso;
    private GameSearchLoadMoreModel_ loadingModel;
    private List<Game> searchItems = new ArrayList<>();
    private IGameSearchAdapterListener listener;

    private final View.OnClickListener onGameClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onGameClicked(null, v);
            }
        }
    };

    @Inject
    GameSearchAdapter(Picasso picasso) {
        this.picasso = picasso;
        enableDiffing();
        loadingModel = new GameSearchLoadMoreModel_();
    }

    public void setListener(IGameSearchAdapterListener listener) {
        this.listener = listener;
    }

    /**
     * Sets the data
     * Any current data will be replaced
     *
     * @param data the data to set.
     */
    public void setData(List<Game> data) {
        if (!searchItems.isEmpty()) {
            removeAllAfterModel(models.get(0));
            removeModel(models.get(0));
        }
        List<GameSearchModel> dataModels = new ArrayList<>();
        for (Game game : data) {
            GameSearchModel model = new GameSearchModel_()
                    .imageloader(picasso)
                    .imageURL(game.getThumbCoverUrl())
                    .title(game.title())
                    .clickListener(new GameClickListener(game));
            dataModels.add(model);
        }
        addModels(dataModels);
        searchItems = data;
    }

    /**
     * Adds data to the adapter, keeping the old data (if any)
     *
     * @param data the data to add.
     */
    public void addData(List<Game> data) {
        List<GameSearchModel> dataModels = new ArrayList<>();
        for (Game game : data) {
            GameSearchModel model = new GameSearchModel_()
                    .imageloader(picasso)
                    .imageURL(game.getThumbCoverUrl())
                    .title(game.title())
                    .clickListener(new GameClickListener(game));
            dataModels.add(model);
        }
        addModels(dataModels);
        searchItems.addAll(data);
    }

    public void setHasAdditionalData(boolean additional) {
        if (additional) {
            addModel(loadingModel);
        } else {
            removeModel(loadingModel);
        }
    }

    public interface IGameSearchAdapterListener {

        void onGameClicked(Game clickedGame, View v);
    }

    private class GameClickListener implements View.OnClickListener {

        Game game;

        public GameClickListener(Game game) {
            this.game = game;
        }

        @Override
        public void onClick(View view) {
            if (listener != null) {
                listener.onGameClicked(game, view);
            }
        }
    }
}
