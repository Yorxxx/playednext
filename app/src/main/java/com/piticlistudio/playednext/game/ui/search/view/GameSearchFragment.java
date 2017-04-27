package com.piticlistudio.playednext.game.ui.search.view;

import android.animation.Animator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.piticlistudio.playednext.AndroidApplication;
import com.piticlistudio.playednext.R;
import com.piticlistudio.playednext.di.component.AppComponent;
import com.piticlistudio.playednext.game.GameComponent;
import com.piticlistudio.playednext.game.model.entity.Game;
import com.piticlistudio.playednext.game.ui.detail.view.GameDetailActivity;
import com.piticlistudio.playednext.game.ui.search.DaggerGameSearchComponent;
import com.piticlistudio.playednext.game.ui.search.GameSearchComponent;
import com.piticlistudio.playednext.game.ui.search.GameSearchContract;
import com.piticlistudio.playednext.game.ui.search.GameSearchModule;
import com.piticlistudio.playednext.game.ui.search.view.adapter.GameSearchAdapter;
import com.piticlistudio.playednext.gamerelation.ui.detail.view.GameRelationDetailActivity;
import com.piticlistudio.playednext.ui.recyclerview.SpacesItemDecoration;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Fragment that displays a searchview allowing to search games
 * Created by jorge.garcia on 21/02/2017.
 */

public class GameSearchFragment extends Fragment implements GameSearchContract.View, GameSearchAdapter.IGameSearchAdapterListener {

