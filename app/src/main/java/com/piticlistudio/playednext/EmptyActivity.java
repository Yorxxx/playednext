package com.piticlistudio.playednext;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.piticlistudio.playednext.game.ui.search.view.GameSearchFragment;

/**
 * Created by jorge.garcia on 10/02/2017.
 */

public class EmptyActivity extends AppCompatActivity {

    public GameSearchFragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        currentFragment = new GameSearchFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, currentFragment)
                .commit();
    }

    /**
     * Take care of popping the fragment back stack or finishing the activity
     * as appropriate.
     */
    @Override
    public void onBackPressed() {
        if (currentFragment != null)
            currentFragment.onBackPressed();
    }
}
