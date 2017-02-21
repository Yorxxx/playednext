package com.piticlistudio.playednext.platform.ui.grid.adapter;

import com.airbnb.epoxy.EpoxyAdapter;
import com.piticlistudio.playednext.platform.model.entity.Platform;
import com.piticlistudio.playednext.utils.UIUtils;

import java.util.List;

public class PlatformLabelGridAdapter extends EpoxyAdapter {

    public PlatformLabelGridAdapter() {
        enableDiffing();
    }

    public void setData(List<Platform> data) {

        if (models.isEmpty()) {
            for (Platform platform : data) {
                int color = platform.getColor();
                int textColor = UIUtils.getTextColorForBackground(color);
                addModel(new PlatformLabelViewModel_()
                        .text(platform.getAcronym())
                        .background(platform.getColor())
                        .textColor(textColor));
            }
        } else {
            notifyModelsChanged();
        }
    }
}
