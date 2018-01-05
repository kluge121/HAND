package com.globe.hand.Main.Tab1Map.activities.controllers.viewholders;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.globe.hand.Main.Tab1Map.activities.controllers.adapters.InviteMemberRecyclerViewAdapter;
import com.globe.hand.R;
import com.globe.hand.common.BaseViewHolder;
import com.globe.hand.models.UploadUser;
import com.globe.hand.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ssangwoo on 2017-12-20.
 */

public class InviteMemberItemViewHolder extends BaseViewHolder<UploadUser> {

    private CircleImageView imageFriendPhoto;
    private TextView textFriendName;
    private ImageView imageSelectFriend;

    private boolean isSelectedFriend;

    private InviteMemberRecyclerViewAdapter.OnFriendCheckListener listener;

    public InviteMemberItemViewHolder(ViewGroup parent,
                                      InviteMemberRecyclerViewAdapter.OnFriendCheckListener listener) {
        super(parent, R.layout.recycler_item_invite_member);
        imageFriendPhoto = itemView.findViewById(R.id.image_friend_photo);
        textFriendName = itemView.findViewById(R.id.text_friend_name);
        imageSelectFriend = itemView.findViewById(R.id.image_select_friend);
        this.listener = listener;
    }

    @Override
    public void bindView(final Context context, final UploadUser model, int position) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isSelectedFriend) {
                    isSelectedFriend = false;
                    imageSelectFriend.setImageResource(R.drawable.friend_plus);
                } else {
                    isSelectedFriend = true;
                    imageSelectFriend.setImageResource(R.drawable.friend_x);
                }
                listener.onFriendCheck(model, isSelectedFriend);
            }
        });
        model.getUserRef().get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            User user = task.getResult().toObject(User.class);

                            if(user.getProfile_url() != null) {
                                Glide.with(context)
                                        .load(user.getProfile_url())
                                        .into(imageFriendPhoto);
                            }

                            textFriendName.setText(user.getName());
                        }
                    }
                });
    }
}
