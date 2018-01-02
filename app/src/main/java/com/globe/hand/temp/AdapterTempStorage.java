package com.globe.hand.temp;

import com.globe.hand.Main.Tab3Friend.fragment.controllers.FriendAdapter;

/**
 * Created by baeminsu on 2017. 12. 30..
 */

public class AdapterTempStorage {

    private static FriendAdapter adapter;

    public static void setAdapter(FriendAdapter adapter) {
        AdapterTempStorage.adapter = adapter;
    }

    public static FriendAdapter getAdapter() {
        return adapter;
    }
}
