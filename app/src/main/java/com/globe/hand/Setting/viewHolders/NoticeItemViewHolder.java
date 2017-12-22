package com.globe.hand.Setting.viewHolders;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import com.globe.hand.common.BaseViewHolder;
import com.globe.hand.R;
import com.globe.hand.models.Notice;

/**
 * Created by ssangwoo on 2017-12-20.
 */

public class NoticeItemViewHolder extends BaseViewHolder<Notice> {

    TextView textNoticeTitle;

    public NoticeItemViewHolder(ViewGroup parent) {
        super(parent, R.layout.recycler_item_notice);
        textNoticeTitle = itemView.findViewById(R.id.text_notice_title);
    }

    @Override
    public void bindView(final Context context, final Notice model, int position) {
        textNoticeTitle.setText(model.getTitle());
    }
}
