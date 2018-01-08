package com.globe.hand.Main.Tab1Map.activities.controllers.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.globe.hand.R;
import com.globe.hand.models.MapPost;
import com.globe.hand.models.MapPostReference;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

/**
 * Created by ssangwoo on 2018-01-02.
 */

public class MapPostFirebaseViewPagerAdapter extends PagerAdapter {

    private Activity activity;

    List<MapPostReference> mapPostReferenceList;

    public MapPostFirebaseViewPagerAdapter(Activity activity, List<MapPostReference> mapPostReferenceList) {
        this.activity = activity;
        this.mapPostReferenceList = mapPostReferenceList;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final View view = LayoutInflater.from(activity)
                .inflate(R.layout.layout_show_map_post, null);

        mapPostReferenceList.get(position).getMapPostReference()
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    MapPost mapPost = task.getResult().toObject(MapPost.class);

                    ImageView imageMapPost = view.findViewById(R.id.show_map_post_image);
                    TextView textTitle = view.findViewById(R.id.show_map_post_title);
                    TextView textContent = view.findViewById(R.id.show_map_post_content);

                    if(mapPost.getImageUrl() != null) {
                        Glide.with(activity)
                                .load(mapPost.getImageUrl())
                                .into(imageMapPost);
                    }
                    textTitle.setText(mapPost.getTitle());
                    textContent.setText(mapPost.getContent());

                    ImageView imageMarker = view.findViewById(R.id.show_map_post_marker);
                    imageMarker.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            activity.onBackPressed();
                        }
                    });
                }
            }
        });
        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return mapPostReferenceList.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
