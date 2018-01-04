package com.globe.hand.Main.Tab1Map.activities.controllers.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.globe.hand.Main.Tab1Map.activities.controllers.viewholders.CategoryItemViewHolder;
import com.globe.hand.R;
import com.globe.hand.common.BaseViewHolder;
import com.globe.hand.models.Category;

import java.util.List;

/**
 * Created by ssangwoo on 2017-12-20.
 */

public class MapRoomCategoryRecyclerAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private final static int BOUNDARY_VIEW_TYPE = 11;

    private Context context;

    private List<Category> categoryList;

    public MapRoomCategoryRecyclerAdapter(Context context, List<Category> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case BOUNDARY_VIEW_TYPE:
                return new BaseViewHolder(parent, R.layout.recycler_item_category_boundary) {
                    @Override
                    public void bindView(Context context, Object model, int position) {
                        // nothing
                    }
                };
            default:
                return new CategoryItemViewHolder(parent);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position % 2 == 0) {
            return super.getItemViewType(position);
        } else {
            return BOUNDARY_VIEW_TYPE;
        }
    }

    @Override
    public void onBindViewHolder(final BaseViewHolder holder, int position) {
        if(position % 2 == 0) {
            int currentPosition = position == 0 ? 0: position/2;
            ((CategoryItemViewHolder) holder).bindView(context,
                    categoryList.get(currentPosition), currentPosition);
        }
    }

    @Override
    public int getItemCount() {
        int itemCount = categoryList.size();
        int boundary = itemCount > 1 ? itemCount-1 : 0;
        return itemCount + boundary;
    }
}
