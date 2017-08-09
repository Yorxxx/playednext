package com.piticlistudio.playednext.company.model;

import com.piticlistudio.playednext.APIKeys;
import com.piticlistudio.playednext.company.model.entity.CompanyMapper;
import com.piticlistudio.playednext.company.model.entity.datasource.IGDBCompany;
import com.piticlistudio.playednext.company.model.repository.CompanyRepository;
import com.piticlistudio.playednext.company.model.repository.ICompanyRepository;
import com.piticlistudio.playednext.company.model.repository.datasource.ICompanyRepositoryDataSource;
import com.piticlistudio.playednext.company.model.repository.datasource.IGDBCompanyRepositoryImpl;
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
    public ICompanyRepositoryDataSource provideNetRepository(NetService service) {
        return new IGDBCompanyRepositoryImpl(service);
    }

    @Provides
    @Named("db")
    public ICompanyRepositoryDataSource provideDBRepository() {
        return new RealmCompanyRepositoryImpl();
    }

    @Provides
    public ICompanyRepository provideRepository(@Named("db") ICompanyRepositoryDataSource local,
                                                @Named("net") ICompanyRepositoryDataSource remote,
                                                CompanyMapper mapper) {
        return new CompanyRepository(local, remote, mapper);
    }

    public interface NetService {

        @Headers({
                "Accept: application/json",
                "user-key: " + APIKeys.IGDB_KEY
        })
        @GET("/companies/{id}/")
        Observable<List<IGDBCompany>> load(@Path("id") int id, @Query("fields") String fields);
    }
}
