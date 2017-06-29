package com.piticlistudio.playednext;

import android.app.Application;
import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.VisibleForTesting;

import com.crashlytics.android.Crashlytics;
import com.piticlistudio.playednext.collection.CollectionModule;
import com.piticlistudio.playednext.company.model.CompanyModule;
import com.piticlistudio.playednext.di.component.AppComponent;
import com.piticlistudio.playednext.di.component.DaggerAppComponent;
import com.piticlistudio.playednext.di.module.AppModule;
import com.piticlistudio.playednext.game.DaggerGameComponent;
import com.piticlistudio.playednext.game.GameComponent;
import com.piticlistudio.playednext.game.GameModule;
import com.piticlistudio.playednext.game.ui.search.GameSearchComponent;
import com.piticlistudio.playednext.gamerelation.service.GameRelationSyncService;
import com.piticlistudio.playednext.gamerelation.ui.detail.GameRelationDetailComponent;
import com.piticlistudio.playednext.gamerelation.ui.list.GameRelationListComponent;
import com.piticlistudio.playednext.genre.GenreModule;
import com.piticlistudio.playednext.platform.PlatformModule;

import io.fabric.sdk.android.Fabric;
import java.io.IOException;

import io.realm.DynamicRealm;
import io.realm.FieldAttribute;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import io.realm.RealmSchema;


public class AndroidApplication extends Application {

    private static final String TAG = "AndroidApplication";
    public AppComponent appComponent;
    public GameComponent gameComponent;
    private GameSearchComponent searchComponent;
    //public GameRelationComponent relationComponent;
    private GameRelationDetailComponent relationDetailComponent;
    private GameRelationListComponent relationListComponent;

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
            if (oldVersion == 7) {
                schema.get("RealmGame")
                        .addField("syncedAt", long.class);
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());

        RealmConfiguration config = new RealmConfiguration.Builder(this)
                .schemaVersion(8)
                .migration(migration)
                .build();
        Realm.setDefaultConfiguration(config);

        initializeComponents();

        startService(new Intent(this, GameRelationSyncService.class));
    }

    private void initializeComponents() {


        gameComponent = DaggerGameComponent.builder()
                .platformModule(new PlatformModule())
                .genreModule(new GenreModule())
                .companyModule(new CompanyModule())
                .gameModule(new GameModule())
                .collectionModule(new CollectionModule())
                .build();

//        relationComponent = DaggerGameRelationComponent.builder()
//                .gameComponent(gameComponent)
//                .gameRelationModule(new GameRelationModule())
//                .relationIntervalModule(new RelationIntervalModule())
//                .build();

        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();

        try {
            appComponent.platformUtils().parse(getApplicationContext().getAssets().open("platformsui.json"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public AppComponent getApplicationComponent() {
        return this.appComponent;
    }

    public GameComponent getGameComponent() {
        return this.gameComponent;
    }

    public GameSearchComponent getSearchComponent() {
        return this.searchComponent;
    }

    public GameRelationDetailComponent getRelationDetailComponent() { return this.relationDetailComponent; }

    public GameRelationListComponent getRelationListComponent() {
        return relationListComponent;
    }

    @VisibleForTesting
    public void setGameComponent(GameComponent component) {
        this.gameComponent = component;
    }

    @VisibleForTesting
    public void setGameSearchComponent(GameSearchComponent component) {
        this.searchComponent = component;
    }

    @VisibleForTesting
    public void setRelationDetailComponent(GameRelationDetailComponent relationDetailComponent) {
        this.relationDetailComponent = relationDetailComponent;
    }

    @VisibleForTesting
    public void setRelationListComponent(GameRelationListComponent component) {
        this.relationListComponent = component;
    }
}
