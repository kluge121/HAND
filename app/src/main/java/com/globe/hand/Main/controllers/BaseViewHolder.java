package com.globe.hand.Main.controllers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by ssangwoo on 2017-12-20.
 */

public abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder {
    private Context context;

    public BaseViewHolder(ViewGroup parent, int layoutId) {
        super(LayoutInflater.from(parent.getContext())
                .inflate(layoutId, parent, false));
    }

    public abstract void bindView(T model, int position);
}
