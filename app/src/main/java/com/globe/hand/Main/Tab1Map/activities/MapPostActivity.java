package com.globe.hand.Main.Tab1Map.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.globe.hand.R;
import com.globe.hand.common.BaseActivity;
import com.globe.hand.models.MapPost;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

public class MapPostActivity extends BaseActivity {

    private TextView textPlace;
    private EditText editTitle;
    private EditText editContent;

    private String mapRoomUid;
    private LatLng mapLatLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_post);

        setToolbar(R.id.map_post_toolbar, true);

        mapLatLng = getIntent().getParcelableExtra("map_room_latLng");
        mapRoomUid = getIntent().getStringExtra("map_room_uid");

        textPlace = findViewById(R.id.text_map_post_place);

        editTitle = findViewById(R.id.edit_map_post_title);
        editContent = findViewById(R.id.edit_map_post_content);

        if (getIntent().hasExtra("place_name")) {
            textPlace.setText(getIntent().getStringExtra("place_name"));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_post_done, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_post_done) {
            String title = editTitle.getText().toString();
            String content = editContent.getText().toString();
            FirebaseFirestore.getInstance()
                    .collection("map_room").document(mapRoomUid)
                    .collection("map_post").add(new MapPost(
                        new GeoPoint(mapLatLng.latitude, mapLatLng.longitude),
                        title, content))
                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if(task.isSuccessful()) {
                                task.getResult().update("uid", task.getResult().getId());
                            }
                        }
                    });
            Intent inMapRoomIntent = new Intent();
            inMapRoomIntent.putExtra("title", title);
            inMapRoomIntent.putExtra("content", content);
            inMapRoomIntent.putExtra("latlng", mapLatLng);
            setResult(RESULT_OK, inMapRoomIntent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("경고")
                .setMessage("작성한 내용이 모두 사라집니다! 정말 지도 화면으로 가시겠습니까?")
                .setPositiveButton(R.string.dialog_back, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        MapPostActivity.super.onBackPressed();
                        dialogInterface.dismiss();
                    }
                }).setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).show();
    }
}
