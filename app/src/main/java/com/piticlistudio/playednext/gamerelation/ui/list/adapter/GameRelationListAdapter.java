package com.piticlistudio.playednext.gamerelation.ui.list.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.airbnb.epoxy.EpoxyAdapter;
import com.piticlistudio.playednext.R;
import com.piticlistudio.playednext.gamerelation.ui.list.adapter.models.HeaderModel;
import com.piticlistudio.playednext.gamerelation.ui.list.adapter.models.HeaderModel_;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

/**
 * Adapter that displays a list of gamerelations
 * Created by jorge.garcia on 01/03/2017.
 */

public class GameRelationListAdapter extends EpoxyAdapter {

    private final Picasso picasso;
    private final Context ctx;
    private HeaderModel completedHeaderModel;
    private HeaderModel pendingHeaderModel;
    private HeaderModel currentHeaderModel;

    @Inject
    public GameRelationListAdapter(Picasso picasso, Context ctx) {
        this.picasso = picasso;
        this.ctx = ctx;

        completedHeaderModel = new HeaderModel_()
                .title(ctx.getString(R.string.gamerelation_list_header_title_done))
                .color(ContextCompat.getColor(ctx, R.color.gamerelation_completed_color))
                .icon(R.drawable.gamerelation_completed_status);
        currentHeaderModel = new HeaderModel_()
                .title(ctx.getString(R.string.gamerelation_list_header_title_playing))
                .color(ContextCompat.getColor(ctx, R.color.gamerelation_current_color))
                .icon(R.drawable.gamerelation_playing_status);
        pendingHeaderModel = new HeaderModel_()
                .title(ctx.getString(R.string.gamerelation_list_header_title_pending))
                .color(ContextCompat.getColor(ctx, R.color.gamerelation_pending_color))
                .icon(R.drawable.gamerelation_waiting_status);

        addModels(completedHeaderModel, currentHeaderModel, pendingHeaderModel);
    }
}
