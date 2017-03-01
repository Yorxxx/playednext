package com.piticlistudio.playednext;

import android.app.Application;
import android.support.annotation.VisibleForTesting;

import com.piticlistudio.playednext.collection.CollectionModule;
import com.piticlistudio.playednext.company.model.CompanyModule;
import com.piticlistudio.playednext.di.component.AppComponent;
import com.piticlistudio.playednext.di.component.DaggerAppComponent;
import com.piticlistudio.playednext.di.module.AppModule;
import com.piticlistudio.playednext.di.module.NetModule;
import com.piticlistudio.playednext.game.GameComponent;
import com.piticlistudio.playednext.game.GameModule;
import com.piticlistudio.playednext.game.model.DaggerGamedataComponent;
import com.piticlistudio.playednext.game.model.GamedataComponent;
import com.piticlistudio.playednext.game.model.GamedataModule;
import com.piticlistudio.playednext.genre.GenreModule;
import com.piticlistudio.playednext.platform.PlatformModule;

import io.realm.DynamicRealm;
import io.realm.FieldAttribute;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import io.realm.RealmSchema;


public class AndroidApplication extends Application {

    private static final String TAG = "AndroidApplication";
    public AppComponent appComponent;
    public GamedataComponent gamedataComponent;
    public GameComponent gameComponent;

    RealmMigration migration = new RealmMigration() {
        @Override
        public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
            // DynamicRealm exposes an editable schema
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
            if (oldVersion == 4) {
                schema.create("RealmGameRelation")
                        .addField("id", int.class, FieldAttribute.PRIMARY_KEY)
                        .addField("createdAt", long.class)
                        .addField("updatedAt", long.class)
                        .addRealmObjectField("game", schema.get("RealmGame"));
            }
            if (oldVersion == 5) {
                schema.create("RealmRelationInterval")
                        .addField("id", int.class, FieldAttribute.PRIMARY_KEY)
                        .addField("type", int.class)
                        .addField("startedAt", long.class)
                        .addField("endedAt", long.class);

                schema.get("RealmGameRelation")
                        .addRealmListField("status", schema.get("RealmRelationInterval"));
            }
            if (oldVersion == 6) {
                schema.get("RealmGameRelation")
                        .renameField("status", "statuses");
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();

        RealmConfiguration config = new RealmConfiguration.Builder(this)
                .schemaVersion(7)
                .migration(migration)
                .build();
        Realm.setDefaultConfiguration(config);

        initializeComponents();

//        RealmGameRelationRepositoryImpl repository = new RealmGameRelationRepositoryImpl();
//        repository.loadAll()
//                .subscribe(new Consumer<List<IGameRelationDatasource>>() {
//                    @Override
//                    public void accept(List<IGameRelationDatasource> iGameRelationDatasources) throws Exception {
//                        Log.d(TAG, "Load all triggered with " + iGameRelationDatasources.size() + " elements");
//                    }
//                });
    }

    private void initializeComponents() {

        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();

        gamedataComponent = DaggerGamedataComponent.builder()
                .gamedataModule(new GamedataModule())
                .netModule(new NetModule())
                .build();

        gameComponent = gamedataComponent.plus(new AppModule(this), new GameModule(), new CollectionModule(), new CompanyModule(), new
                GenreModule(), new PlatformModule());
    }

    @VisibleForTesting
    public void setGameComponent(GameComponent component) {
        this.gameComponent = component;
    }

    public AppComponent getApplicationComponent() {
        return this.appComponent;
    }

    public GameComponent getGameComponent() { return this.gameComponent; }
}
