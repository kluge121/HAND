package com.globe.hand.Main.Tab2Event.fragment.controllers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

/**
 * Created by baeminsu on 2017. 12. 28..
 */

public class MyEventViewHolder extends BaseViewHolder<DocumentSnapshot> {

    private View view;
    private TextView category;
    private TextView name;
    private TextView needCount;
    private TextView count;
    private TextView point;
    private TextView price;
    private TextView content;
    private ImageView image;
    private ImageView ivContent;
    private ImageView ivSide;
    private MyEventAdapter adapter;
    private EventEntity model;


    MyEventViewHolder(ViewGroup parent, int layoutId, MyEventAdapter adapter) {
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
        this.adapter = adapter;

        ivContent = itemView.findViewById(R.id.middle_event_content);
        ivSide = itemView.findViewById(R.id.middle_event_side);


    }


    @Override
    public void bindView(final Context context, DocumentSnapshot ref, final int position) {


        DocumentReference documentReference = (DocumentReference) ref.get("eventRef");

        Log.e("체크", ref.toString());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {

                    model = task.getResult().toObject(EventEntity.class);

                    Log.e("체크", model.getContent() + "ㅇ");

                    category.setText(model.getCategory());
                    name.setText(model.getEventName());
                    needCount.setText(model.getCount() - model.getCurrentCount() + "명");
                    count.setText("(" + model.getCurrentCount() + "/" + model.getCount() + ")");
                    point.setText(model.getPoint());
                    price.setText(model.getPrice());
                    content.setText(model.getContent());

                    if (model.getImageUrl() != null)
                        Glide.with(context).load(Uri.parse(model.getImageUrl())).into(image);


                }
            }
        });


        ivSide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(context).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cancelEvent(model, position);

                    }
                }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setMessage(model.getEventName() + "참여를 취소하시겠습니까?").show();

            }
        });

    }

    void cancelEvent(EventEntity model, final int position) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser loginUser = FirebaseAuth.getInstance().getCurrentUser();


        //제-거
        DocumentReference userEvnetRef = db.collection("user").document(loginUser.getUid())
                .collection("userEvent").document(model.getUid());

        //이벤트 - 1 해야함
        DocumentReference eventRef = db.collection("event").document(model.getUid());

        WriteBatch batch = db.batch();
        batch.delete(userEvnetRef);
        batch.update(eventRef, "currentCount", model.getCurrentCount() - 1);

        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    adapter.removeItem(position);
                }

            }
        });


    }


}
