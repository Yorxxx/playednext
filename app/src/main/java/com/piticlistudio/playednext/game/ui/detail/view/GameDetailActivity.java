package com.piticlistudio.playednext.game.ui.detail.view;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.piticlistudio.playednext.game.model.entity.Game;
import com.piticlistudio.playednext.image.model.entity.ImageData;
import com.piticlistudio.playednext.image.ui.viewer.ImageViewerActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity that displays a detailed game
 * Created by jorge.garcia on 16/02/2017.
 */

public class GameDetailActivity extends AppCompatActivity implements GameDetailFragment.Callbacks {

    private Game currentData = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
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
                startActivity(ImageViewerActivity.init(this, images));
            }
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
