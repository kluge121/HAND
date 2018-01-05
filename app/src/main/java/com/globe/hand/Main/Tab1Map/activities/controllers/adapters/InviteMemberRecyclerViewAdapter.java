package com.globe.hand.Main.Tab1Map.activities.controllers.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.globe.hand.Main.Tab1Map.activities.controllers.viewholders.InviteMemberItemViewHolder;
import com.globe.hand.common.BaseViewHolder;
import com.globe.hand.models.UploadUser;

import java.util.List;

/**
 * Created by ssangwoo on 2017-12-20.
 */

public class InviteMemberRecyclerViewAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private Context context;

    private List<UploadUser> uploadUserList;

    private OnFriendCheckListener listener;

    public InviteMemberRecyclerViewAdapter(Context context, List<UploadUser> uploadUserList,
                                           OnFriendCheckListener listener) {
        this.context = context;
        this.uploadUserList = uploadUserList;
        this.listener = listener;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new InviteMemberItemViewHolder(parent, listener);
    }

    @Override
    public void onBindViewHolder(final BaseViewHolder holder, int position) {
        ((InviteMemberItemViewHolder)holder).bindView(context,
                uploadUserList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return uploadUserList.size();
    }

    public interface OnFriendCheckListener {
        void onFriendCheck(UploadUser user, boolean isSelected);
    }
}
