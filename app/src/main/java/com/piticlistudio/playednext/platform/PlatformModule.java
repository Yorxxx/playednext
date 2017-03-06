package com.piticlistudio.playednext.platform;

import com.piticlistudio.playednext.APIKeys;
import com.piticlistudio.playednext.platform.model.entity.PlatformMapper;
import com.piticlistudio.playednext.platform.model.entity.datasource.IGDBPlatform;
import com.piticlistudio.playednext.platform.model.repository.IPlatformRepository;
import com.piticlistudio.playednext.platform.model.repository.PlatformRepository;
import com.piticlistudio.playednext.platform.model.repository.datasource.IGDBPlatformRepositoryImpl;
import com.piticlistudio.playednext.platform.model.repository.datasource.IPlatformRepositoryDatasource;
import com.piticlistudio.playednext.platform.model.repository.datasource.RealmPlatformRepositoryImpl;

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
public class PlatformModule {

    @Provides
    public NetService service(Retrofit retrofit) {
        return retrofit.create(NetService.class);
    }

    @Provides
    @Named("net")
    public IPlatformRepositoryDatasource provideNetRepository(NetService service) {
        return new IGDBPlatformRepositoryImpl(service);
    }

    @Provides
    @Named("db")
    public IPlatformRepositoryDatasource provideDBRepository() {
        return new RealmPlatformRepositoryImpl();
    }

    @Provides
    public IPlatformRepository provideRepository(@Named("db") IPlatformRepositoryDatasource local,
                                                 @Named("net") IPlatformRepositoryDatasource remote,
                                                 PlatformMapper mapper) {
        return new PlatformRepository(local, remote, mapper);
    }

    public interface NetService {

        @Headers({
                "Accept: application/json",
                "X-Mashape-Key: " + APIKeys.IGDB_KEY
        })
        @GET("/platforms/{id}/")
        Observable<List<IGDBPlatform>> load(@Path("id") int id, @Query("fields") String fields);
    }
}
