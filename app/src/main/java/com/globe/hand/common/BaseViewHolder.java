package com.globe.hand.common;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

/**
 * Created by ssangwoo on 2017-12-20.
 */

public abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder {
    public BaseViewHolder(ViewGroup parent, int layoutId) {
        super(LayoutInflater.from(parent.getContext())
                .inflate(layoutId, parent, false));
    }

    protected abstract void bindView(final Context context, T model, int position);
}
