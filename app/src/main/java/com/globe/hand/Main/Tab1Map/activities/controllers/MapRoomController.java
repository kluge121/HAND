package com.globe.hand.Main.Tab1Map.activities.controllers;

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

    private Fragment fragment;
    private GoogleMap map;
    private HandInfoWindowAdapter handInfoWindowAdapter;

    private Marker currentMarker;
    private MapRoomMarkerFactory markerFactory;

    private String mapRoomUid;

    public MapRoomController(Fragment fragment, GoogleMap map) {
        this.fragment = fragment;
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
        markerFactory = MapRoomMarkerFactory.newInstance(latLng);

        changeCurrentMarker();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if(currentMarker != null) {
            currentMarker.remove();
        }
        marker.showInfoWindow();
        map.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
        return true;
    }

    public void setMapRoom(String mapRoomUid) {
        this.mapRoomUid = mapRoomUid;

        handInfoWindowAdapter = new HandInfoWindowAdapter(
                fragment.getContext(), mapRoomUid);
        map.setInfoWindowAdapter(handInfoWindowAdapter);
    }

    public void addMapPostMarker(LatLng latLng) {
//        newMapPostMarker(latLng);
    }

    public void newMapPostMarker(MapPost post) {
        LatLng latLng = new LatLng(post.getGeoPoint().getLatitude(),
                post.getGeoPoint().getLongitude());
        MapRoomMarkerFactory mapPostMarkerFactory =
                MapRoomMarkerFactory.newInstance(latLng);
        map.addMarker(mapPostMarkerFactory.newMapPostMarkerOptions(
                post.getTitle(), post.getContent()));
    }

    private void changeCurrentMarker() {
        if(currentMarker != null) {
            currentMarker.remove();
        }
        currentMarker = map.addMarker(markerFactory.newAnySelectMarkerOptions());
        currentMarker.showInfoWindow();
        map.animateCamera(CameraUpdateFactory.newLatLng(currentMarker.getPosition()));
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        if(marker.getTitle().equals("여기에 글쓰기")) {
            Intent mapPostIntent = new Intent(fragment.getContext(), MapPostActivity.class);
            mapPostIntent.putExtra("map_room_latLng", marker.getPosition());
            mapPostIntent.putExtra("map_room_uid", mapRoomUid);
            fragment.startActivityForResult(mapPostIntent,
                    fragment.getResources().getInteger(R.integer.map_post_request_code));
        } else {
            //TODO: 글쓴거 보여주기
            Intent mapPostIntent = new Intent(fragment.getContext(), ShowMapPostActivity.class);
            mapPostIntent.putExtra("map_room_latLng", marker.getPosition());
            mapPostIntent.putExtra("map_room_uid", mapRoomUid);
            fragment.startActivityForResult(mapPostIntent,
                    fragment.getResources().getInteger(R.integer.map_show_request_code));
        }
    }

    public void initMapPostMarkers(List<MapPost> postList) {
        for(MapPost post: postList) {
            newMapPostMarker(post);
        }
    }
}
