package com.piticlistudio.playednext.gamerelation.ui.detail.view;

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
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.piticlistudio.playednext.AndroidApplication;
import com.piticlistudio.playednext.R;
import com.piticlistudio.playednext.di.component.AppComponent;
import com.piticlistudio.playednext.game.model.entity.Game;
import com.piticlistudio.playednext.gamerelation.model.entity.GameRelation;
import com.piticlistudio.playednext.gamerelation.ui.detail.DaggerGameRelationDetailComponent;
import com.piticlistudio.playednext.gamerelation.ui.detail.GameRelationDetailComponent;
import com.piticlistudio.playednext.gamerelation.ui.detail.GameRelationDetailContract;
import com.piticlistudio.playednext.gamerelation.ui.detail.GameRelationDetailModule;
import com.piticlistudio.playednext.gamerelation.ui.detail.view.adapter.GameRelationDetailAdapter;
import com.piticlistudio.playednext.platform.model.entity.Platform;
import com.piticlistudio.playednext.platform.ui.grid.adapter.PlatformLabelGridAdapter;
import com.piticlistudio.playednext.relationinterval.model.entity.RelationInterval;
import com.piticlistudio.playednext.utils.UIUtils;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;

/**
 * Fragment that displays a detailed gamerelation (along with its game)
 * Created by jorge.garcia on 16/02/2017.
 */

public class GameRelationDetailFragment extends Fragment implements GameRelationDetailContract.View, PlatformLabelGridAdapter.Callbacks, GameRelationDetailView.GameRelationDetailCallback {

    public static final String TAG = "GameRelationDetail";
    private final static String ARG_GAMEID = "gameId";
    private static Callbacks sDummyCallbacks = data -> {
    };
    @Inject
    public GameRelationDetailContract.Presenter presenter;
    @Inject
    public GameRelationDetailAdapter adapter;
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
    @BindView(R.id.detail_content)
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
    @BindView(R.id.gamerelation_switch_box)
    GameRelationDetailView relationDetailLayout;
    private boolean isRetrying;
    private Unbinder unbinder;

    private PlatformLabelGridAdapter platformAdapter;
    public Disposable screenshotViewerDisposable;
    private PublishSubject<View> doubleClickSubject = PublishSubject.create();
    private Callbacks mCallbacks = sDummyCallbacks;
    private int requestedGameId = 0;
    public GameRelationDetailComponent component;

    public static GameRelationDetailFragment newInstance(int gameId) {
        GameRelationDetailFragment fragment = new GameRelationDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_GAMEID, gameId);
        fragment.setArguments(args);
        return fragment;
    }

    private AppComponent getAppComponent() {
        return ((AndroidApplication) getActivity().getApplication()).appComponent;
    }

    /**
     * Returns the component for this view
     *
     * @return the component
     */
    private GameRelationDetailComponent getComponent() {
        component = ((AndroidApplication)getActivity().getApplication()).getRelationDetailComponent();
        if (component == null) {
            component = DaggerGameRelationDetailComponent.builder()
                    .appComponent(getAppComponent())
                    .gameRelationDetailModule(new GameRelationDetailModule())
                    .build();
        }
        return component;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.game_detail, container, false);
        unbinder = ButterKnife.bind(this, v);
        return v;
    }

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

        getComponent().inject(this);
        presenter.attachView(this);

        adapter.setSpanCount(3);
        GridLayoutManager gm = new GridLayoutManager(getActivity(), 3);
        gm.setSpanSizeLookup(adapter.getSpanSizeLookup());
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        listview.setLayoutManager(gm);
        listview.setAdapter(adapter);

        int spanScount = getSpanCount();
        platformAdapter = new PlatformLabelGridAdapter(getAppComponent().platformUtils());
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

        relationDetailLayout.setListener(this);
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
        presenter.detachView(false);
        this.component = null;
        mCallbacks = sDummyCallbacks;
        if (screenshotViewerDisposable != null)
            screenshotViewerDisposable.dispose();
        screenshotViewerDisposable = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        relationDetailLayout.onDestroy();
        unbinder.unbind();
    }

    /**
     * Shows the loading status.
     */
    @Override
    public void showLoading() {
        if (!isRetrying) {
            this.error.animate().alpha(0).translationY(-5000).setInterpolator(new AnticipateInterpolator()).setDuration(300).start();
            loading.animate().alpha(1).setDuration(300).start();
        } else {
            loading.animate().alpha(0).setDuration(300).start();
            retryBtn.setAlpha(0);
            loadingRetry.setAlpha(1);
        }

    }

    /**
     * Shows the main content of the view
     */
    @Override
    public void showContent() {
        isRetrying = false;
        loading.animate().alpha(0).setDuration(300).start();
        this.error.animate().translationY(-5000).alpha(0).setInterpolator(new AnticipateInterpolator()).setDuration(800).start();
    }

    /**
     * Sets the data to show.
     *
     * @param data the data to show
     */
    @Override
    public void setData(GameRelation data) {
        toolbar.setTitle(data.game().title());
        title.setText(data.game().title());

        if (data.game().screenshots.size() > 0 && (screenshotViewerDisposable == null || screenshotViewerDisposable.isDisposed())) {
            screenshotViewerDisposable = Observable.interval(5, TimeUnit.SECONDS)
                    .take(data.game().screenshots.size())
                    .map(aLong -> data.game().screenshots.get(aLong.intValue()))
                    .repeat()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(imageData -> {
                        getAppComponent().picasso().load(imageData.getFullUrl()).into(backdrop);
                    });
        }
        adapter.setData(data);
        platformAdapter.setData(data.game().platforms);
        mCallbacks.onDataLoaded(data.game());
        relationDetailLayout.setData(data);
    }

    /**
     * Shows an error
     *
     * @param error the error to show.
     */
    @Override
    public void showError(Throwable error) {
       if (!isRetrying) {
            this.error.animate().translationY(0).alpha(1).setDuration(300).start();
        } else {
           // shake off animation
            ObjectAnimator
                    .ofFloat(this.error, "translationX", 0, 25, -25, 25, -25, 15, -15, 6, -6, 0)
                    .setDuration(300)
                    .start();
        }

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

    @Override
    public void onRelationTypeChange(GameRelation data, RelationInterval.RelationType newType, boolean isActive) {
        presenter.save(data, newType, isActive);
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
