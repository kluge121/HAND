package com.globe.hand.Main.Tab1Map.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.globe.hand.Main.Tab1Map.activities.fragments.MapPostAddPictureFragment;
import com.globe.hand.R;
import com.globe.hand.common.BaseActivity;
import com.globe.hand.models.Category;
import com.globe.hand.models.MapPost;
import com.globe.hand.models.MapPostReference;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class MapPostActivity extends BaseActivity
        implements MapPostAddPictureFragment.OnUploadMapPostPictureListener {
    private TextView textPlace;
    private EditText editTitle;
    private Spinner spinnerCategory;
    private EditText editContent;

    private String mapRoomUid;
    private LatLng mapLatLng;
    private String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_post);

        setToolbar(R.id.map_post_toolbar, true);

        mapLatLng = getIntent().getParcelableExtra("map_room_latLng");
        mapRoomUid = getIntent().getStringExtra("map_room_uid");

        textPlace = findViewById(R.id.text_map_post_place);

        editTitle = findViewById(R.id.edit_map_post_title);
        spinnerCategory = findViewById(R.id.spinner_map_post_category);
        editContent = findViewById(R.id.edit_map_post_content);

        if (getIntent().hasExtra("place_name")) {
            textPlace.setText(getIntent().getStringExtra("place_name"));
        }

        FirebaseFirestore.getInstance()
                .collection("map_room").document(mapRoomUid)
                .collection("category").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            List<Category> categoryList = task.getResult().toObjects(Category.class);
                            spinnerCategory.setAdapter(new ArrayAdapter<>(MapPostActivity.this,
                                    android.R.layout.simple_spinner_dropdown_item,
                                    categoryList));
                        }
                    }
                });

        getSupportFragmentManager().beginTransaction()
                .add(R.id.map_post_picture_container,
                        MapPostAddPictureFragment.newInstance())
                .commit();
    }

    @Override
    public void onUploadMapPostPicture(String imageUrl) {
        this.imageUrl = imageUrl;
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
            final String category = ((Category)spinnerCategory.getSelectedItem()).getName();
            final String categoryUid = ((Category)spinnerCategory.getSelectedItem()).getUid();

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            final DocumentReference mapRoomReference = db.collection("map_room").document(mapRoomUid);
            CollectionReference mapPostReference = mapRoomReference
                    .collection("category").document(categoryUid).collection("map_post");

            mapPostReference.add(new MapPost(
                    FirebaseAuth.getInstance().getCurrentUser().getUid(),
                    new GeoPoint(mapLatLng.latitude, mapLatLng.longitude), title, content, imageUrl))
                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()) {
                                // uid 업데이트
                                task.getResult().update("uid", task.getResult().getId());

                                // map_room_ref 추가
                                mapRoomReference.collection("map_post_ref")
                                        .document(task.getResult().getId())
                                        .set(new MapPostReference(task.getResult(), category,
                                                FirebaseAuth.getInstance().getCurrentUser().getUid()));
                            }
                        }
                    });
//            Intent inMapRoomIntent = new Intent();
//            inMapRoomIntent.putExtra("title", title);
//            inMapRoomIntent.putExtra("content", content);
//            inMapRoomIntent.putExtra("latlng", mapLatLng);
//            inMapRoomIntent.putExtra("image_url", imageUrl);
            setResult(RESULT_OK); // , inMapRoomIntent);
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