    private static final String TAG = "GameSearchFragment";
    // The minimum number of items to have below your current scroll position
    // before loading more
    private static final int LOADMORE_THRESHOLD = 3;
    // The max amount of items to load on every request
    private static final int MAX_LOAD_ITEMS = 15;
    @Inject
    public GameSearchAdapter adapter;
    @Inject
    public GameSearchContract.Presenter presenter;
    @BindView(R.id.initialstateview)
    LinearLayout initialstateview;
    @BindView(R.id.searchview)
    SearchView searchview;
    @BindView(R.id.progress)
    ProgressBar progress;
    @BindView(R.id.closeBtn)
    FloatingActionButton closeBtn;
    @BindView(R.id.searchlist)
    RecyclerView listview;
    @BindView(R.id.content)
    ViewGroup content;
    @BindView(R.id.gamesearch_error)
    ViewGroup errorLayout;
    @BindView(R.id.errorMsg)
    TextView errorMessage;
    @BindView(R.id.emptystateview)
    ViewGroup emptyStateView;
    int loadLimit = MAX_LOAD_ITEMS;
    private Unbinder unbinder;
    private IGameSearchFragmentListener listener;
    private boolean isLoadingMore = false;
    private boolean canLoadMore = false;
    private GameSearchComponent component;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.game_search_fragment, container, false);
        unbinder = ButterKnife.bind(this, v);
        return v;
    }


    private GameComponent getGameComponent() {
        return ((AndroidApplication) getActivity().getApplication()).gameComponent;
    }

    private AppComponent getAppComponent() {
        return ((AndroidApplication) getActivity().getApplication()).appComponent;
    }

    /**
     * Returns the component for this view
     *
     * @return the component
     */
    private GameSearchComponent getComponent() {
        component = ((AndroidApplication) getActivity().getApplication()).getSearchComponent();
        if (component == null) {
            component = DaggerGameSearchComponent.builder()
                    .appComponent(getAppComponent())
                    .gameComponent(getGameComponent())
                    .gameSearchModule(new GameSearchModule())
                    .build();
        }
        return component;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getComponent().inject(this);
        presenter.attachView(this);

        // Set up the adapter
        int spanScount = getSpanCount();
//        adapter = getGameComponent().searchAdapter();
        adapter.setSpanCount(spanScount);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), spanScount);
        gridLayoutManager.setSpanSizeLookup(adapter.getSpanSizeLookup());
        gridLayoutManager.setItemPrefetchEnabled(true);
        gridLayoutManager.setInitialPrefetchItemCount(spanScount * 4);


        listview.setLayoutManager(gridLayoutManager);
        listview.addItemDecoration(new SpacesItemDecoration((int) getResources().getDimension(R.dimen.game_search_adapter_spacing),
                getSpanCount()));
        listview.setAdapter(adapter);

        adapter.setListener(this);

        listview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            /**
             * Callback method to be invoked when the RecyclerView has been scrolled. This will be
             * called after the scroll has completed.
             * <p>
             * This callback will also be called if visible item range changes after a layout
             * calculation. In that case, dx and dy will be 0.
             *
             * @param recyclerView The RecyclerView which scrolled.
             * @param dx           The amount of horizontal scroll.
             * @param dy           The amount of vertical scroll.
             */
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int totalItemCount = gridLayoutManager.getItemCount();
                int lastVisibleItem = gridLayoutManager.findLastVisibleItemPosition();

                if (canLoadMore && !isLoadingMore && totalItemCount <= (lastVisibleItem + LOADMORE_THRESHOLD)) {
                    isLoadingMore = true;
                    search(searchview.getQuery().toString(), totalItemCount, loadLimit);
                }
            }
        });

        searchview.setIconifiedByDefault(false);
        // TODO RxBinding
        searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                isLoadingMore = false;
                search(query, 0, loadLimit);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                isLoadingMore = false;
                search(newText, 0, loadLimit);
                return true;
            }
        });
    }

    public void setListener(IGameSearchFragmentListener listener) {
        this.listener = listener;
    }

    public void onBackPressed() {
        closeSearch(this.closeBtn);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null)
            unbinder.unbind();
        unbinder = null;
        presenter.detachView(false);
        component = null;
    }


    private int getSpanCount() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels; // * displayMetrics.density;
        float rowWidth = getResources().getDimension(R.dimen.game_search_adapter_cell_width);
        float spacesWidth = getResources().getDimension(R.dimen.game_search_adapter_spacing);
        return (int) (dpWidth / (rowWidth + (2 * spacesWidth)));
    }

    @OnClick(R.id.closeBtn)
    public void closeSearch(View v) {
        closeBtn.animate()
                .scaleX(0)
                .scaleY(0)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .setDuration(250)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        if (listener != null) {
                            listener.onCloseSearchClicked(v);
                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                })
                .start();
    }

    /**
     * Shows the loading status.
     */
    @Override
    public void showLoading() {
        progress.setVisibility(View.VISIBLE);
    }

    /**
     * Shows the main content of the view
     */
    @Override
    public void showContent() {
        progress.setVisibility(View.GONE);
        initialstateview.animate().alpha(0).setDuration(300).start();
        errorLayout.animate().alpha(0).setDuration(300).start();
    }

    /**
     * Sets the data to show.
     *
     * @param data the data to show
     */
    @Override
    public void setData(List<Game> data) {
        adapter.setHasAdditionalData(false);
        if (isLoadingMore) {
            adapter.addData(data);
            isLoadingMore = false;
        } else {
            adapter.setData(data);
            if (data.isEmpty()) {
                emptyStateView.animate().alpha(1).setDuration(300).start();
            } else {
                if (emptyStateView.getAlpha() != 0)
                    emptyStateView.animate().alpha(0).setDuration(300).start();
            }
        }
        canLoadMore = data.size() == loadLimit;
        adapter.setHasAdditionalData(canLoadMore);
    }

    /**
     * Shows an error
     *
     * @param error the error to show.
     */
    @Override
    public void showError(Throwable error) {
        progress.setVisibility(View.GONE);
        if (isLoadingMore) {
            Snackbar errorBar = Snackbar.make(content, error.getLocalizedMessage(), Snackbar.LENGTH_LONG);
            errorBar.setAction(R.string.game_search_error_retry_button, view -> {
                adapter.setHasAdditionalData(true);
                search(searchview.getQuery().toString(), adapter.getItemCount() - 1, loadLimit);
            });
            errorBar.show();
            errorLayout.animate().alpha(0).setDuration(300).start();
            adapter.setHasAdditionalData(false);
        } else {
            errorMessage.setText(error.getLocalizedMessage());
            errorLayout.animate().alpha(1).setDuration(300).start();
        }
        initialstateview.animate().alpha(0).setDuration(300).start();
        emptyStateView.animate().alpha(0).setDuration(300).start();
        isLoadingMore = false;
        canLoadMore = false;
    }

    /**
     * Searches games with the specified query match
     *
     * @param query  the name of the game to search
     * @param offset the number of items to skip
     * @param limit  the max amount items to return
     */
    @Override
    public void search(String query, int offset, int limit) {
        presenter.search(query, offset, limit);
    }

    @Override
    public void onGameClicked(Game clickedGame, View v) {
        startActivity(GameRelationDetailActivity.init(getActivity(), clickedGame));
    }

    public interface IGameSearchFragmentListener {

        /**
         * Called when the view has been requested to be dismissed
         *
         * @param v the clicked view.
         */
        void onCloseSearchClicked(View v);
    }

}
