package com.piticlistudio.playednext.game.model.entity;

import com.fernandocejas.arrow.optional.Optional;
import com.piticlistudio.playednext.collection.model.entity.RealmCollectionMapper;
import com.piticlistudio.playednext.collection.model.entity.datasource.RealmCollection;
import com.piticlistudio.playednext.company.model.entity.Company;
import com.piticlistudio.playednext.company.model.entity.RealmCompanyMapper;
import com.piticlistudio.playednext.company.model.entity.datasource.RealmCompany;
import com.piticlistudio.playednext.game.model.entity.datasource.RealmGame;
import com.piticlistudio.playednext.image.model.entity.RealmImageDataMapper;
import com.piticlistudio.playednext.image.model.entity.datasource.RealmImageData;
import com.piticlistudio.playednext.mvp.model.entity.Mapper;

import javax.inject.Inject;

import io.realm.RealmList;

/**
 * Maps a Game entity into a RealGame entity
 * Created by jorge.garcia on 14/02/2017.
 */

public class RealmGameMapper implements Mapper<RealmGame, Game> {

    private final RealmImageDataMapper imageMapper;
    private final RealmCollectionMapper collectionMapper;
    private final RealmCompanyMapper companyMapper;

    @Inject
    public RealmGameMapper(RealmImageDataMapper imageMapper, RealmCollectionMapper collectionMapper, RealmCompanyMapper companyMapper) {
        this.imageMapper = imageMapper;
        this.collectionMapper = collectionMapper;
        this.companyMapper = companyMapper;
    }

    /**
     * Transforms the data into an Optional model.
     *
     * @param data the data to transform
     * @return an Optional model.
     */
    @Override
    public Optional<RealmGame> transform(Game data) {
        if (data == null)
            return Optional.absent();
        RealmGame result = new RealmGame();
        result.setId(data.id());
        result.setName(data.title());
        result.setStoryline(data.storyline);
        result.setSummary(data.summary);

        if (data.cover != null && data.cover.isPresent()) {
            Optional<RealmImageData> image = imageMapper.transform(data.cover.get());
            if (image.isPresent())
                result.setCover(image.get());
        }

        if (data.collection != null && data.collection.isPresent()) {
            Optional<RealmCollection> collection = collectionMapper.transform(data.collection.get());
            if (collection.isPresent())
                result.setCollection(collection.get());
        }

        RealmList<RealmCompany> developers = new RealmList<>();
        for (Company developer : data.developers) {
            Optional<RealmCompany> company = companyMapper.transform(developer);
            if (company.isPresent())
                developers.add(company.get());
        }
        result.setDevelopers(developers);

        RealmList<RealmCompany> publishers = new RealmList<>();
        for (Company publisher : data.publishers) {
            Optional<RealmCompany> company = companyMapper.transform(publisher);
            if (company.isPresent())
                publishers.add(company.get());
        }
        result.setPublishers(publishers);

        return Optional.of(result);
    }
}
