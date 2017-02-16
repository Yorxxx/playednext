package com.piticlistudio.playednext.game.ui.detail.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.piticlistudio.playednext.AndroidApplication;
import com.piticlistudio.playednext.game.GameComponent;
import com.piticlistudio.playednext.game.model.entity.Game;
import com.piticlistudio.playednext.game.ui.detail.GameDetailContract;
import com.piticlistudio.playednext.game.ui.detail.presenter.GameDetailPresenter;

/**
 * Activity that displays a detailed game
 * Created by jorge.garcia on 16/02/2017.
 */

public class GameDetailActivity extends AppCompatActivity implements GameDetailContract.View {

    private static final String TAG = "GameDetailActivity";

    private GameDetailPresenter presenter;

    private GameComponent getGameComponent() {
        return ((AndroidApplication) getApplication()).gameComponent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = getGameComponent().detailPresenter();
        presenter.attachView(this);
        loadData(50);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null)
            presenter.detachView(false);
    }

    /**
     * Shows the loading status.
     */
    @Override
    public void showLoading() {
        Log.d(TAG, "showLoading() called");
    }

    /**
     * Shows the main content of the view
     */
    @Override
    public void showContent() {
        Log.d(TAG, "showContent() called");
    }

    /**
     * Sets the data to show.
     *
     * @param data the data to show
     */
    @Override
    public void setData(Game data) {
        Log.d(TAG, "setData() called with: data = [" + data + "]");
    }

    /**
     * Shows an error
     *
     * @param error the error to show.
     */
    @Override
    public void showError(Throwable error) {
        Log.d(TAG, "showError() called with: error = [" + error + "]");
    }

    /**
     * Loads the data for the specified identifier
     *
     * @param gameId the id of the game to load.
     */
    @Override
    public void loadData(int gameId) {
        presenter.loadData(gameId);
    }
}
