package com.piticlistudio.playednext.image.ui.viewer;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.piticlistudio.playednext.AndroidApplication;
import com.piticlistudio.playednext.R;
import com.piticlistudio.playednext.di.component.AppComponent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class ImageViewerActivity extends AppCompatActivity {

    private final static String IMAGE_URLS = "image-urls";

    @BindView(R.id.viewpager)
    ViewPager viewPager;

    public static Intent init(Activity activity, List<String> data) {
        Intent intent = new Intent(activity, ImageViewerActivity.class);
        intent.putStringArrayListExtra(IMAGE_URLS, new ArrayList<>(data));
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.image_viewer_fragment);
        ButterKnife.bind(this);

        int orientation = getResources().getConfiguration().orientation;
        if (getIntent().hasExtra(IMAGE_URLS) && orientation == Configuration.ORIENTATION_LANDSCAPE) {
            ImageViewPagerAdapter adapter = new ImageViewPagerAdapter(getSupportFragmentManager(), getIntent().getStringArrayListExtra(IMAGE_URLS));
            viewPager.setAdapter(adapter);
        }
        else {
            finish();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);}
    }

    /**
     * Take care of popping the fragment back stack or finishing the activity
     * as appropriate.
     */
    @Override
    public void onBackPressed() {
        // TODO show a message to user that, if he want's to close this view, he just needs to rotate the device
    }

    public static class ImageFragment extends Fragment {

        static String IMAGE_URL = "url";

        @BindView(R.id.image)
        ImageView image;

        private Unbinder unbinder;

        public static ImageFragment newInstance(String image) {
            ImageFragment fragment = new ImageFragment();
            Bundle args = new Bundle();
            args.putString(IMAGE_URL, image);
            fragment.setArguments(args);
            return fragment;
        }

        private AppComponent getAppComponent() {
            return ((AndroidApplication) getActivity().getApplication()).appComponent;
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.image_viewer_pager_item, container, false);
            unbinder = ButterKnife.bind(this, v);
            return v;
        }

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            if (getArguments().containsKey(IMAGE_URL)) {
                getAppComponent().picasso()
                        .load(getArguments().getString(IMAGE_URL))
                        .fit()
                        .centerInside()
                        .into(image);
            }
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
            unbinder.unbind();
        }
    }

    /**
     * A pager that displays a list of images
     * Created by jorge.garcia on 16/02/2017.
     */

    public class ImageViewPagerAdapter extends FragmentPagerAdapter {

        private List<String> imageUrls = new ArrayList<>();

        ImageViewPagerAdapter(FragmentManager fm, List<String> images) {
            super(fm);
            this.imageUrls = images;
        }

        /**
         * Return the Fragment associated with a specified position.
         *
         * @param position
         */
        @Override
        public Fragment getItem(int position) {
            return ImageFragment.newInstance(imageUrls.get(position));
        }

        /**
         * Return the number of views available.
         */
        @Override
        public int getCount() {
            return imageUrls.size();
        }

    }
}
