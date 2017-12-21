package com.globe.hand.Main.Tab1Map.controllers;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by alstn on 2017-08-10.
 */

public class RecyclerViewDecoration extends RecyclerView.ItemDecoration {

    private final int divHeight;
    private final int divWeight;

    public RecyclerViewDecoration(int divHeight, int divWeight) {
        this.divHeight = divHeight;
        this.divWeight = divWeight;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.top = divHeight;
        outRect.bottom = divHeight;
        outRect.right = divWeight;
        outRect.left = divWeight;

    }
}
