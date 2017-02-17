package com.piticlistudio.playednext.game.ui.detail.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class GameDetailFragment extends Fragment implements GameDetailContract.View {

    public static final String TAG = "GameDetail";

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

    private Unbinder unbinder;
    private GameDetailAdapter adapter;
    private Disposable screenshotViewerDisposable;
    private GameDetailPresenter presenter;
    private PublishSubject<View> doubleClickSubject = PublishSubject.create();

    private static Callbacks sDummyCallbacks = data -> {};
    private Callbacks mCallbacks = sDummyCallbacks;

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

        loadData(1942);
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
        mCallbacks.onDataLoaded(data);
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
    public void scrollDown(View v) {
        // Only scroll down when user simulates a "double tap"
        doubleClickSubject.onNext(v);
    }

    @OnClick({R.id.toolbar})
    public void scrollUp(View v) {
        barLayout.setExpanded(true);
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
