package com.piticlistudio.playednext.collection;

import com.piticlistudio.playednext.APIKeys;
import com.piticlistudio.playednext.collection.model.entity.CollectionMapper;
import com.piticlistudio.playednext.collection.model.entity.datasource.ICollectionData;
import com.piticlistudio.playednext.collection.model.entity.datasource.IGDBCollection;
import com.piticlistudio.playednext.collection.model.repository.CollectionRepository;
import com.piticlistudio.playednext.collection.model.repository.ICollectionRepository;
import com.piticlistudio.playednext.collection.model.repository.datasource.ICollectionRepositoryDatasource;
import com.piticlistudio.playednext.collection.model.repository.datasource.IGDBCollectionRepositoryImpl;
import com.piticlistudio.playednext.collection.model.repository.datasource.RealmCollectionRepositoryImpl;

import java.util.List;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Module for collections
 * Created by jorge.garcia on 10/02/2017.
 */
@Module
public class CollectionModule {

    public interface IGDBService {

        @Headers({
                "Accept: application/json",
                "user-key: " + APIKeys.IGDB_KEY
        })
        @GET("/collections/{id}/")
        Observable<List<IGDBCollection>> load(@Path("id") int id, @Query("fields") String fields);
    }

    @Provides
    public IGDBService service(Retrofit retrofit) {
        return retrofit.create(IGDBService.class);
    }

    @Provides
    @Named("net")
    public ICollectionRepositoryDatasource provideNetRepository(IGDBService service) {
        return new IGDBCollectionRepositoryImpl(service);
    }

    @Provides
    @Named("db")
    public ICollectionRepositoryDatasource provideDBRepository() {
        return new RealmCollectionRepositoryImpl();
    }

    @Provides
    public ICollectionRepository provideRepository(@Named("db") ICollectionRepositoryDatasource local,
                                                   @Named("net") ICollectionRepositoryDatasource remote,
                                                   CollectionMapper mapper) {
        return new CollectionRepository(local, remote, mapper);
    }
}
