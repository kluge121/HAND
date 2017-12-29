package com.globe.hand.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ssangwoo on 2017-12-28.
 */

public class PlaceRetrofitModel {

    @SerializedName("results")
    List<Results> results;

    @SerializedName("status")
    String status;

    public PlaceRetrofitModel() {}

    public List<Results> getResults() {
        return results;
    }

    public String getStatus() {
        return status;
    }

    public static class Results {
        @SerializedName("place_id")
        String placeId;

        public String getPlaceId() {
            return placeId;
        }
    }
}
