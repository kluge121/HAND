package com.globe.hand.Main.Tab1Map.activities.controllers;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.globe.hand.Main.Tab1Map.activities.MapPostActivity;
import com.globe.hand.Main.Tab1Map.activities.controllers.adapters.HandInfoWindowAdapter;
import com.globe.hand.R;
import com.globe.hand.models.MapPost;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.List;

/**
 * Created by ssangwoo on 2017-12-25.
 */

public class MapRoomController implements GoogleMap.OnMapClickListener,
        GoogleMap.OnInfoWindowClickListener,
        GoogleMap.OnMarkerClickListener {

    private Activity activity;
    private GoogleMap map;
    private HandInfoWindowAdapter handInfoWindowAdapter;

    private Marker currentMarker;
    private MapRoomMarkerFactory markerFactory;
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
        changeCurrentMarker(latLng);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        removeCurrentMarker();

        marker.showInfoWindow();
        map.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
        return true;
    }

    public void setMapRoom(String mapRoomUid) {
        this.mapRoomUid = mapRoomUid;

        handInfoWindowAdapter = new HandInfoWindowAdapter(
                activity, mapRoomUid);
        map.setInfoWindowAdapter(handInfoWindowAdapter);
    }

    private void changeCurrentMarker(LatLng latLng) {
        removeCurrentMarker();

        markerFactory = MapRoomMarkerFactory.newInstance(latLng);
        currentMarker = map.addMarker(markerFactory.newAnySelectMarkerOptions());
        currentMarker.showInfoWindow();
        map.animateCamera(CameraUpdateFactory.newLatLng(currentMarker.getPosition()));
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        if(marker.getTitle().equals("여기에 글쓰기")) {
//            removeCurrentMarker();
            Intent mapPostIntent = new Intent(activity, MapPostActivity.class);
            mapPostIntent.putExtra("map_room_latLng", marker.getPosition());
            mapPostIntent.putExtra("map_room_uid", mapRoomUid);
            activity.startActivityForResult(mapPostIntent,
                    activity.getResources().getInteger(R.integer.map_post_request_code));
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
    }

    public void initMapPostMarkers(List<MapPost> postList) {
        for(MapPost post: postList) {
            initMapPostMarker(post);
        }
    }

    public void removeCurrentMarker() {
        if(currentMarker != null) {
            currentMarker.remove();
        }
    }


    public void setOnMapPostMarkerClickListener(OnMapPostMarkerClickListener listener) {
        this.listener = listener;
    }

    public interface OnMapPostMarkerClickListener {
        void onMapPostMarkerClick(Marker marker);
    }
}
