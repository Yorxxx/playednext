package com.piticlistudio.playednext.gamerelation.model.repository.datasource;

import com.piticlistudio.playednext.gamerelation.model.entity.datasource.IGameRelationDatasource;
import com.piticlistudio.playednext.gamerelation.model.entity.datasource.RealmGameRelation;
import com.piticlistudio.playednext.mvp.model.repository.datasource.BaseRealmRepository;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * Repository for RealmGameRelation
 * Created by jorge.garcia on 27/02/2017.
 */

public class RealmGameRelationRepositoryImpl extends BaseRealmRepository<RealmGameRelation> implements
        IGameRelationRepositoryDatasource<IGameRelationDatasource> {

    // Maps for storing strong references to Realm classes while they are subscribed to.
    // This is needed if users create Observables without manually maintaining a reference to them.
    // In that case RealmObjects/RealmResults/RealmLists might be GC'ed too early.
    ThreadLocal<StrongReferenceCounter<RealmResults>> resultsRefs;

    public RealmGameRelationRepositoryImpl() {
        super(RealmGameRelation.class);
        resultsRefs = new ThreadLocal<StrongReferenceCounter<RealmResults>>() {
            @Override
            protected StrongReferenceCounter<RealmResults> initialValue() {
                return new StrongReferenceCounter<>();
            }
        };
    }

    /**
     * Loads the model with the specified id.
     *
     * @param id the id to load.
     * @return an Observable that emits the model loaded
     */
    @Override
    public Single<IGameRelationDatasource> load(int id) {
        return super.find(id)
                .map(data -> data);
    }

    /**
     * Saves the data
     *
     * @param data the data to save
     * @return an Observable that emits the saved data
     */
    @Override
    public Single<IGameRelationDatasource> save(IGameRelationDatasource data) {
        return super.store((RealmGameRelation) data)
                .map(result -> result);
    }

    /**
     * Loads all items
     *
     * @return an Observable that emits all items available
     */
    @Override
    public Observable<List<IGameRelationDatasource>> loadAll() {

        return Observable.defer(() -> Observable.create(new ObservableOnSubscribe<List<IGameRelationDatasource>>() {
            @Override
            public void subscribe(ObservableEmitter<List<IGameRelationDatasource>> e) throws Exception {
                final Realm observableRealm = Realm.getDefaultInstance();

                final RealmChangeListener<RealmResults<RealmGameRelation>> listener = element -> {
                    if (!e.isDisposed()) {
                        List<IGameRelationDatasource> datasources = new ArrayList<>();
                        for (RealmGameRelation realmGameRelation : element) {
                            datasources.add(observableRealm.copyFromRealm(realmGameRelation));
                        }
                        e.onNext(datasources);
                    }
                };

                RealmResults<RealmGameRelation> results = observableRealm.where(RealmGameRelation.class).findAllAsync();
                resultsRefs.get().acquireReference(results);
                results.addChangeListener(listener);

                e.setDisposable(new Disposable() {
                    @Override
                    public void dispose() {
                        results.removeChangeListener(listener);
                        observableRealm.close();
                        resultsRefs.get().releaseReference(results);
                    }

                    @Override
                    public boolean isDisposed() {
                        return false;
                    }
                });

                List<IGameRelationDatasource> datasources = new ArrayList<>();
                for (RealmGameRelation realmGameRelation : results) {
                    datasources.add(observableRealm.copyFromRealm(realmGameRelation));
                }
                e.onNext(datasources);
            }
        }));
    }

    /*
 * Copyright 2015 Realm Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
    // Helper class for keeping track of strong references to objects.
    private static class StrongReferenceCounter<K> {

        private final Map<K, Integer> references = new IdentityHashMap<K, Integer>();

        public void acquireReference(K object) {
            Integer count = references.get(object);
            if (count == null) {
                references.put(object, 1);
            } else {
                references.put(object, count + 1);
            }
        }

        public void releaseReference(K object) {
            Integer count = references.get(object);
            if (count == null) {
                throw new IllegalStateException("Object does not have any references: " + object);
            } else if (count > 1) {
                references.put(object, count - 1);
            } else if (count == 1) {
                references.remove(object);
            } else {
                throw new IllegalStateException("Invalid reference count: " + count);
            }
        }
    }
}
