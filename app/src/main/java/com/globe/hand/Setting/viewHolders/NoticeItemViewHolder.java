package com.globe.hand.Setting.viewHolders;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.globe.hand.MapRoom.RealMapActivity;
import com.globe.hand.common.BaseViewHolder;
import com.globe.hand.R;
import com.globe.hand.models.MapRoom;
import com.globe.hand.models.Notice;

/**
 * Created by ssangwoo on 2017-12-20.
 */

public class NoticeItemViewHolder extends BaseViewHolder<Notice> {

    RelativeLayout noticeContainer;
    TextView textNoticeTitle;

    public NoticeItemViewHolder(ViewGroup parent) {
        super(parent, R.layout.layout_notice_item);
        noticeContainer = itemView.findViewById(R.id.notice_container);
        textNoticeTitle = itemView.findViewById(R.id.text_notice_title);
    }

    @Override
    public void bindView(final Context context, final Notice model, int position) {
        noticeContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO:
            }
        });
        textNoticeTitle.setText(model.getTitle());
    }
}
