package com.globe.hand.Main.Tab1Map.activities.controllers;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;

import com.globe.hand.Main.Tab1Map.activities.MapPostActivity;
import com.globe.hand.Main.Tab1Map.activities.controllers.adapters.HandInfoWindowAdapter;
import com.globe.hand.R;
import com.globe.hand.models.MapPost;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

/**
 * Created by ssangwoo on 2017-12-25.
 */

public class MapRoomController implements GoogleMap.OnMapClickListener,
        GoogleMap.OnInfoWindowClickListener,
        GoogleMap.OnMarkerClickListener {

    private Activity activity;
    private GoogleMap map;

    private Marker currentAnySelectMarker;
    private Marker selectedMarker;

    private OnMapPostMarkerClickListener listener;

    private String mapRoomUid;

    public MapRoomController(Activity activity, GoogleMap map) {
        this.activity = activity;
        this.map = map;
    }

    public void initialize(LatLng latLng) {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));

        map.setOnMapClickListener(this);
        map.setOnMarkerClickListener(this);
        map.setOnInfoWindowClickListener(this);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        addAnySelectedMarker(latLng);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        removeCurrentSelectedMarker();

        changeSelectedMarkerIcon(marker);

        map.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
        return true;
    }

    public void setMapRoom(String mapRoomUid) {
        this.mapRoomUid = mapRoomUid;

        HandInfoWindowAdapter handInfoWindowAdapter = new HandInfoWindowAdapter(
                activity, mapRoomUid);
        map.setInfoWindowAdapter(handInfoWindowAdapter);
    }

    private void addAnySelectedMarker(LatLng latLng) {
        removeCurrentSelectedMarker();

        MapRoomMarkerFactory markerFactory = MapRoomMarkerFactory.newInstance(latLng);
        currentAnySelectMarker = map.addMarker(markerFactory.newAnySelectMarkerOptions());

        changeSelectedMarkerIcon(currentAnySelectMarker);


        map.animateCamera(CameraUpdateFactory.newLatLng(currentAnySelectMarker.getPosition()));
    }

    @Override
    public void onInfoWindowClick(final Marker marker) {
        if(marker.getTitle().equals("여기에 글쓰기")) {
//            removeCurrentSelectedMarker();
            FirebaseFirestore.getInstance()
                    .collection("map_room").document(mapRoomUid)
                    .collection("category").get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()) {
                                if (!task.getResult().isEmpty()) {
                                    Intent mapPostIntent = new Intent(activity, MapPostActivity.class);
                                    mapPostIntent.putExtra("map_room_latLng", marker.getPosition());
                                    mapPostIntent.putExtra("map_room_uid", mapRoomUid);
                                    activity.startActivityForResult(mapPostIntent,
                                            activity.getResources().getInteger(R.integer.map_post_request_code));
                                    return;
                                }
                            }
                            new AlertDialog.Builder(activity)
                                    .setMessage("하나 이상의 카테고리가 필요합니다!")
                                    .setNegativeButton(R.string.dialog_close, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                        }
                                    }).show();
                        }
                    });
        } else {
            //TODO: 글쓴거 보여주기
            if(listener != null) {
                listener.onMapPostMarkerClick(marker);
            }
        }
    }

    public void initMapPostMarker(MapPost mapPost) {
        LatLng latLng = new LatLng(mapPost.getGeoPoint().getLatitude(),
                mapPost.getGeoPoint().getLongitude());

        MapRoomMarkerFactory mapPostMarkerFactory =
                MapRoomMarkerFactory.newInstance(latLng);
        Marker marker = map.addMarker(mapPostMarkerFactory
                .newMapPostMarkerOptions(mapPost.getTitle(), mapPost.getContent()));
        marker.setTag(mapPost.getUid());
        marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.hand_marker));
    }

    public void initMapPostMarkers(List<MapPost> postList) {
        for(MapPost post: postList) {
            initMapPostMarker(post);
        }
    }

    public void removeCurrentSelectedMarker() {
        if(selectedMarker != null) {
            if(selectedMarker.equals(currentAnySelectMarker)) {
                selectedMarker = null;
            }
        }
        if(currentAnySelectMarker != null) {
            currentAnySelectMarker.remove();
        }
    }

    private void changeSelectedMarkerIcon(Marker marker) {
        //TODO : 아이콘 바꾸기(오류해결해야함)
        if(selectedMarker != null) {
            selectedMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.hand_marker));
        }
        selectedMarker = marker;
        selectedMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.hand_selected_marker));
        selectedMarker.showInfoWindow();
    }


    public void setOnMapPostMarkerClickListener(OnMapPostMarkerClickListener listener) {
        this.listener = listener;
    }

    public interface OnMapPostMarkerClickListener {
        void onMapPostMarkerClick(Marker marker);
    }
}
