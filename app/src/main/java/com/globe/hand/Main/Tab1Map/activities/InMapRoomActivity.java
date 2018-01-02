package com.globe.hand.Main.Tab1Map.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;

import com.globe.hand.Main.Tab1Map.activities.controllers.MapRoomController;
import com.globe.hand.Main.Tab1Map.activities.fragments.ShowMapPostViewPagerFragment;
import com.globe.hand.common.BaseActivity;
import com.globe.hand.R;
import com.globe.hand.models.MapPost;
import com.globe.hand.models.MapRoom;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class InMapRoomActivity extends BaseActivity implements OnMapReadyCallback {

    private MapRoomController mapRoomController;
    private ListenerRegistration registration;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private SupportMapFragment mapFragment;

    private MapRoom mapRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        setToolbar(R.id.map_toolbar, false);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        drawerLayout = findViewById(R.id.map_drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout,
                toolbar, R.string.open, R.string.close);

        drawerLayout.addDrawerListener(toggle);

        mapFragment = SupportMapFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.map_container, mapFragment)
                .commit();
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapRoomController = new MapRoomController(InMapRoomActivity.this, googleMap);

        // 마커 불러오는 부분
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        String mapRoomUid = getIntent().getStringExtra("map_room_uid");

        DocumentReference currentMapRoom =
                db.collection("map_room").document(mapRoomUid);
        currentMapRoom.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    mapRoom = task.getResult().toObject(MapRoom.class);
                    mapRoomController.setMapRoom(mapRoom.getUid());
                    mapRoomController.setOnMapPostMarkerClickListener(new MapRoomController.
                            OnMapPostMarkerClickListener() {
                        @Override
                        public void onMapPostMarkerClick() {
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.map_container,
                                            ShowMapPostViewPagerFragment.newInstance(mapRoom.getUid()))
                                    .addToBackStack(null)
                                    .commit();
                        }
                    });

                    setToolbarTitle(mapRoom.getTitle());

                    //TODO: 바운드에 맞게 마커 로딩하기
                    registration = db.collection("map_room").document(mapRoom.getUid())
                            .collection("map_post").addSnapshotListener(
                                    new EventListener<QuerySnapshot>() {
                                        @Override
                                        public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                                            List<MapPost> postList = documentSnapshots.toObjects(MapPost.class);
                                            mapRoomController.initMapPostMarkers(postList);
                                        }
                                    });
                }
            }
        });

        // 대전시청
        //TODO: 마커들의 위치를 고려한 처음 카메라 위치 선정
        mapRoomController.initialize(new LatLng(36.35049163104827, 127.38484181463717));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == getResources().getInteger(R.integer.map_post_request_code)) {
            if (resultCode == RESULT_OK) {
                String title = data.getStringExtra("title");
                String content = data.getStringExtra("content");
                LatLng latLng = data.getParcelableExtra("latlng");
                mapRoomController.newMapPostMarker(latLng, title, content).showInfoWindow();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_close, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        } else if (item.getItemId() == R.id.action_close) {
            onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(registration != null) {
            registration.remove();
        }
    }
}
