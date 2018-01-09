package com.globe.hand.Main.Tab1Map.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.globe.hand.Main.Tab1Map.activities.controllers.MapRoomController;
import com.globe.hand.Main.Tab1Map.activities.controllers.adapters.MapRoomCategoryRecyclerAdapter;
import com.globe.hand.Main.Tab1Map.activities.controllers.adapters.MapRoomMemberFirebaseRecyclerViewAdapter;
import com.globe.hand.Main.Tab1Map.activities.fragments.ShowMapPostViewPagerFragment;
import com.globe.hand.common.BaseActivity;
import com.globe.hand.R;
import com.globe.hand.models.Category;
import com.globe.hand.models.MapPost;
import com.globe.hand.models.MapPostReference;
import com.globe.hand.models.MapRoom;
import com.globe.hand.models.MapRoomMember;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
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
        setContentView(R.layout.activity_map_room);

        setToolbar(R.id.map_room_toolbar, false);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        drawerLayout = findViewById(R.id.map_room_drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout,
                toolbar, R.string.open, R.string.close);
        toggle.setDrawerIndicatorEnabled(false);
        toggle.setHomeAsUpIndicator(R.drawable.ic_map_room_drawer);
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });

        drawerLayout.addDrawerListener(toggle);

        mapFragment = SupportMapFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.map_room_container, mapFragment)
                .commit();
        mapFragment.getMapAsync(this);
    }

    private void initNavigationView() {
        final NavigationView navigation = findViewById(R.id.map_room_drawer_content);
        TextView textName = navigation.findViewById(R.id.map_room_drawer_name);
        final TextView textPostCount = navigation.findViewById(R.id.text_map_room_post_count);

        ImageView showCategoryList = navigation.findViewById(R.id.map_room_drawer_show_category_list);
        final RecyclerView categoryRecycler = navigation.findViewById(R.id.map_room_drawer_category_recycler_view);
        RelativeLayout categoryPlusContainer = navigation.findViewById(R.id.map_room_drawer_category_plus_container);

        LinearLayout memberContainer = navigation.findViewById(R.id.map_room_drawer_member_container);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        final DocumentReference mapRoomReference = db.collection("map_room")
                .document(mapRoom.getUid());


        // 카테고리 윗부분
        String displayName = user.getDisplayName();
        if(getIntent().hasExtra("friend_name")) {// 친구꺼라면
            displayName = getIntent().getStringExtra("friend_name");
        }
        textName.setText(String.format(getString(R.string.map_room_drawer_name_format),
                displayName));

        mapRoomReference.collection("map_post_ref").whereEqualTo("authorUid", mapRoom.getUid())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                        if (documentSnapshots != null) {
                            textPostCount.setText(String.format(getString(R.string.map_room_drawer_post_format),
                                    documentSnapshots.size()));
                        }
                    }
                });

        // 카테고리 윗쪽버튼(문서아이콘)
        showCategoryList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 이게 무슨버튼일까
            }
        });

        // 카테고리 리사이클러
        categoryRecycler.setLayoutManager(new LinearLayoutManager(InMapRoomActivity.this));
        mapRoomReference.collection("category")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.e("category_snapshot", e.getMessage());
                            return;
                        }
                        categoryRecycler.setAdapter(new MapRoomCategoryRecyclerAdapter(
                                InMapRoomActivity.this, documentSnapshots.toObjects(Category.class)));
                    }
                });

        // 친구 맵룸을 누른게 아니라면
        if(!getIntent().hasExtra("friend_name")) {
            // 카테고리 추가
            categoryPlusContainer.setVisibility(View.VISIBLE);
            categoryPlusContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final EditText editCategory = new EditText(InMapRoomActivity.this);
                    AlertDialog.Builder alertBuilder =
                            new AlertDialog.Builder(InMapRoomActivity.this)
                                    .setTitle("카테고리 추가")
                                    .setMessage("추가할 카테고리의 이름을 입력해주세요.")
                                    .setPositiveButton(getString(R.string.dialog_add), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            final String category = editCategory.getText().toString();
                                            if (!category.isEmpty()) {
                                                mapRoomReference.collection("category").add(
                                                        new Category(mapRoom.getUid(), category))
                                                        .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                                                if (task.isSuccessful()) {
                                                                    task.getResult().update("uid", task.getResult().getId());
                                                                }
                                                            }
                                                        });
                                            }
                                            dialogInterface.dismiss();
                                        }
                                    }).setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                    alertBuilder.setView(editCategory);
                    alertBuilder.show();
                }
            });

            // 멤버 부분
            memberContainer.setVisibility(View.VISIBLE);
            db.collection("map_room").document(mapRoom.getUid())
                    .collection("members")
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                            if (e != null) {
                                Log.e("member", e.getMessage());
                                return;
                            }

                            if (documentSnapshots != null) {
                                List<MapRoomMember> memberList = documentSnapshots.toObjects(MapRoomMember.class);
                                if (!memberList.isEmpty()) {
                                    final RecyclerView memberRecycler = navigation.findViewById(R.id.map_room_drawer_member_recycler_view);

                                    // 멤버 리사이클러
                                    memberRecycler.setLayoutManager(new LinearLayoutManager(InMapRoomActivity.this));

                                    memberRecycler.setAdapter(new MapRoomMemberFirebaseRecyclerViewAdapter(
                                            InMapRoomActivity.this, memberList));
                                }
                            }
                        }
                    });
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapRoomController = new MapRoomController(
                InMapRoomActivity.this, googleMap,
                getIntent().hasExtra("friend_name"));

        // 마커 불러오는 부분
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        String mapRoomUid = getIntent().getStringExtra("map_room_uid");

        DocumentReference currentMapRoom = db.collection("map_room").document(mapRoomUid);
        currentMapRoom.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    mapRoom = task.getResult().toObject(MapRoom.class);
                    mapRoomController.setMapRoom(mapRoom.getUid());
                    mapRoomController.setOnMapPostMarkerClickListener(new MapRoomController.
                            OnMapPostMarkerClickListener() {
                        @Override
                        public void onMapPostMarkerClick(Marker marker) {
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.map_room_container,
                                            ShowMapPostViewPagerFragment.newInstance(
                                                    mapRoom.getUid(), (String) marker.getTag()))
                                    .addToBackStack(null)
                                    .commit();
                        }
                    });

                    setToolbarTitle(mapRoom.getTitle());

                    // 드로어 로딩
                    initNavigationView();

                    //TODO: 바운드에 맞게 마커 로딩하기
                    registration = db.collection("map_room").document(mapRoom.getUid())
                            .collection("map_post_ref").limit(10).addSnapshotListener(
                                    new EventListener<QuerySnapshot>() {
                                        @Override
                                        public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                                            if (e != null) {
                                                Log.e("marker_snapshot", e.getMessage());
                                                return;
                                            }
                                            if (documentSnapshots.size() > 0) {
                                                List<MapPostReference> referenceList =
                                                        documentSnapshots.toObjects(MapPostReference.class);
                                                for (MapPostReference reference : referenceList) {
                                                    DocumentReference documentReference = reference.getMapPostReference();
                                                    documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                            if (task.isSuccessful()) {
                                                                if (task.getResult().exists()) {
                                                                    mapRoomController.initMapPostMarker(task.getResult()
                                                                            .toObject(MapPost.class));
                                                                }
                                                            }
                                                        }
                                                    });
                                                }
                                            }
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
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == getResources().getInteger(R.integer.map_post_request_code)) {
            if(resultCode == RESULT_OK) {
                mapRoomController.removeCurrentSelectedMarker();
            }
        }
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
            // TODO: setResult(~~)
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (registration != null) {
            registration.remove();
        }
    }
}
