package com.globe.hand.Setting.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.globe.hand.R;
import com.globe.hand.Setting.OnUpdateListForSettingFragmentListener;
import com.globe.hand.Setting.adapters.SettingRecyclerViewAdapter;
import com.globe.hand.models.Notice;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SettingRecyclerViewFragment extends Fragment {

    private OnUpdateListForSettingFragmentListener listener;

    public SettingRecyclerViewFragment() {
        // Required empty public constructor
    }

    public static SettingRecyclerViewFragment newInstance() {
        return new SettingRecyclerViewFragment();
    }

    RecyclerView settingRecycler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(
                R.layout.fragment_setting_recycler, container, false);
        settingRecycler = view.findViewById(R.id.list_for_setting_recycler_view);
        settingRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("notice").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            ArrayList<Notice> noticeArrayList = new ArrayList<>();
                            for(DocumentSnapshot documentSnapshot : task.getResult()) {
                                noticeArrayList.add(new Notice(
                                        documentSnapshot.getId(),
                                        (String) documentSnapshot.get("title"),
                                        (String) documentSnapshot.get("content"),
                                        documentSnapshot.getDate("create_time")
                                ));
                            }
                            settingRecycler.setAdapter(
                                    new SettingRecyclerViewAdapter(listener,
                                            getContext(), noticeArrayList));
                        } else {
                            Log.e("notice_recycler", "e-rror", task.getException());
                        }
                    }
                });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnUpdateListForSettingFragmentListener) {
            listener = (OnUpdateListForSettingFragmentListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }


}
