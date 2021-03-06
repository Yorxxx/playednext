package com.piticlistudio.playednext.gamerelation.ui.detail.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.github.zagum.switchicon.SwitchIconView;
import com.piticlistudio.playednext.AndroidApplication;
import com.piticlistudio.playednext.R;
import com.piticlistudio.playednext.gamerelation.model.entity.GameRelation;
import com.piticlistudio.playednext.gamerelation.ui.detail.DaggerGameRelationDetailComponent;
import com.piticlistudio.playednext.gamerelation.ui.detail.GameRelationDetailComponent;
import com.piticlistudio.playednext.gamerelation.ui.detail.GameRelationDetailContract;
import com.piticlistudio.playednext.gamerelation.ui.detail.GameRelationDetailModule;
import com.piticlistudio.playednext.mvp.ui.BaseLinearLayout;
import com.piticlistudio.playednext.relationinterval.model.entity.RelationInterval;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class GameRelationDetailView extends BaseLinearLayout implements GameRelationDetailContract.View {

    private static final String TAG = "GameRelationDetailView";

    @BindView(R.id.switchesContainer)
    LinearLayout switchesContainer;
    @BindView(R.id.doneSwitchBtn)
    SwitchIconView doneBtn;
    @BindView(R.id.playingSwitchBtn)
    SwitchIconView playingBtn;
    @BindView(R.id.waitingSwitchBtn)
    SwitchIconView waitingBtn;
    @Inject
    public GameRelationDetailContract.Presenter presenter;
    private GameRelation data;
    private GameRelationDetailComponent component;

    private GameRelationDetailCallback listener;

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

    public void setListener(GameRelationDetailCallback listener) {
        this.listener = listener;
    }

    protected GameRelationDetailComponent getComponent() {
        Activity activity = getActivity();
        if (activity != null) {
            component = ((AndroidApplication) activity.getApplication()).getRelationDetailComponent();
        }
        if (component == null) {
            component = DaggerGameRelationDetailComponent.builder()
                    .appComponent(getApplicationComponent())
                    .gameRelationDetailModule(new GameRelationDetailModule())
                    .build();
        }
        return component;
    }

    private void init() {
        if (getComponent() != null) {
            getComponent().inject(this);
        }
        presenter.attachView(this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }

    public void onDestroy() {
        presenter.detachView(false);
        component = null;
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
            playingBtn.setIconEnabled(false);
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
        switchesContainer.setAlpha(0);
    }

    /**
     * Shows the main content
     */
    @Override
    public void showContent() {
        switchesContainer.animate().alpha(1).setDuration(300).start();
    }

    /**
     * Shows the error
     *
     * @param error the error to show
     */
    @Override
    public void showError(Throwable error) {
        Log.e(TAG, "Error: " + error.getLocalizedMessage());
        switchesContainer.setVisibility(View.GONE);
    }

    @OnClick(R.id.waitingSwitchBtn)
    public void setRelationAsWaiting() {
        boolean active = !waitingBtn.isIconEnabled();
        waitingBtn.switchState(true);
        playingBtn.setIconEnabled(false, true);
        doneBtn.setIconEnabled(false, true);
        if (listener != null) {
            listener.onRelationTypeChange(data, RelationInterval.RelationType.PENDING, active);
        }
        else {
            presenter.save(data, RelationInterval.RelationType.PENDING, active);
        }
    }

    @OnClick(R.id.playingSwitchBtn)
    public void setRelationAsPlaying() {
        boolean active = !playingBtn.isIconEnabled();
        playingBtn.switchState(true);
        doneBtn.setIconEnabled(false, true);
        waitingBtn.setIconEnabled(false, true);
        if (listener != null) {
            listener.onRelationTypeChange(data, RelationInterval.RelationType.PLAYING, active);
        }
        else {
            presenter.save(data, RelationInterval.RelationType.PENDING, active);
        }
    }

    @OnClick(R.id.doneSwitchBtn)
    public void setRelationAsCompleted() {
        boolean active = !doneBtn.isIconEnabled();
        doneBtn.switchState(true);
        waitingBtn.setIconEnabled(false, true);
        playingBtn.setIconEnabled(false, true);
        if (listener != null) {
            // TODO avoid callbacks. Our listener could be notified directly from database changes
            listener.onRelationTypeChange(data, RelationInterval.RelationType.DONE, active);
        }
        else {
            presenter.save(data, RelationInterval.RelationType.PENDING, active);
        }
    }

    public interface GameRelationDetailCallback {

        /**
         * Notifies that the relation has been requested to change.
         * @param data the data to change
         * @param newType the type to change
         * @param isActive boolean indicating if the type is active or not
         */
        void onRelationTypeChange(GameRelation data, RelationInterval.RelationType newType, boolean isActive);
    }
}
