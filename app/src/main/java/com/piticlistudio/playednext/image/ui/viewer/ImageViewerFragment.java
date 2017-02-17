package com.piticlistudio.playednext.image.ui.viewer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
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

/**
 * Fragment that displays a list of images
 * Created by jorge.garcia on 16/02/2017.
 */

public class ImageViewerFragment extends Fragment {

    public static final String TAG = "ImageViewer";
    private final static String IMAGE_URLS = "image-urls";

    @BindView(R.id.viewpager)
    ViewPager viewPager;

    private Unbinder unbinder;

    public static ImageViewerFragment newInstance(List<String> data) {
        ImageViewerFragment fragment = new ImageViewerFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(IMAGE_URLS, new ArrayList<>(data));
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.image_viewer_fragment, container, false);
        unbinder = ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ImageViewPagerAdapter adapter = new ImageViewPagerAdapter(getChildFragmentManager(), getArguments().getStringArrayList(IMAGE_URLS));
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
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

        public ImageViewPagerAdapter(FragmentManager fm, List<String> images) {
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
