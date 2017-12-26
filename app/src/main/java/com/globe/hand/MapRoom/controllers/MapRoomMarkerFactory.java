package com.globe.hand.MapRoom.controllers;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by ssangwoo on 2017-12-25.
 */

public class MapRoomMarkerFactory {

    private LatLng latLng;

    private MapRoomMarkerFactory(LatLng latLng) {
        this.latLng = latLng;
    }

    public static MapRoomMarkerFactory newInstance(LatLng latLng) {
        return new MapRoomMarkerFactory(latLng);
    }


    public MarkerOptions newPreMarkerOptions() {
        //TODO : 커스텀 마커 만들기
        return new MarkerOptions().position(latLng).title("글쓰기");
    }
}
