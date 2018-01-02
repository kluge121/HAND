package com.globe.hand.Main.Tab1Map.activities.controllers.adapters;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.globe.hand.R;
import com.globe.hand.models.MapPost;

import java.util.List;

/**
 * Created by ssangwoo on 2018-01-02.
 */

public class MapPostViewPagerAdapter extends PagerAdapter {

    private Activity activity;

    private List<MapPost> mapPostList;

    public MapPostViewPagerAdapter(Activity activity, List<MapPost> mapPostList) {
        this.activity = activity;
        this.mapPostList = mapPostList;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(activity)
                .inflate(R.layout.layout_show_map_post, container, false);

        MapPost mapPost = mapPostList.get(position);

//        ImageView mapPostImage = view.findViewById(R.id.show_map_post_image);
        TextView mapPostTitle = view.findViewById(R.id.show_map_post_title);
        TextView mapPostContent = view.findViewById(R.id.show_map_post_content);
        ImageView mapPostMarker = view.findViewById(R.id.show_map_post_marker);

//        mapPostImage.setImageResource(mapPost.get);
        mapPostTitle.setText(mapPost.getTitle());
        mapPostContent.setText(mapPost.getContent());
        mapPostMarker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.onBackPressed();
            }
        });

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }

    @Override
    public int getCount() {
        return mapPostList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
