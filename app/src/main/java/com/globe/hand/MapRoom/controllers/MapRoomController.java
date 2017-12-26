package com.globe.hand.MapRoom.controllers;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.globe.hand.MapRoom.WriteActivity;
import com.globe.hand.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by ssangwoo on 2017-12-25.
 */

public class MapRoomController implements GoogleMap.OnMapClickListener,
        GoogleMap.OnInfoWindowClickListener {

    private Fragment fragment;
    private GoogleMap map;
    private Marker currentMarker;

    public MapRoomController(Fragment fragment, GoogleMap map) {
        this.fragment = fragment;
        this.map = map;
    }

    public void initialize(LatLng latLng) {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));

        map.setOnMapClickListener(this);
        map.setOnInfoWindowClickListener(this);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        MapRoomMarkerFactory markerFactory = MapRoomMarkerFactory.newInstance(latLng);
        if(currentMarker != null) {
            currentMarker.remove();
        }
        currentMarker = map.addMarker(markerFactory.newPreMarkerOptions());
        currentMarker.showInfoWindow();
        map.animateCamera(CameraUpdateFactory.newLatLng(currentMarker.getPosition()));
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        if(marker.getTitle().equals("글쓰기")) {
            Intent writeIntent = new Intent(fragment.getContext(), WriteActivity.class);
            writeIntent.putExtra("place_id", marker.getId());
            fragment.startActivityForResult(writeIntent,
                    fragment.getResources().getInteger(R.integer.write_request_code));
        } else {

        }
        //TODO: 마커 정보 보여주기
    }
}
