package com.piticlistudio.playednext.platform.ui.grid.adapter;

import com.airbnb.epoxy.EpoxyAdapter;
import com.airbnb.epoxy.EpoxyModel;
import com.piticlistudio.playednext.platform.model.entity.Platform;

import java.util.List;

public class PlatformLabelGridAdapter extends EpoxyAdapter {

    public PlatformLabelGridAdapter() {
        enableDiffing();
    }

    public void setData(List<Platform> data) {

        if (models.isEmpty()) {
            for (Platform platform : data) {
                addModel(new PlatformLabelViewModel_().text(platform.name()));
            }
        }
        else {
            notifyModelsChanged();
        }
    }
}
