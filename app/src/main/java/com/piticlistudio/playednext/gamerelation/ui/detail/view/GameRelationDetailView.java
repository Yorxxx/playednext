package com.piticlistudio.playednext.gamerelation.ui.detail.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.github.zagum.switchicon.SwitchIconView;
import com.piticlistudio.playednext.AndroidApplication;
import com.piticlistudio.playednext.R;
import com.piticlistudio.playednext.di.component.AppComponent;
import com.piticlistudio.playednext.game.GameComponent;
import com.piticlistudio.playednext.gamerelation.DaggerGameRelationComponent;
import com.piticlistudio.playednext.gamerelation.GameRelationComponent;
import com.piticlistudio.playednext.gamerelation.GameRelationModule;
import com.piticlistudio.playednext.gamerelation.model.entity.GameRelation;
import com.piticlistudio.playednext.gamerelation.ui.detail.GameRelationDetailContract;
import com.piticlistudio.playednext.gamerelation.ui.detail.presenter.GameRelationDetailPresenter;
import com.piticlistudio.playednext.mvp.ui.BaseLinearLayout;
import com.piticlistudio.playednext.relationinterval.model.entity.RelationInterval;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class GameRelationDetailView extends BaseLinearLayout implements GameRelationDetailContract.View {

    @BindView(R.id.switchesContainer)
    LinearLayout switchesContainer;
    @BindView(R.id.doneSwitchBtn)
    SwitchIconView doneBtn;
    @BindView(R.id.playingSwitchBtn)
    SwitchIconView playingBtn;
    @BindView(R.id.waitingSwitchBtn)
    SwitchIconView waitingBtn;
    private GameRelationDetailPresenter presenter;
    private GameRelation data;

    public GameRelationDetailView(Context context) {
        super(context);
        if (!isInEditMode())
            init();
    }

    public GameRelationDetailView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode())
            init();
    }

    public GameRelationDetailView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (!isInEditMode())
            init();
    }

    protected GameComponent getGameComponent() {
        Activity activity = getActivity();
        if (activity != null) {
            return ((AndroidApplication) activity.getApplication()).getGameComponent();
        }
        return null;
    }

    protected AppComponent getAppComponent() {
        Activity activity = getActivity();
        if (activity != null) {
            return ((AndroidApplication) activity.getApplication()).getApplicationComponent();
        }
        return null;
    }

    private void init() {
        GameRelationComponent component = DaggerGameRelationComponent.builder()
                .appComponent(getAppComponent())
                .gameComponent(getGameComponent())
                .gameRelationModule(new GameRelationModule())
                .build();

        presenter = component.detailPresenter();
        presenter.attachView(this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }

    /**
     * Sets the data
     *
     * @param data the data to set
     */
    @Override
    public void setData(GameRelation data) {
        this.data = data;
        if (data.getCurrent().isPresent()) {
            switch (data.getCurrent().get().type()) {
                case DONE:
                    doneBtn.setIconEnabled(true, false);
                    break;
                case PENDING:
                    waitingBtn.setIconEnabled(true, false);
                    break;
                case PLAYING:
                    playingBtn.setIconEnabled(true, false);
                    break;
            }
        } else {
            doneBtn.setIconEnabled(false);
            waitingBtn.setIconEnabled(false);
            waitingBtn.setIconEnabled(false);
        }
    }

    /**
     * Loads the data
     *
     * @param id the id of the relation to load.
     */
    @Override
    public void loadData(int id) {
        if (presenter != null) {
            presenter.loadData(id);
        }
    }

    /**
     * Shows a loading view
     */
    @Override
    public void showLoading() {
        switchesContainer.setEnabled(false);
    }

    /**
     * Shows the main content
     */
    @Override
    public void showContent() {
        switchesContainer.setEnabled(true);
    }

    /**
     * Shows the error
     *
     * @param error the error to show
     */
    @Override
    public void showError(Throwable error) {
        switchesContainer.setVisibility(View.GONE);
    }

    @OnClick(R.id.waitingSwitchBtn)
    public void setRelationAsWaiting() {
        presenter.save(this.data, RelationInterval.RelationType.PENDING, !waitingBtn.isIconEnabled());
        waitingBtn.switchState(!waitingBtn.isIconEnabled());
        playingBtn.setIconEnabled(false, true);
        doneBtn.setIconEnabled(false, true);
    }

    @OnClick(R.id.playingSwitchBtn)
    public void setRelationAsPlaying() {
        presenter.save(this.data, RelationInterval.RelationType.PLAYING, !playingBtn.isIconEnabled());
        playingBtn.switchState(!playingBtn.isIconEnabled());
        doneBtn.setIconEnabled(false, true);
        waitingBtn.setIconEnabled(false, true);
    }

    @OnClick(R.id.doneSwitchBtn)
    public void setRelationAsCompleted() {
        presenter.save(this.data, RelationInterval.RelationType.DONE, !doneBtn.isIconEnabled());
        doneBtn.switchState(!doneBtn.isIconEnabled());
        waitingBtn.setIconEnabled(false, true);
        playingBtn.setIconEnabled(false, true);
    }
}
