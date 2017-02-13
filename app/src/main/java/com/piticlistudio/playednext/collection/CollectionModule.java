package com.piticlistudio.playednext.collection;

import com.piticlistudio.playednext.collection.model.entity.CollectionMapper;
import com.piticlistudio.playednext.collection.model.entity.datasource.ICollectionData;
import com.piticlistudio.playednext.collection.model.entity.datasource.NetCollection;
import com.piticlistudio.playednext.collection.model.entity.datasource.RealmCollection;
import com.piticlistudio.playednext.collection.model.repository.CollectionRepository;
import com.piticlistudio.playednext.collection.model.repository.ICollectionRepository;
import com.piticlistudio.playednext.collection.model.repository.datasource.ICollectionRepositoryDatasource;
import com.piticlistudio.playednext.collection.model.repository.datasource.NetCollectionRepositoryImpl;
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

    public interface NetService {

        @Headers({
                "Accept: application/json",
                "X-Mashape-Key: XxTvUubZsDmshGVnDjpP4ZnVFfaLp1FLO7Vjsnzi8CSsAfuObi"
        })
        @GET("/collections/{id}/")
        Observable<List<NetCollection>> load(@Path("id") int id, @Query("fields") String fields);
    }

    @Provides
    public NetService service(Retrofit retrofit) {
        return retrofit.create(NetService.class);
    }

    @Provides
    @Named("net")
    public ICollectionRepositoryDatasource<ICollectionData> provideNetRepository(NetService service) {
        return new NetCollectionRepositoryImpl(service);
    }

    @Provides
    @Named("db")
    public ICollectionRepositoryDatasource<ICollectionData> provideDBRepository() {
        return new RealmCollectionRepositoryImpl();
    }

    @Provides
    public ICollectionRepository provideRepository(@Named("db") ICollectionRepositoryDatasource<ICollectionData> local,
                                                   @Named("net") ICollectionRepositoryDatasource<ICollectionData> remote,
                                                   CollectionMapper mapper) {
        return new CollectionRepository(local, remote, mapper);
    }
}
