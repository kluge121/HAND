package com.globe.hand.Main.Tab1Map.activities.controllers;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.globe.hand.Main.Tab1Map.activities.MapPostActivity;
import com.globe.hand.Main.Tab1Map.activities.ShowMapPostActivity;
import com.globe.hand.Main.Tab1Map.activities.controllers.adapters.HandInfoWindowAdapter;
import com.globe.hand.R;
import com.globe.hand.models.MapPost;
import com.globe.hand.models.MapRoom;
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

    public void addAnySelectMapPostMarker(LatLng latLng) {
        changeCurrentMarker(latLng);
    }

    public Marker newMapPostMarker(LatLng latLng, String title, String content) {
        MapRoomMarkerFactory mapPostMarkerFactory =
                MapRoomMarkerFactory.newInstance(latLng);
        return map.addMarker(mapPostMarkerFactory.newMapPostMarkerOptions(title, content));
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
            Intent mapPostIntent = new Intent(activity, ShowMapPostActivity.class);
            mapPostIntent.putExtra("map_room_latLng", marker.getPosition());
            mapPostIntent.putExtra("map_room_uid", mapRoomUid);
            activity.startActivityForResult(mapPostIntent,
                    activity.getResources().getInteger(R.integer.map_show_request_code));
        }
    }

    public void initMapPostMarkers(List<MapPost> postList) {
        for(MapPost post: postList) {
            LatLng latLng = new LatLng(post.getGeoPoint().getLatitude(),
                    post.getGeoPoint().getLongitude());
            newMapPostMarker(latLng, post.getTitle(), post.getContent());
        }
    }

    public void removeCurrentMarker() {
        if(currentMarker != null) {
            currentMarker.remove();
        }

    }
}
