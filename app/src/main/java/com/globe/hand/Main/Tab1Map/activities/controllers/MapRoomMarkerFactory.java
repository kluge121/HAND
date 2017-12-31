package com.globe.hand.Main.Tab1Map.activities.controllers;

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

    public MarkerOptions newAnySelectMarkerOptions() {
        return new MarkerOptions().position(latLng).title("여기에 글쓰기");
    }

    public MarkerOptions newMapPostMarkerOptions(String title, String content) {
        int contentEndIndex = content.length() <= 15 ? content.length(): 15;
        return new MarkerOptions().position(latLng)
                .title(title).snippet(content.substring(0, contentEndIndex) + "...");
    }
}
