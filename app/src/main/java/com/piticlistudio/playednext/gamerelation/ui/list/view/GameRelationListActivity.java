package com.piticlistudio.playednext.gamerelation.ui.list.view;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;

import com.piticlistudio.playednext.AndroidApplication;
import com.piticlistudio.playednext.R;
import com.piticlistudio.playednext.di.component.AppComponent;
import com.piticlistudio.playednext.game.ui.search.view.GameSearchFragment;
import com.piticlistudio.playednext.gamerelation.model.entity.GameRelation;
import com.piticlistudio.playednext.gamerelation.ui.detail.view.GameRelationDetailActivity;
import com.piticlistudio.playednext.gamerelation.ui.list.DaggerGameRelationListComponent;
import com.piticlistudio.playednext.gamerelation.ui.list.GameRelationListComponent;
import com.piticlistudio.playednext.gamerelation.ui.list.GameRelationListContract;
import com.piticlistudio.playednext.gamerelation.ui.list.GameRelationListModule;
import com.piticlistudio.playednext.gamerelation.ui.list.adapter.GameRelationListAdapter;
import com.piticlistudio.playednext.relationinterval.model.entity.RelationInterval;
import com.piticlistudio.playednext.ui.recyclerview.SwipeTouchHelper;
import com.piticlistudio.playednext.ui.widget.RevealBackgroundView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Activity that displays a list of GameRelation entities
 * Created by jorge.garcia on 01/03/2017.
 */

public class GameRelationListActivity extends AppCompatActivity implements GameRelationListContract.View, GameSearchFragment.IGameSearchFragmentListener, GameRelationListAdapter.GameRelationAdapterListener {

    private static final String TAG = "GameRelationList";
    @BindView(R.id.listview)
    RecyclerView listview;
    @BindView(R.id.addBtn)
    FloatingActionButton fabBtn;
    Unbinder unbinder;
    @BindView(R.id.reveal)
    RevealBackgroundView reveal;
    @Inject
    public GameRelationListContract.Presenter presenter;
    private GameSearchFragment searchView;
    @Inject
    public GameRelationListAdapter adapter;
    private GameRelationListComponent component;

    protected AppComponent getAppComponent() {
        return ((AndroidApplication) getApplication()).appComponent;
    }

    protected GameRelationListComponent getComponent() {
        component = ((AndroidApplication)getApplication()).getRelationListComponent();
        if (component == null) {
            component = DaggerGameRelationListComponent.builder()
                    .appComponent(getAppComponent())
                    .gameRelationListModule(new GameRelationListModule())
                    .build();
        }
        return component;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gamerelation_list_activity);
        unbinder = ButterKnife.bind(this);

        getComponent().inject(this);
        presenter.attachView(this);

//        adapter = component.listAdapter();
        listview.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
        listview.setLayoutManager(new LinearLayoutManager(getParent()));
        listview.setAdapter(adapter);
        adapter.setListener(this);

        SwipeTouchHelper swipeTouchHelper = new SwipeTouchHelper(adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeTouchHelper);
        itemTouchHelper.attachToRecyclerView(listview);

        loadData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView(false);
        component = null;
    }

    /**
     * Take care of popping the fragment back stack or finishing the activity
     * as appropriate.
     */
    @Override
    public void onBackPressed() {
        if (searchView == null)
            finish();
        else {
            searchView.onBackPressed();
        }
    }

    /**
     * Sets the data
     *
     * @param completed the list of items completed
     * @param current   the list of items being completed
     * @param waiting   the list of items waiting to be completed
     */
    @Override
    public void setData(List<GameRelation> completed, List<GameRelation> current, List<GameRelation> waiting) {
        Log.d(TAG, "setData() called with: completed = [" + completed.size() + "], current = [" + current.size() + "], waiting = [" +
                waiting.size() + "]");
        adapter.setData(completed, current, waiting);
    }

    /**
     * Loads the data
     */
    @Override
    public void loadData() {
        presenter.loadData();
    }

    /**
     * Shows a loading view
     */
    @Override
    public void showLoading() {
        Log.d(TAG, "showLoading() called");

    }

    /**
     * Shows the main content
     */
    @Override
    public void showContent() {
        Log.d(TAG, "showContent() called");
    }

    /**
     * Shows the error
     *
     * @param error the error to show
     */
    @Override
    public void showError(Throwable error) {
        Log.d(TAG, "showError() called with: error = [" + error + "]");
    }

    @OnClick(R.id.addBtn)
    public void searchGame(View v) {
        revealSearchView(v);
    }

    private void revealSearchView(View fromview) {
        reveal.setRevealColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
        int[] location = new int[2];
        fromview.getLocationOnScreen(location);
        location[0] += fromview.getWidth() / 2;
        reveal.startFromLocation(location);
        searchView = new GameSearchFragment();
        searchView.setListener(this);
        fabBtn.setVisibility(View.GONE);
        reveal.setOnStateChangeListener(state -> {
            if (state == RevealBackgroundView.STATE_FINISHED && searchView != null) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.setCustomAnimations(R.anim.enter_from_top, R.anim.exit_to_top);
                transaction.add(android.R.id.content, searchView, "gamesearch");
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }

    /**
     * Called when the view has been requested to be dismissed
     *
     * @param v the clicked view.
     */
    @Override
    public void onCloseSearchClicked(View v) {
        if (searchView != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
            transaction.setCustomAnimations(R.anim.exit_to_top, R.anim.exit_to_top);
            transaction.remove(searchView);
            transaction.commit();

            // TODO: 03/03/2017 is there a way to be notified when the transaction has completed?
            int animDuration = getResources().getInteger(R.integer.gamesearch_exit_anim_duration);
            Handler handler = new Handler();
            handler.postDelayed(() -> {
                int[] location = new int[2];
                fabBtn.getLocationOnScreen(location);
                location[0] += fabBtn.getWidth() / 2;
                reveal.hideFromLocation(location);
                reveal.setOnStateChangeListener(state -> {
                    if (state == RevealBackgroundView.STATE_FINISHED) {
                        searchView = null;
                        fabBtn.setVisibility(View.VISIBLE);
                        searchView = null;
                    }
                });


            }, animDuration);
        }
    }

    /**
     * Callback whenever a relation has been clicked
     *
     * @param clickedRelation the relation clicked
     */
    @Override
    public void onGameRelationClicked(GameRelation clickedRelation) {
        startActivity(GameRelationDetailActivity.init(this, clickedRelation.game()));
    }

    /**
     * Callback when a change on the current relation type has been requested
     *
     * @param relation    the relation requested to update its type
     * @param currentType the current type of the relation
     * @param newType     the new requested type
     */
    @Override
    public void onGameRelationChanged(GameRelation relation, RelationInterval.RelationType currentType, RelationInterval.RelationType newType) {
        presenter.save(relation, newType);
    }
}
