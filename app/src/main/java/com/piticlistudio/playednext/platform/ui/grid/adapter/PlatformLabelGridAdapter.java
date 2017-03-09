package com.piticlistudio.playednext.platform.ui.grid.adapter;

import android.view.View;

import com.airbnb.epoxy.EpoxyAdapter;
import com.piticlistudio.playednext.platform.model.entity.Platform;
import com.piticlistudio.playednext.platform.ui.PlatformUIUtils;
import com.piticlistudio.playednext.utils.UIUtils;

import java.util.List;

public class PlatformLabelGridAdapter extends EpoxyAdapter {

    private Callbacks listener;
    private final PlatformUIUtils utils;

    public PlatformLabelGridAdapter(PlatformUIUtils uiUtils) {
        this.utils = uiUtils;
        enableDiffing();
    }

    public void setListener(Callbacks listener) {
        this.listener = listener;
    }

    public void setData(List<Platform> data) {

        if (models.isEmpty()) {
            for (Platform platform : data) {
                int color = platform.getColor();
                int textColor = UIUtils.getTextColorForBackground(color);
                addModel(new PlatformLabelViewModel_()
                        .text(utils.getAcronym(platform.name()))
                        .background(utils.getColor(platform.name()))
                        .textColor(textColor)
                        .clickListener(view -> {
                            if (listener != null)
                                listener.onPlatformClicked(platform, view);
                        }));
            }
        } else {
            notifyModelsChanged();
        }
    }

    public interface Callbacks {

        /**
         * Callback when a platform has been clicked
         *
         * @param data the data clicked
         * @param v    the view clicked
         */
        void onPlatformClicked(Platform data, View v);
    }
}
