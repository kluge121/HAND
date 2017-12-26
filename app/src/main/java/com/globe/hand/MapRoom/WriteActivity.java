package com.globe.hand.MapRoom;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.globe.hand.App;
import com.globe.hand.R;
import com.globe.hand.common.BaseActivity;
import com.globe.hand.common.RetrofitHelper;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResult;
import com.google.android.gms.location.places.PlacePhotoResult;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WriteActivity extends BaseActivity {

    private Place currentPlace;

    private TextView textPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        setToolbar(R.id.write_toolbar, true);

        textPlace = findViewById(R.id.text_write_place);

        if(getIntent().hasExtra("place_id")) {
            String placeId = getIntent().getStringExtra("place_id");
            getPlace(placeId);
//            getPlace(placeId);
        }
    }

//    private Place getPlace(LatLng latLng) {
//        Place place = null;
//
//        RetrofitHelper retrofitHelper = new RetrofitHelper(FindPlaceRetrofitService.baseUrl);
//        FindPlaceRetrofitService findPlaceRetrofitService =
//                retrofitHelper.getRetrofit().create(FindPlaceRetrofitService.class);
//        findPlaceRetrofitService.getPlaceInfo(
//                String.format("%s,%s", latLng.latitude, latLng.longitude),
//                500, getString(R.string.google_maps_key))
//                .enqueue(new Callback<Place>() {
//                    @Override
//                    public void onResponse(Call<Place> call, Response<Place> response) {
//                        if(response.isSuccessful()) {
//                            = response.body();
//                            source.setResult(firebaseAuthToken.getFirebaseToken());
//                        } else {
//                            Log.e("LoginActivity11", "error - " + response.raw().toString());
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<Place> call, Throwable throwable) {
//                        Log.e("WriteActivity", throwable.getMessage());
//                    }
//                });
//        return place;
//    }

    private void getPlace(String placeId) {
        // TODO: PLACE ID 받아오게 REST 루다가 보내야함
        Places.GeoDataApi.getPlaceById(App.getGoogleApiHelper().getGoogleApiClient(),
                placeId).setResultCallback(new ResultCallback<PlaceBuffer>() {
            @Override
            public void onResult(@NonNull PlaceBuffer places) {
                if (places.getStatus().isSuccess() && places.getCount() > 0) {
                    currentPlace = places.get(0);
                    textPlace.setText(currentPlace.getName());
                } else {
                    Log.e("WriteActivity", "Place not found");
                }
                places.release();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_write_done, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_wirte_done) {
            setResult(RESULT_OK);
            startActivity(new Intent(this, RealMapActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("경고")
                .setMessage("작성한 내용이 모두 사라집니다! 정말 지도 화면으로 가시겠습니까?")
                .setPositiveButton(R.string.dialog_back, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        WriteActivity.super.onBackPressed();
                        dialogInterface.dismiss();
                    }
                }).setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
        }).show();
    }
}
