package com.piticlistudio.playednext.game.ui.detail.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.piticlistudio.playednext.AndroidApplication;
import com.piticlistudio.playednext.R;
import com.piticlistudio.playednext.di.component.AppComponent;
import com.piticlistudio.playednext.game.GameComponent;
import com.piticlistudio.playednext.game.model.entity.Game;
import com.piticlistudio.playednext.game.ui.detail.GameDetailContract;
import com.piticlistudio.playednext.game.ui.detail.presenter.GameDetailPresenter;
import com.piticlistudio.playednext.game.ui.detail.view.adapter.GameDetailAdapter;
import com.piticlistudio.playednext.platform.model.entity.Platform;
import com.piticlistudio.playednext.platform.ui.grid.adapter.PlatformLabelGridAdapter;
import com.piticlistudio.playednext.utils.UIUtils;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;

/**
 * Fragment that displays a detailed game
 * Created by jorge.garcia on 16/02/2017.
 */

public class GameDetailFragment extends Fragment implements GameDetailContract.View, PlatformLabelGridAdapter.Callbacks {

    public static final String TAG = "GameDetail";
    private final static String ARG_GAMEID = "gameId";
    private static Callbacks sDummyCallbacks = data -> {
    };
    @BindView(R.id.listview)
    RecyclerView listview;
    @BindView(R.id.backdrop)
    ImageView backdrop;
    @BindView(R.id.appbar)
    AppBarLayout barLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.backdropTitle)
    TextView title;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.platformslist)
    RecyclerView platformsList;
    @BindView(R.id.content)
    ViewGroup content;
    @BindView(R.id.loading)
    View loading;
    @BindView(R.id.error)
    View error;
    @BindView(R.id.errorMsg)
    TextView errorMsg;
    @BindView(R.id.retry)
    Button retryBtn;
    @BindView(R.id.retryLoading)
    View loadingRetry;

    private boolean isRetrying;
    private Unbinder unbinder;
    private GameDetailAdapter adapter;
    private PlatformLabelGridAdapter platformAdapter;
    private Disposable screenshotViewerDisposable;
    private GameDetailPresenter presenter;
    private PublishSubject<View> doubleClickSubject = PublishSubject.create();
    private Callbacks mCallbacks = sDummyCallbacks;
    private int requestedGameId = 0;

    public static GameDetailFragment newInstance(int gameId) {
        GameDetailFragment fragment = new GameDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_GAMEID, gameId);
        fragment.setArguments(args);
        return fragment;
    }

    private GameComponent getGameComponent() {
        return ((AndroidApplication) getActivity().getApplication()).gameComponent;
    }

    private AppComponent getAppComponent() {
        return ((AndroidApplication) getActivity().getApplication()).appComponent;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.game_detail, container, false);
        unbinder = ButterKnife.bind(this, v);
        return v;
    }

    /**
     * Called when a fragment is first attached to its context.
     * {@link #onCreate(Bundle)} will be called after this.
     *
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (!(getActivity() instanceof Callbacks)) {
            throw new ClassCastException("Activity must implement fragment's callbacks");
        }
        mCallbacks = (Callbacks) getActivity();
    }

    private int getSpanCount() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        return (int) (dpWidth / 100);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(null);
        }

        backdrop.getLayoutParams().height = UIUtils.getScreenHeight(getContext());

        presenter = getGameComponent().detailPresenter();
        presenter.attachView(this);

        adapter = getGameComponent().detailAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        listview.setLayoutManager(layoutManager);
        listview.setAdapter(adapter);

        int spanScount = getSpanCount();
        platformAdapter = new PlatformLabelGridAdapter();
        platformAdapter.setSpanCount(spanScount);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), spanScount);
        gridLayoutManager.setSpanSizeLookup(platformAdapter.getSpanSizeLookup());
        platformsList.setLayoutManager(gridLayoutManager);
        platformsList.setAdapter(platformAdapter);
        platformAdapter.setListener(this);

        // Determine if we should display our fake title or not
        barLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            backdrop.invalidate();
            boolean isCollapsed = verticalOffset == -1.0 * appBarLayout.getTotalScrollRange();
            if (isCollapsed) {
                collapsingToolbar.setTitle(toolbar.getTitle());
            } else
                collapsingToolbar.setTitle(" ");
        });

        doubleClickSubject
                .buffer(300, TimeUnit.MILLISECONDS)
                .map(views -> views.size() == 2)
                .filter(aBoolean -> aBoolean)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aBoolean -> {
                    barLayout.setExpanded(false);
                });

        if (getArguments().containsKey(ARG_GAMEID)) {
            requestedGameId = getArguments().getInt(ARG_GAMEID);
            loadData(requestedGameId);
        }
    }

    /**
     * Called when the fragment is no longer attached to its activity.  This
     * is called after {@link #onDestroy()}.
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = sDummyCallbacks;
        if (screenshotViewerDisposable != null)
            screenshotViewerDisposable.dispose();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     * Shows the loading status.
     */
    @Override
    public void showLoading() {
        if (error.getTranslationY() == 0 && !isRetrying) {
            this.error.animate().translationY(-5000).setInterpolator(new AnticipateInterpolator()).setDuration(300).start();
        }
        else {
            if (loading.getAlpha() != 1)
                loading.animate().alpha(1).setDuration(300).start();
        }

    }

    /**
     * Shows the main content of the view
     */
    @Override
    public void showContent() {
        isRetrying = false;
        if (loading.getAlpha() != 0)
            loading.animate().alpha(0).setDuration(300).start();
        if (error.getTranslationY() == 0) {
            this.error.animate().translationY(-5000).setInterpolator(new AnticipateInterpolator()).setDuration(800).start();
        }
    }

    /**
     * Sets the data to show.
     *
     * @param data the data to show
     */
    @Override
    public void setData(Game data) {
        toolbar.setTitle(data.title());
        title.setText(data.title());

        if (data.screenshots.size() > 0 && (screenshotViewerDisposable == null || screenshotViewerDisposable.isDisposed())) {
            screenshotViewerDisposable = Observable.interval(5, TimeUnit.SECONDS)
                    .take(data.screenshots.size())
                    .map(aLong -> data.screenshots.get(aLong.intValue()))
                    .repeat()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(imageData -> {
                        Log.d(TAG, "Setting screenshot image");
                        getAppComponent().picasso().load(imageData.getFullUrl()).into(backdrop);
                    });
        }
        adapter.setData(data);
        platformAdapter.setData(data.platforms);
        mCallbacks.onDataLoaded(data);
    }

    /**
     * Shows an error
     *
     * @param error the error to show.
     */
    @Override
    public void showError(Throwable error) {
        if (!isRetrying) {
            this.error.animate().translationY(0).setInterpolator(new OvershootInterpolator()).setDuration(300).start();
        }
        else {
            ObjectAnimator
                    .ofFloat(this.error, "translationX", 0, 25, -25, 25, -25,15, -15, 6, -6, 0)
                    .setDuration(300)
                    .start();
        }

        if (loading.getAlpha() != 0)
            loading.animate().alpha(0).setDuration(300).start();

        errorMsg.setText(error.getLocalizedMessage());
        retryBtn.setAlpha(1);
        loadingRetry.setAlpha(0);
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

    @OnClick({R.id.retry})
    public void retryRequest() {
        isRetrying = true;
        retryBtn.animate().alpha(0).setDuration(300).start();
        loadingRetry.animate().alpha(1).setDuration(300).start();
        loadData(requestedGameId);
    }

    @OnClick({R.id.appbar})
    public void scrollDown(View v) {
        // Only scroll down when user simulates a "double tap"
        doubleClickSubject.onNext(v);
    }

    @OnClick({R.id.toolbar})
    public void scrollUp(View v) {
        barLayout.setExpanded(true);
    }

    /**
     * Callback when a platform has been clicked
     *
     * @param data the data clicked
     * @param v    the view clicked
     */
    @Override
    public void onPlatformClicked(Platform data, View v) {
        Snackbar.make(content, data.name(), Snackbar.LENGTH_SHORT).show();
    }

    public interface Callbacks {

        /**
         * Callback whenever data has been loaded
         *
         * @param data the data loaded
         */
        void onDataLoaded(Game data);
    }
}
