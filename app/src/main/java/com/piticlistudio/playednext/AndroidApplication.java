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
import com.piticlistudio.playednext.genre.GenreModule;
import com.piticlistudio.playednext.platform.PlatformModule;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.realm.DynamicRealm;
import io.realm.FieldAttribute;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import io.realm.RealmSchema;


public class AndroidApplication extends Application {

    private static final String TAG = "AndroidApplication";
    public GamedataComponent gamedataComponent;
    public GameComponent gameComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        RealmConfiguration config = new RealmConfiguration.Builder(this)
                .schemaVersion(4)
                .migration(migration)
                .build();
        Realm.setDefaultConfiguration(config);

        initializeComponents();
    }

    private void initializeComponents() {

        gamedataComponent = DaggerGamedataComponent.builder()
                .gamedataModule(new GamedataModule())
                .netModule(new NetModule())
                .build();

        gameComponent = gamedataComponent.plus(new GameModule(), new CollectionModule(), new CompanyModule(), new GenreModule(), new PlatformModule());

        // TODO: 10/02/2017 remove
        final GameRepository repository = gameComponent.repository();
        repository.load(1500)
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

    RealmMigration migration = new RealmMigration() {
        @Override
        public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
            // DynamicRealm exposes an editable schema
            Log.d(TAG, "migrate() called with: realm = [" + realm + "], oldVersion = [" + oldVersion + "], newVersion = [" + newVersion + "]");
            RealmSchema schema = realm.getSchema();
            if (oldVersion == 1) {
                schema.create("RealmGenre")
                        .addField("id", int.class, FieldAttribute.PRIMARY_KEY)
                        .addField("name", String.class, FieldAttribute.REQUIRED);

                schema.get("RealmGame")
                        .addRealmListField("genres", schema.get("RealmGenre"));
                oldVersion++;
            }
            if (oldVersion == 2) {
                schema.create("RealmPlatform")
                        .addField("id", int.class, FieldAttribute.PRIMARY_KEY)
                        .addField("name", String.class, FieldAttribute.REQUIRED);

                schema.create("RealmReleaseDate")
                        .addField("human", String.class, FieldAttribute.REQUIRED)
                        .addField("date", long.class);

                schema.create("RealmGameRelease")
                        .addRealmObjectField("platform", schema.get("RealmPlatform"))
                        .addRealmObjectField("release", schema.get("RealmReleaseDate"));

                schema.get("RealmGame")
                        .addRealmListField("releases", schema.get("RealmGameRelease"));

                oldVersion++;
            }
            if (oldVersion == 3) {
                schema.get("RealmGame")
                        .addRealmListField("platforms", schema.get("RealmPlatforms"));
            }
        }
    };
}
