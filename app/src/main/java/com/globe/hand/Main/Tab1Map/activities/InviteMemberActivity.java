package com.globe.hand.Main.Tab1Map.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;

import com.globe.hand.Main.Tab1Map.activities.controllers.adapters.InviteMemberRecyclerViewAdapter;
import com.globe.hand.R;
import com.globe.hand.common.BaseActivity;
import com.globe.hand.common.RecyclerViewEmptySupport;
import com.globe.hand.enums.MapRoomPermission;
import com.globe.hand.models.JoinedMapRooms;
import com.globe.hand.models.MapRoomMember;
import com.globe.hand.models.UploadUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class InviteMemberActivity extends BaseActivity
        implements InviteMemberRecyclerViewAdapter.OnFriendCheckListener {

    String mapRoomUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_member);

        setToolbar(R.id.invite_member_toolbar, true);
        setToolbarTitle("친구 초대");

        mapRoomUid = getIntent().getStringExtra("map_room_uid");

        final RecyclerViewEmptySupport friendRecycler = findViewById(R.id.invite_member_recycler_view);
        friendRecycler.setLayoutManager(new LinearLayoutManager(InviteMemberActivity.this));
        friendRecycler.setEmptyView(findViewById(R.id.invite_member_empty_view));
        FirebaseFirestore.getInstance().collection("user")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("friend").limit(10).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<UploadUser> uploadUserList =
                                    task.getResult().toObjects(UploadUser.class);
                            friendRecycler.setAdapter(new InviteMemberRecyclerViewAdapter(
                                    InviteMemberActivity.this, uploadUserList,
                                    InviteMemberActivity.this));
                        }
                    }
                });
    }

    private ArrayList<UploadUser> uploadUserArrayList = new ArrayList<>();

    @Override
    public void onFriendCheck(UploadUser user, boolean isSelectedFriend) {
        if(isSelectedFriend) {
            uploadUserArrayList.add(user);
        } else {
            uploadUserArrayList.remove(user);
        }
        onPrepareOptionsMenu(optionsMenu);
    }

    private Menu optionsMenu;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_plus_friend, menu);
        optionsMenu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_plus_friend).setVisible(!uploadUserArrayList.isEmpty());
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_plus_friend) {
            for(final UploadUser user: uploadUserArrayList) {
                final FirebaseFirestore db = FirebaseFirestore.getInstance();
                final DocumentReference currentMapRomReference
                        = db.collection("map_room").document(mapRoomUid);
                currentMapRomReference.collection("members")
                        .document(user.getUid())
                        .set(new MapRoomMember(user.getUserRef(),
                                MapRoomPermission.MEMBER.name(), mapRoomUid))
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    db.collection("map_room")
                                            .document(user.getUid())
                                            .collection("joined_map_rooms")
                                            .add(new JoinedMapRooms(currentMapRomReference));
                                    setResult(RESULT_OK);
                                    finish();
                                }
                            }
                        });
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
