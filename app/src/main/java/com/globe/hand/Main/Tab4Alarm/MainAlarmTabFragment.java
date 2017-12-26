package com.globe.hand.Main.Tab4Alarm;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.globe.hand.R;


public class MainAlarmTabFragment extends Fragment {

    RecyclerView recyclerView;
    TabLayout tabLayout;

    public static MainAlarmTabFragment newInstance() {
        return new MainAlarmTabFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main_alarm_tab,container,false);

        recyclerView = v.findViewById(R.id.alarm_tab_recyclerview);
        tabLayout = v.findViewById(R.id.alarm_tab_tablayout);


        return v;
    }



}
