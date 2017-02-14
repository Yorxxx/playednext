package com.piticlistudio.playednext;

import android.app.Application;
import android.util.Log;

import com.piticlistudio.playednext.collection.CollectionModule;
import com.piticlistudio.playednext.company.model.CompanyModule;
import com.piticlistudio.playednext.di.module.NetModule;
import com.piticlistudio.playednext.game.GameComponent;
import com.piticlistudio.playednext.game.GameModule;
import com.piticlistudio.playednext.game.model.DaggerGamedataComponent;
import com.piticlistudio.playednext.game.model.GamedataComponent;
import com.piticlistudio.playednext.game.model.GamedataModule;
import com.piticlistudio.playednext.game.model.entity.Game;
import com.piticlistudio.playednext.game.model.repository.GameRepository;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmConfiguration;


public class AndroidApplication extends Application {

    private static final String TAG = "AndroidApplication";
    public GamedataComponent gamedataComponent;
    public GameComponent gameComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        RealmConfiguration config = new RealmConfiguration.Builder(this)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);

        initializeComponents();
    }

    private void initializeComponents() {

        gamedataComponent = DaggerGamedataComponent.builder()
                .gamedataModule(new GamedataModule())
                .netModule(new NetModule())
                .build();

        gameComponent = gamedataComponent.plus(new GameModule(), new CollectionModule(), new CompanyModule());

        // TODO: 10/02/2017 remove
        final GameRepository repository = gameComponent.repository();
        repository.load(1000)
                .flatMap(repository::save)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Game>() {
                    @Override
                    public void accept(Game game) throws Exception {
                        Log.d(TAG, "accept() returned: " + game);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, "accept: " + throwable.getLocalizedMessage());
                    }
                });
    }
}
