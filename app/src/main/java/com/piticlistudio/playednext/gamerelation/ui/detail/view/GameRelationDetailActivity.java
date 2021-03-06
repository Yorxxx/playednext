package com.piticlistudio.playednext.gamerelation.ui.detail.view;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.piticlistudio.playednext.game.model.entity.Game;
import com.piticlistudio.playednext.image.model.entity.ImageData;
import com.piticlistudio.playednext.image.ui.viewer.ImageViewerActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity that displays a detailed game relation
 * Created by jorge.garcia on 16/02/2017.
 */

public class GameRelationDetailActivity extends AppCompatActivity implements GameRelationDetailFragment.Callbacks {

    private final static String EXTRA_GAMEID = "gameId";
    private Game currentData = null;

    GameRelationDetailFragment currentFragment;

    /**
     * Initializes a new intent to launch this activity
     *
     * @param activity the activity in which to launch
     * @param game     the game to show
     * @return an Intent ready to be launched
     */
    public static Intent init(Activity activity, Game game) {
        Intent intent = new Intent(activity, GameRelationDetailActivity.class);
        intent.putExtra(EXTRA_GAMEID, game.id());
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        if (savedInstanceState == null) {
            if (getIntent().hasExtra(EXTRA_GAMEID)) {
                int gameId = getIntent().getIntExtra(EXTRA_GAMEID, 0);
                currentFragment = GameRelationDetailFragment.newInstance(gameId);
                getSupportFragmentManager().beginTransaction().replace(android.R.id.content, currentFragment).commit();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
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
