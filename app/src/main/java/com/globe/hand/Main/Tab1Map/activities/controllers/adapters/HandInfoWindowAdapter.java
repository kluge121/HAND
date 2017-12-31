package com.globe.hand.Main.Tab1Map.activities.controllers.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.globe.hand.App;
import com.globe.hand.Main.Tab1Map.FindPlaceRetrofitService;
import com.globe.hand.R;
import com.globe.hand.common.RetrofitHelper;
import com.globe.hand.models.MapPost;
import com.globe.hand.models.MapRoom;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QuerySnapshot;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ssangwoo on 2017-12-28.
 */

public class HandInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private Context context;
    private String mapRoomUid;

    public HandInfoWindowAdapter(Context context, String mapRoomUid) {
        this.context = context;
        this.mapRoomUid = mapRoomUid;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.layout_marker_hand_info_window, null);
        updateUI(view, marker);
        return view;
    }

    private void updateUI(final View view, final Marker marker) {
        if (!marker.getTitle().equals("여기에 글쓰기")) {
            RelativeLayout anySelectLayout =
                    view.findViewById(R.id.hand_info_window_any_select_container);
            RelativeLayout mapPostLayout =
                    view.findViewById(R.id.hand_info_window_map_post_container);
            anySelectLayout.setVisibility(View.GONE);
            mapPostLayout.setVisibility(View.VISIBLE);

            TextView textTitle = view.findViewById(R.id.hand_info_window_title);
            textTitle.setText(marker.getTitle());
            TextView textContent = view.findViewById(R.id.hand_info_window_content);
            textContent.setText(marker.getSnippet());

//            RetrofitHelper retrofitHelper = new RetrofitHelper(FindPlaceRetrofitService.baseUrl);
//            FindPlaceRetrofitService findPlaceRetrofitService =
//                    retrofitHelper.getRetrofit().create(FindPlaceRetrofitService.class);
//            findPlaceRetrofitService.getPlaceInfo(
//                    String.format("%s,%s", marker.getPosition().latitude,
//                            marker.getPosition().longitude),
//                    100, context.getString(R.string.google_maps_web_api_key))
//                    .enqueue(new Callback<PlaceRetrofitModel>() {
//                        @Override
//                        public void onResponse(Call<PlaceRetrofitModel> call,
//                                               Response<PlaceRetrofitModel> response) {
//                            if (response.isSuccessful()) {
//                                PlaceRetrofitModel model = response.body();
//                                if (model.getStatus().equals("OK")) {
//                                    RelativeLayout placeLayout =
//                                            view.findViewById(R.id.hand_info_window_place_container);
//                                    placeLayout.setVisibility(View.VISIBLE);
//
//                                    String placeId = model.getResults().get(0).getPlaceId();
//                                    Places.GeoDataApi.getPlaceById(App.getGoogleApiHelper().getGoogleApiClient(),
//                                            placeId).setResultCallback(new ResultCallback<PlaceBuffer>() {
//                                        @Override
//                                        public void onResult(@NonNull PlaceBuffer places) {
//                                            if (places.getStatus().isSuccess() && places.getCount() > 0) {
//
//                                                Place place = places.get(0);
//                                                TextView placeName = view.findViewById(R.id.hand_info_window_place_name);
//                                                placeName.setText(place.getName().toString());
//                                            } else {
//                                                Log.e("Place", "Place not found");
//                                            }
//                                            places.release();
//                                        }
//                                    });
//                                    Places.GeoDataApi.getPlacePhotos(App.getGoogleApiHelper().getGoogleApiClient(),
//                                            placeId).setResultCallback(new ResultCallback<PlacePhotoMetadataResult>() {
//                                        @Override
//                                        public void onResult(@NonNull PlacePhotoMetadataResult placePhotoMetadataResult) {
//                                            if (placePhotoMetadataResult.getStatus().isSuccess()) {
//                                                PlacePhotoMetadataBuffer buffer = placePhotoMetadataResult.getPhotoMetadata();
//                                                final ImageView imagePhoto = view.findViewById(R.id.hand_info_window_place_photo);
//                                                if (buffer.getCount() > 0) {
//                                                    buffer.get(0)
//                                                            .getPhoto(App.getGoogleApiHelper().getGoogleApiClient())
//                                                            .setResultCallback(new ResultCallback<PlacePhotoResult>() {
//                                                                @Override
//                                                                public void onResult(@NonNull PlacePhotoResult placePhotoResult) {
//                                                                    if (placePhotoResult.getStatus().isSuccess()) {
//                                                                        imagePhoto.setImageBitmap(placePhotoResult.getBitmap());
//                                                                    }
//                                                                }
//                                                            });
//                                                    buffer.release();
//                                                }
//                                            }
//                                        }
//                                    });
//                                } else {
//                                    Log.e("Place Status", "error - " + model.getStatus());
//                                }
//                            } else {
//                                Log.e("Place Response", "error - " + response.raw().toString());
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<PlaceRetrofitModel> call, Throwable throwable) {
//                            Log.e("Place", throwable.getMessage());
//                        }
//                    });
        }

//        LatLng latLng = marker.getPosition();
//        GeoPoint geoPoint = new GeoPoint(latLng.latitude, latLng.longitude);
//        final FirebaseFirestore db = FirebaseFirestore.getInstance();
//        db.collection("map_room").document(mapRoomUid)
//                .collection("map_post").whereEqualTo("geoPoint", geoPoint).get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            if (task.getResult().getDocuments().isEmpty()) {
//                                RelativeLayout anySelectLayout =
//                                        view.findViewById(R.id.hand_info_window_any_select_container);
//                                anySelectLayout.setVisibility(View.VISIBLE);
//                            } else {
////                            for(DocumentSnapshot documentSnapshot: task.getResult().getDocuments()) {
//                                RelativeLayout mapPostLayout =
//                                        view.findViewById(R.id.hand_info_window_map_post_container);
//                                mapPostLayout.setVisibility(View.VISIBLE);
//
//                                MapPost mapPost = task.getResult().getDocuments()
//                                        .get(0).toObject(MapPost.class);
//
//                                TextView textTitle = view.findViewById(R.id.hand_info_window_title);
//                                textTitle.setText(mapPost.getTitle());
//                                TextView textContent = view.findViewById(R.id.hand_info_window_content);
//                                textContent.setText(mapPost.getContent());
////                            }
//                            }
//                        } else {
//                            RelativeLayout anySelectLayout =
//                                    view.findViewById(R.id.hand_info_window_any_select_container);
//                            anySelectLayout.setVisibility(View.VISIBLE);
//                        }
//                    }
//                });
    }
}
