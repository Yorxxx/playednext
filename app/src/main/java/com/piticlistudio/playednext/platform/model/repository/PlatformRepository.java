package com.piticlistudio.playednext.platform.model.repository;

import com.piticlistudio.playednext.mvp.model.repository.BaseRepository;
import com.piticlistudio.playednext.platform.model.entity.Platform;
import com.piticlistudio.playednext.platform.model.entity.PlatformMapper;
import com.piticlistudio.playednext.platform.model.entity.datasource.IPlatformData;
import com.piticlistudio.playednext.platform.model.repository.datasource.IPlatformRepositoryDatasource;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Repository for Platform entities
 * Created by jorge.garcia on 14/02/2017.
 */

public class PlatformRepository extends BaseRepository<Platform, IPlatformData> implements IPlatformRepository {

    @Inject
    public PlatformRepository(@Named("db") IPlatformRepositoryDatasource dbImpl,
                              @Named("net") IPlatformRepositoryDatasource netImpl,
                              PlatformMapper mapper) {
        super(dbImpl, netImpl, mapper);
    }
}
