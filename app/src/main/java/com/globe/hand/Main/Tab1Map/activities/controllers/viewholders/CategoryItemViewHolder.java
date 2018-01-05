package com.globe.hand.Main.Tab1Map.activities.controllers.viewholders;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import com.globe.hand.R;
import com.globe.hand.common.BaseViewHolder;
import com.globe.hand.models.Category;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

/**
 * Created by ssangwoo on 2017-12-20.
 */

public class CategoryItemViewHolder extends BaseViewHolder<Category> {

    TextView textCategoryTitle, textCategoryCount;

    public CategoryItemViewHolder(ViewGroup parent) {
        super(parent, R.layout.recycler_item_category);
        textCategoryTitle = itemView.findViewById(R.id.text_category_name);
        textCategoryCount = itemView.findViewById(R.id.text_category_count);
    }

    @Override
    public void bindView(final Context context, final Category model, int position) {
        textCategoryTitle.setText(model.getName());
        FirebaseFirestore.getInstance().collection("map_room")
                .document(model.getMapRoomUid()).collection("map_post_ref")
                .whereEqualTo("category", model.getName())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                        if(documentSnapshots != null) {
                            textCategoryCount.setText(String.valueOf(documentSnapshots.size()));
                        }
                    }
                });
    }
}
