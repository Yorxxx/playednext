package com.piticlistudio.playednext.game.model.entity;

import com.fernandocejas.arrow.optional.Optional;
import com.piticlistudio.playednext.collection.model.entity.Collection;
import com.piticlistudio.playednext.collection.model.entity.CollectionMapper;
import com.piticlistudio.playednext.company.model.entity.Company;
import com.piticlistudio.playednext.company.model.entity.CompanyMapper;
import com.piticlistudio.playednext.company.model.entity.datasource.ICompanyData;
import com.piticlistudio.playednext.game.model.entity.datasource.IGameDatasource;
import com.piticlistudio.playednext.image.model.entity.ImageData;
import com.piticlistudio.playednext.image.model.entity.ImageDataMapper;
import com.piticlistudio.playednext.image.model.entity.datasource.IImageData;
import com.piticlistudio.playednext.mvp.model.entity.Mapper;
import com.piticlistudio.playednext.mvp.model.entity.NetworkEntityIdRelation;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class GameMapper implements Mapper<Game, IGameDatasource> {

    private final CollectionMapper collectionMapper;
    private final ImageDataMapper imageMapper;
    private final CompanyMapper companyMapper;

    @Inject
    public GameMapper(CollectionMapper collectionMapper, ImageDataMapper imageMapper, CompanyMapper companyMapper) {
        this.collectionMapper = collectionMapper;
        this.imageMapper = imageMapper;
        this.companyMapper = companyMapper;
    }

    /**
     * Transforms the data into an Optional model.
     *
     * @param data the data to transform
     * @return an Optional model.
     */
    @Override
    public Optional<Game> transform(IGameDatasource data) {
        if (data == null || data.getName() == null)
            return Optional.absent();
        Game result = Game.create(data.getId(), data.getName());
        result.storyline = data.getStoryline();
        result.summary = data.getSummary();

        if (data.getCollection().isPresent() && data.getCollection().get().data.isPresent()) {
            result.collection = collectionMapper.transform(data.getCollection().get().getData());
        }
        if (data.getCover().isPresent()) {
            result.cover = imageMapper.transform(data.getCover().get());
        }
        for (IImageData iImageData : data.getScreenshots()) {
            Optional<ImageData> image = imageMapper.transform(iImageData);
            if (image.isPresent())
                result.screenshots.add(image.get());
        }
        List<Company> developers = new ArrayList<>();
        for (NetworkEntityIdRelation<ICompanyData> iCompanyDataNetworkEntityIdRelation : data.getDevelopers()) {
            if (iCompanyDataNetworkEntityIdRelation.data.isPresent()) {
                Optional<Company> company = companyMapper.transform(iCompanyDataNetworkEntityIdRelation.data.get());
                if (company.isPresent()) {
                    developers.add(company.get());
                }
            }
        }
        result.developers = developers;

        return Optional.of(result);
    }
}
