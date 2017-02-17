package com.piticlistudio.playednext.game.ui.detail.view;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.piticlistudio.playednext.game.model.entity.Game;
import com.piticlistudio.playednext.image.model.entity.ImageData;
import com.piticlistudio.playednext.image.ui.viewer.ImageViewerFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity that displays a detailed game
 * Created by jorge.garcia on 16/02/2017.
 */

public class GameDetailActivity extends AppCompatActivity implements GameDetailFragment.Callbacks {

    private final static String GAMEDETAIL_TAG = "gamedetail";
    private final static String IMAGEVIEW_TAG = "imageviewer";
    private Game currentData = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setStatusBarColor(Color.TRANSPARENT);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(android.R.id.content, new GameDetailFragment()).commit();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            List<String> images = new ArrayList<>();
            if (currentData != null && currentData.screenshots.size() > 0) {
                for (ImageData screenshot : currentData.screenshots) {
                    images.add(screenshot.getFullUrl());
                }
                getSupportFragmentManager().beginTransaction()
                        .replace(android.R.id.content, ImageViewerFragment.newInstance(images), IMAGEVIEW_TAG)
                        .commit();
            }
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            getSupportFragmentManager().beginTransaction()
                    .replace(android.R.id.content, new GameDetailFragment(), GAMEDETAIL_TAG)
                    .commit();
        }
    }

    /**
     * Take care of popping the fragment back stack or finishing the activity
     * as appropriate.
     */
    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().findFragmentByTag(IMAGEVIEW_TAG) == null) {
            super.onBackPressed();
        }
    }

    /**
     * Callback whenever data has been loaded
     *
     * @param data the data loaded
     */
    @Override
    public void onDataLoaded(Game data) {
        this.currentData = data;
    }
}
