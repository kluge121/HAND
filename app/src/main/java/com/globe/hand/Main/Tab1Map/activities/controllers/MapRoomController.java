package com.globe.hand.Main.Tab1Map.activities.controllers;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.Fragment;

import com.globe.hand.Main.Tab1Map.activities.WriteActivity;
import com.globe.hand.Main.Tab1Map.activities.controllers.adapters.HandInfoWindowAdapter;
import com.globe.hand.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by ssangwoo on 2017-12-25.
 */

public class MapRoomController implements GoogleMap.OnMapClickListener,
        GoogleMap.OnInfoWindowClickListener {

    private Fragment fragment;
    private GoogleMap map;

    private HandInfoWindowAdapter handInfoWindowAdapter;

    private Marker currentMarker;
    private MapRoomMarkerFactory markerFactory;

    public MapRoomController(Fragment fragment, GoogleMap map) {
        this.fragment = fragment;
        this.map = map;
    }

    public void initialize(LatLng latLng) {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));

        handInfoWindowAdapter = new HandInfoWindowAdapter(fragment.getContext());

        map.setInfoWindowAdapter(handInfoWindowAdapter);
        map.setOnMapClickListener(this);
        map.setOnInfoWindowClickListener(this);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        markerFactory = MapRoomMarkerFactory.newInstance(latLng);

        changeCurrentMarker();

        map.animateCamera(CameraUpdateFactory.newLatLng(currentMarker.getPosition()));
    }

    private void changeCurrentMarker() {
        if(currentMarker != null) {
            currentMarker.remove();
        }
        currentMarker = map.addMarker(markerFactory.newPreMarkerOptions());
        currentMarker.showInfoWindow();
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        if(marker.getTitle().equals("여기에 글쓰기")) {
            Intent writeIntent = new Intent(fragment.getContext(), WriteActivity.class);
            fragment.startActivityForResult(writeIntent,
                    fragment.getResources().getInteger(R.integer.write_request_code));
        } else {
            //TODO: 글쓴거 보여주기
        }
    }
}
