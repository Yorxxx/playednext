package com.piticlistudio.playednext;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.piticlistudio.playednext.game.ui.search.view.GameSearchFragment;

/**
 * Created by jorge.garcia on 10/02/2017.
 */

public class EmptyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, new GameSearchFragment())
                .commit();
    }
}
