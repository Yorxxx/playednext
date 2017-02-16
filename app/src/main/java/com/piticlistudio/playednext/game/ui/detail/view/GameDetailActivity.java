package com.piticlistudio.playednext.game.ui.detail.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;

import com.piticlistudio.playednext.AndroidApplication;
import com.piticlistudio.playednext.R;
import com.piticlistudio.playednext.di.component.AppComponent;
import com.piticlistudio.playednext.game.GameComponent;
import com.piticlistudio.playednext.game.model.entity.Game;
import com.piticlistudio.playednext.game.ui.detail.GameDetailContract;
import com.piticlistudio.playednext.game.ui.detail.presenter.GameDetailPresenter;
import com.piticlistudio.playednext.utils.UIUtils;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * Activity that displays a detailed game
 * Created by jorge.garcia on 16/02/2017.
 */

public class GameDetailActivity extends AppCompatActivity implements GameDetailContract.View {

    private static final String TAG = "GameDetailActivity";

    @BindView(R.id.backdrop)
    ImageView backdrop;
    @BindView(R.id.appbar)
    AppBarLayout barLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private Disposable screenshotViewerDisposable;
    private GameDetailPresenter presenter;

    private GameComponent getGameComponent() {
        return ((AndroidApplication) getApplication()).gameComponent;
    }

    private AppComponent getAppComponent() {
        return ((AndroidApplication) getApplication()).appComponent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_detail);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(null);
        }

        backdrop.getLayoutParams().height = UIUtils.getScreenHeight(getApplicationContext());

        presenter = getGameComponent().detailPresenter();
        presenter.attachView(this);
        loadData(1942);

        barLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> backdrop.invalidate());
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (screenshotViewerDisposable != null && !screenshotViewerDisposable.isDisposed()) {
            screenshotViewerDisposable.dispose();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null)
            presenter.detachView(false);
    }

    /**
     * Shows the loading status.
     */
    @Override
    public void showLoading() {
        Log.d(TAG, "showLoading() called");
    }

    /**
     * Shows the main content of the view
     */
    @Override
    public void showContent() {
        Log.d(TAG, "showContent() called");
    }

    /**
     * Sets the data to show.
     *
     * @param data the data to show
     */
    @Override
    public void setData(Game data) {
        toolbar.setTitle(data.title());

        if (screenshotViewerDisposable != null && !screenshotViewerDisposable.isDisposed()) {
            screenshotViewerDisposable.dispose();
        }

        if (data.screenshots.size() > 0) {
            screenshotViewerDisposable = Observable.interval(5, TimeUnit.SECONDS)
                    .take(data.screenshots.size())
                    .map(aLong -> data.screenshots.get(aLong.intValue()))
                    .repeat()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(imageData -> {
                        getAppComponent().picasso().load(imageData.getFullUrl())
                                .into(backdrop);
                    });
        }
    }

    /**
     * Shows an error
     *
     * @param error the error to show.
     */
    @Override
    public void showError(Throwable error) {
        Log.d(TAG, "showError() called with: error = [" + error + "]");
    }

    /**
     * Loads the data for the specified identifier
     *
     * @param gameId the id of the game to load.
     */
    @Override
    public void loadData(int gameId) {
        presenter.loadData(gameId);
    }

    @OnClick({R.id.appbar})
    public void scrollDown() {
        Log.d(TAG, "scrollDown() called");
        barLayout.setExpanded(false);
    }

    @OnClick({R.id.toolbar})
    public void scrollUp() {
        Log.d(TAG, "scrollUp() called");
        barLayout.setExpanded(true);
    }
}
