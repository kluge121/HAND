package com.globe.hand.Main.Tab1Map.activities.controllers.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.globe.hand.App;
import com.globe.hand.Main.Tab1Map.FindPlaceRetrofitService;
import com.globe.hand.R;
import com.globe.hand.common.RetrofitHelper;
import com.globe.hand.models.PlaceRetrofitModel;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResult;
import com.google.android.gms.location.places.PlacePhotoResult;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ssangwoo on 2017-12-28.
 */

public class HandInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private Context context;
    private View view;

    public HandInfoWindowAdapter(Context context) {
        this.context = context;
        this.view = ((LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.layout_marker_hand_info_window, null);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        updateUI(marker);
        return view;
    }

    private void updateUI(Marker marker) {
        LatLng latLng = marker.getPosition();
        RetrofitHelper retrofitHelper = new RetrofitHelper(FindPlaceRetrofitService.baseUrl);
        FindPlaceRetrofitService findPlaceRetrofitService =
                retrofitHelper.getRetrofit().create(FindPlaceRetrofitService.class);
        findPlaceRetrofitService.getPlaceInfo(
                String.format("%s,%s", latLng.latitude, latLng.longitude),
                100, context.getString(R.string.google_maps_web_api_key))
                .enqueue(new Callback<PlaceRetrofitModel>() {
                    @Override
                    public void onResponse(Call<PlaceRetrofitModel> call,
                                           Response<PlaceRetrofitModel> response) {
                        if (response.isSuccessful()) {
                            PlaceRetrofitModel model = response.body();
                            if (model.getStatus().equals("OK")) {
                                String placeId = model.getResults().get(0).getPlaceId();
                                Places.GeoDataApi.getPlaceById(App.getGoogleApiHelper().getGoogleApiClient(),
                                        placeId).setResultCallback(new ResultCallback<PlaceBuffer>() {
                                    @Override
                                    public void onResult(@NonNull PlaceBuffer places) {
                                        if (places.getStatus().isSuccess() && places.getCount() > 0) {
                                            RelativeLayout loadingLayout = view.findViewById(R.id.hand_info_window_loading);
                                            RelativeLayout infoWindowContainer = view.findViewById(R.id.hand_info_window_container);
                                            loadingLayout.setVisibility(View.GONE);
                                            infoWindowContainer.setVisibility(View.VISIBLE);

                                            Place place = places.get(0);
                                            TextView placeName = view.findViewById(R.id.hand_info_window_name);
                                            placeName.setText(place.getName().toString());
                                        } else {
                                            Log.e("Place", "Place not found");
                                        }
                                        places.release();
                                    }
                                });
                                Places.GeoDataApi.getPlacePhotos(App.getGoogleApiHelper().getGoogleApiClient(),
                                        placeId).setResultCallback(new ResultCallback<PlacePhotoMetadataResult>() {
                                    @Override
                                    public void onResult(@NonNull PlacePhotoMetadataResult placePhotoMetadataResult) {
                                        if(placePhotoMetadataResult.getStatus().isSuccess()) {
                                            PlacePhotoMetadataBuffer buffer = placePhotoMetadataResult.getPhotoMetadata();
                                            final ImageView imagePhoto = view.findViewById(R.id.hand_info_window_photo);
                                            if (buffer.getCount() > 0) {
                                                buffer.get(0)
                                                        .getScaledPhoto(App.getGoogleApiHelper().getGoogleApiClient(),
                                                                imagePhoto.getWidth(), imagePhoto.getHeight())
                                                        .setResultCallback(new ResultCallback<PlacePhotoResult>() {
                                                            @Override
                                                            public void onResult(@NonNull PlacePhotoResult placePhotoResult) {
                                                                if(placePhotoResult.getStatus().isSuccess()) {
                                                                    imagePhoto.setImageBitmap(placePhotoResult.getBitmap());
                                                                }
                                                            }
                                                        });
                                                buffer.release();
                                            }
                                        }
                                    }
                                });
                            } else {
                                Log.e("Place Status", "error - " + model.getStatus());
                            }
                        } else {
                            Log.e("Place Response", "error - " + response.raw().toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<PlaceRetrofitModel> call, Throwable throwable) {
                        Log.e("Place", throwable.getMessage());
                    }
                });
    }
}
