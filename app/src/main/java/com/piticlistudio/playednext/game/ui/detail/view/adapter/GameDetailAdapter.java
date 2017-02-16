package com.piticlistudio.playednext.game.ui.detail.view.adapter;

import com.airbnb.epoxy.EpoxyAdapter;
import com.piticlistudio.playednext.company.model.entity.Company;
import com.piticlistudio.playednext.game.model.entity.Game;
import com.piticlistudio.playednext.game.ui.detail.view.adapter.viewmodel.GameDetailInfoModel;
import com.piticlistudio.playednext.game.ui.detail.view.adapter.viewmodel.GameDetailInfoModel_;
import com.piticlistudio.playednext.genre.model.entity.Genre;
import com.piticlistudio.playednext.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for displaying a game detail
 * Created by jorge.garcia on 16/02/2017.
 */

public class GameDetailAdapter extends EpoxyAdapter {

    private Game data = null;
    private GameDetailInfoModel infoModel;

    public GameDetailAdapter() {
        enableDiffing();
    }

    public void setData(Game data) {
        bindInfoModel(data);

        if (this.data != null)
            notifyModelsChanged();

        this.data = data;
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
}
