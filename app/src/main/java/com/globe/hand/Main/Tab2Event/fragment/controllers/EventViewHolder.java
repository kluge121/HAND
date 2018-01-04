package com.globe.hand.Main.Tab2Event.fragment.controllers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.globe.hand.Main.Tab2Event.models.EventEntity;
import com.globe.hand.Main.Tab2Event.models.EventRefEntity;
import com.globe.hand.R;
import com.globe.hand.common.BaseViewHolder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.text.DecimalFormat;

/**
 * Created by baeminsu on 2017. 12. 28..
 */

public class EventViewHolder extends BaseViewHolder<EventEntity> {

    private View view;
    private TextView category;
    private TextView name;
    private TextView needCount;
    private TextView count;
    private TextView point;
    private TextView price;
    private TextView content;
    private ImageView image;
    private EventAdapter adapter;
    private ImageView ivContent;
    private ImageView ivSide;


    EventViewHolder(ViewGroup parent, int layoutId, EventAdapter adapter) {
        super(parent, layoutId);
        view = itemView;

        category = itemView.findViewById(R.id.event_item_category);
        name = itemView.findViewById(R.id.event_item_name);
        needCount = itemView.findViewById(R.id.event_item_need_count);
        count = itemView.findViewById(R.id.event_item_count);
        point = itemView.findViewById(R.id.event_item_point);
        price = itemView.findViewById(R.id.event_item_price);
        content = itemView.findViewById(R.id.event_item_content);
        image = itemView.findViewById(R.id.event_item_image);

        ivContent = itemView.findViewById(R.id.middle_event_content);
        ivSide = itemView.findViewById(R.id.middle_event_side);

        this.adapter = adapter;


    }


    @Override
    public void bindView(final Context context, final EventEntity model, final int position) {


        category.setText(model.getCategory());
        name.setText(model.getEventName());
        needCount.setText(model.getCount() - model.getCurrentCount() + "명");
        count.setText("(" + model.getCurrentCount() + "/" + model.getCount() + ")");
        point.setText(model.getPoint());
        price.setText(model.getPrice());
        content.setText(model.getContent());

        if (model.getImageUrl() != null)
            Glide.with(context).load(Uri.parse(model.getImageUrl())).into(image);

        ivSide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(context).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addEventList(model, position);

                    }
                }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setMessage(model.getEventName() + "에 참여하시겠습니까").show();

            }
        });

    }

    private void addEventList(EventEntity model, final int postion) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser loginUser = FirebaseAuth.getInstance().getCurrentUser();

        DocumentReference userEvnetRef = db.collection("user").document(loginUser.getUid())
                .collection("userEvent").document(model.getUid());

        DocumentReference eventRef = db.collection("event").document(model.getUid());


        EventRefEntity insetModel = new EventRefEntity(model);

        WriteBatch batch = db.batch();
        batch.set(userEvnetRef, insetModel);
        batch.update(eventRef, "currentCount", model.getCurrentCount() + 1);


        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    adapter.removeItem(postion);
                }

            }
        });

    }


}
