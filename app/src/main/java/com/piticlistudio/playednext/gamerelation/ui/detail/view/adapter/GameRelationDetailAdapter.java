package com.piticlistudio.playednext.gamerelation.ui.detail.view.adapter;

import android.content.Context;
import android.content.res.Resources;

import com.airbnb.epoxy.EpoxyAdapter;
import com.piticlistudio.playednext.R;
import com.piticlistudio.playednext.company.model.entity.Company;
import com.piticlistudio.playednext.game.model.entity.Game;
import com.piticlistudio.playednext.gamerelation.model.entity.GameRelation;
import com.piticlistudio.playednext.gamerelation.ui.detail.view.adapter.viewmodel.GameDetailDescriptionModel;
import com.piticlistudio.playednext.gamerelation.ui.detail.view.adapter.viewmodel.GameDetailDescriptionModel_;
import com.piticlistudio.playednext.gamerelation.ui.detail.view.adapter.viewmodel.GameDetailInfoModel;
import com.piticlistudio.playednext.gamerelation.ui.detail.view.adapter.viewmodel.GameDetailInfoModel_;
import com.piticlistudio.playednext.gamerelation.ui.detail.view.adapter.viewmodel.GameRelationDetailIntervalInfoModel;
import com.piticlistudio.playednext.gamerelation.ui.detail.view.adapter.viewmodel.GameRelationDetailIntervalInfoModel_;
import com.piticlistudio.playednext.genre.model.entity.Genre;
import com.piticlistudio.playednext.relationinterval.model.entity.RelationInterval;
import com.piticlistudio.playednext.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Adapter for displaying a game detail
 * Created by jorge.garcia on 16/02/2017.
 */

public class GameRelationDetailAdapter extends EpoxyAdapter {

    private GameRelation data = null;
    private GameDetailInfoModel infoModel;
    private GameDetailDescriptionModel descriptionModel;
    private GameRelationDetailIntervalInfoModel pendingInfoModel;
    private GameRelationDetailIntervalInfoModel playingInfoModel;
    private GameRelationDetailIntervalInfoModel completedInfoModel;
    private final Context ctx;

    @Inject
    GameRelationDetailAdapter(Context ctx) {
        enableDiffing();
        this.ctx = ctx;
        infoModel = new GameDetailInfoModel_();
        descriptionModel = new GameDetailDescriptionModel_();
        pendingInfoModel = new GameRelationDetailIntervalInfoModel_(RelationInterval.RelationType.PENDING);
        playingInfoModel = new GameRelationDetailIntervalInfoModel_(RelationInterval.RelationType.PLAYING);
        completedInfoModel = new GameRelationDetailIntervalInfoModel_(RelationInterval.RelationType.DONE);
        addModels(completedInfoModel, playingInfoModel, pendingInfoModel);
        hideModels(completedInfoModel, playingInfoModel, pendingInfoModel);
    }

    /**
     * Sets the data
     *
     * @param data the data to set into the adapter
     */
    public void setData(GameRelation data) {

        bindDescriptionModel(data.game());
        bindInfoModel(data.game());
        bindIntervalModel(data);

        if (this.data != null)
            notifyModelsChanged();

        this.data = data;
    }

    /**
     * Binds the description viewmodel from the supplied data
     *
     * @param game the game to show its description
     */
    private void bindDescriptionModel(Game game) {
        String description = game.summary != null ? game.summary : game.storyline;
        ((GameDetailDescriptionModel_) descriptionModel).description(description);
        if (this.data == null)
            addModel(descriptionModel);
        descriptionModel.show(description != null && description.length() > 0);
    }

    /**
     * Binds the view model containing the information of the supplide game.
     *
     * @param game the game to bind its view.
     */
    private void bindInfoModel(Game game) {
        String saga = game.collection.isPresent() ? game.collection.get().name() : null;
        List<String> developers = new ArrayList<>();
        for (Company developer : game.developers) {
            developers.add(developer.name());
        }
        String developer = StringUtils.stringify(developers);

        List<String> publishers = new ArrayList<>();
        for (Company publisher : game.publishers) {
            publishers.add(publisher.name());
        }
        String publisher = StringUtils.stringify(publishers);

        List<String> genres = new ArrayList<>();
        for (Genre genre : game.genres) {
            genres.add(genre.name());
        }
        String genre = StringUtils.stringify(genres);

        ((GameDetailInfoModel_) infoModel)
                .developer(developer)
                .showDeveloper(developers.size() > 0)
                .publisher(publisher)
                .showPublisher(publishers.size() > 0)
                .genre(genre)
                .showGenre(genres.size() > 0)
                .saga(saga)
                .showSaga(saga != null);
        if (this.data == null)
            addModel(infoModel);
        infoModel.show(saga != null || developer != null || publisher != null || genre != null);
    }

    /**
     * Binds the relationtype viewmodels from the supplied data
     *
     * @param relation the data to bind its info.
     */
    private void bindIntervalModel(GameRelation relation) {
        if (relation.getStatuses().isEmpty()) {
            pendingInfoModel.hide();
            completedInfoModel.hide();
            playingInfoModel.hide();
        } else {
            for (RelationInterval.RelationType relationType : RelationInterval.RelationType.values()) {
                String message;
                Resources res = ctx.getResources();
                switch (relationType) {
                    case DONE:
                        int value = relation.getNumberOfTimesInStatus(relationType);
                        message = res.getQuantityString(R.plurals.game_detail_relationcompleted_count, value, value);
                        ((GameRelationDetailIntervalInfoModel_) completedInfoModel).description(message)
                                .icon(R.drawable.gamerelation_completed_status_accentcolor);
                        break;
                    case PLAYING:
                        int hours = relation.getTotalHoursWithStatus(relationType);
                        if (hours > 24) {
                            message = res.getQuantityString(R.plurals.game_detail_relationplaying_count_days, hours/24, hours/24);
                        }
                        else
                            message = res.getQuantityString(R.plurals.game_detail_relationplaying_count_hours, hours, hours);
                        ((GameRelationDetailIntervalInfoModel_) playingInfoModel).description(message)
                                .icon(R.drawable.gamerelation_playing_status);
                        break;
                    case PENDING:
                        int pending = relation.getTotalHoursWithStatus(relationType);
                        if (pending > 24) {
                            message = res.getQuantityString(R.plurals.game_detail_relationplaying_count_days, pending/24, pending/24);
                        }
                        else
                            message = res.getQuantityString(R.plurals.game_detail_relationwaiting_count_hours, pending, pending);
                        ((GameRelationDetailIntervalInfoModel_) pendingInfoModel).description(message)
                                .icon(R.drawable.gamerelation_waiting_status);
                        break;
                }
            }
            showModels(completedInfoModel, playingInfoModel, pendingInfoModel);
        }
    }
}
