package com.globe.hand.Setting.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.globe.hand.common.BaseViewHolder;
import com.globe.hand.Setting.viewHolders.NoticeItemViewHolder;
import com.globe.hand.models.Notice;

import java.util.List;

/**
 * Created by ssangwoo on 2017-12-20.
 */

public class SettingRecyclerViewAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private Context context;

    private List<Notice> noticeList;

    public SettingRecyclerViewAdapter(Context context, List<Notice> noticeList) {
        this.context = context;
        this.noticeList = noticeList;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NoticeItemViewHolder(parent);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        ((NoticeItemViewHolder)holder).bindView(context,
                noticeList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return noticeList.size();
    }
}
