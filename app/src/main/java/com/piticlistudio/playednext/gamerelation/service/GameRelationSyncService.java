package com.piticlistudio.playednext.gamerelation.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.piticlistudio.playednext.AndroidApplication;
import com.piticlistudio.playednext.di.component.AppComponent;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Service for syncing gamerelation data
 * Created by jorge on 20/6/17.
 */

public class GameRelationSyncService extends IntentService {

    private static final String TAG = "GameRelationSyncService";

    GameRelationSyncManager manager;

    public GameRelationSyncService() {
        super(TAG);
    }

    protected AppComponent getAppComponent() {
        return ((AndroidApplication) getApplication()).appComponent;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        GameRelationServiceComponent component = DaggerGameRelationServiceComponent.builder()
                .appComponent(getAppComponent())
                .build();
        component.inject(this);

        manager = component.manager();

        manager.sync()
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(game -> Log.d(TAG, "Succesfully synced game = [" + game + "]"),
                        throwable -> Log.e(TAG, "Failed syncing data: " + throwable.getLocalizedMessage()),
                        () -> Log.d(TAG, "Completed"));
    }
}
