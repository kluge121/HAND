package com.globe.hand.Main.Tab1Map.activities.controllers;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.globe.hand.App;
import com.globe.hand.Main.Tab1Map.FindPlaceRetrofitService;
import com.globe.hand.R;
import com.globe.hand.common.RetrofitHelper;
import com.globe.hand.models.PlaceRetrofitModel;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        return new MarkerOptions().position(latLng);
    }
}
