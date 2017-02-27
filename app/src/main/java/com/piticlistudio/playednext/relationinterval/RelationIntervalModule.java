package com.piticlistudio.playednext.relationinterval;


import com.piticlistudio.playednext.relationinterval.model.entity.RealmRelationIntervalMapper;
import com.piticlistudio.playednext.relationinterval.model.entity.datasource.RelationIntervalMapper;

import dagger.Module;
import dagger.Provides;

@Module
public class RelationIntervalModule {

    @Provides
    public RelationIntervalMapper dataMapper() {
        return new RelationIntervalMapper();
    }

    @Provides
    public RealmRelationIntervalMapper realmMapper() {
        return new RealmRelationIntervalMapper();
    }
}
