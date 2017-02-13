package com.piticlistudio.playednext.company.model;

import com.piticlistudio.playednext.company.model.entity.datasource.NetCompany;
import com.piticlistudio.playednext.company.model.entity.datasource.RealmCompany;
import com.piticlistudio.playednext.company.model.repository.datasource.ICompanyRepositoryDataSource;
import com.piticlistudio.playednext.company.model.repository.datasource.NetCompanyRepositoryImpl;
import com.piticlistudio.playednext.company.model.repository.datasource.RealmCompanyRepositoryImpl;

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
 * Module for companies
 * Created by jorge.garcia on 13/02/2017.
 */
@Module
public class CompanyModule {

    @Provides
    public NetService service(Retrofit retrofit) {
        return retrofit.create(NetService.class);
    }

    @Provides
    @Named("net")
    public ICompanyRepositoryDataSource<NetCompany> provideNetRepository(NetService service) {
        return new NetCompanyRepositoryImpl(service);
    }

    @Provides
    @Named("db")
    public ICompanyRepositoryDataSource<RealmCompany> provideDBRepository() {
        return new RealmCompanyRepositoryImpl();
    }

//    @Provides
//    public ICollectionRepository provideRepository(@Named("db") ICollectionRepositoryDatasource<RealmCollection> local,
//                                                   @Named("net") ICollectionRepositoryDatasource<NetCollection> remote,
//                                                   CollectionMapper mapper) {
//        return new CollectionRepository(local, remote, mapper);
//    }

    public interface NetService {

        @Headers({
                "Accept: application/json",
                "X-Mashape-Key: XxTvUubZsDmshGVnDjpP4ZnVFfaLp1FLO7Vjsnzi8CSsAfuObi"
        })
        @GET("/collections/{id}/")
        Observable<List<NetCompany>> load(@Path("id") int id, @Query("fields") String fields);
    }
}
