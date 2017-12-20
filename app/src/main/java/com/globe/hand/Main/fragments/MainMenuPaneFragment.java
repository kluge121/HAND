package com.globe.hand.Main.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.globe.hand.R;
import com.globe.hand.Setting.SettingActivity;
import com.globe.hand.enums.MenuPane;

public class MainMenuPaneFragment extends Fragment
        implements View.OnClickListener {
    private OnMenuPaneClickListener mListener;

    public MainMenuPaneFragment() {
        // Required empty public constructor
    }

    public static MainMenuPaneFragment newInstance() {
        return new MainMenuPaneFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(
                R.layout.fragment_main_menu_pane, container, false);
        view.findViewById(R.id.button_map).setOnClickListener(this);
        view.findViewById(R.id.button_friend).setOnClickListener(this);
        view.findViewById(R.id.button_event).setOnClickListener(this);
        view.findViewById(R.id.button_setting).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        if(view.getId() == R.id.button_map) {
            if (mListener != null) {
                mListener.OnMenuPaneClick(MenuPane.MAP);
            }
        } else if(view.getId() == R.id.button_friend) {
            if (mListener != null) {
                mListener.OnMenuPaneClick(MenuPane.FRIEND);
            }
        } else if(view.getId() == R.id.button_event) {
            if (mListener != null) {
                mListener.OnMenuPaneClick(MenuPane.EVENT);
            }
        } else if(view.getId() == R.id.button_setting) {
            intent = new Intent(getContext(), SettingActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMenuPaneClickListener) {
            mListener = (OnMenuPaneClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnMapRoomInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnMenuPaneClickListener {
        void OnMenuPaneClick(MenuPane menuPane);
    }
}
