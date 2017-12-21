package com.globe.hand.Setting.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.globe.hand.MapRoom.controllers.adapters.MapRoomRecyclerViewAdapter;
import com.globe.hand.R;
import com.globe.hand.models.MapRoom;
import com.globe.hand.models.Notice;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;

public class SettingPostFragment extends Fragment {

    private final static String NOTICE_DOCUMENT_ID = "notice_document_id";

    private String noticeDocumentId;

    public SettingPostFragment() {
        // Required empty public constructor
    }

    public static SettingPostFragment newInstance(String noticeDocumentId) {
        SettingPostFragment settingPostFragment
                = new SettingPostFragment();
        Bundle bundle = new Bundle();
        bundle.putString(NOTICE_DOCUMENT_ID, noticeDocumentId);
        settingPostFragment.setArguments(bundle);
        return settingPostFragment;
    }

    Notice notice;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(
                R.layout.fragment_setting_post, container, false);

        FirebaseFirestore.getInstance().collection("notice")
                .document(noticeDocumentId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        if (task.isSuccessful()) {
                            TextView textNoticeTitle = view.findViewById(R.id.text_notice_document_title);
                            TextView textNoticeCreateDate = view.findViewById(R.id.text_notice_document_date);
                            TextView textNoticeContent = view.findViewById(R.id.text_notice_document_content);
                            if (documentSnapshot != null) {
                                notice = new Notice(
                                        documentSnapshot.getId(),
                                        (String) documentSnapshot.get("title"),
                                        (String) documentSnapshot.get("content"),
                                        documentSnapshot.getDate("create_time")
                                );
                                textNoticeTitle.setText(notice.getTitle());
                                textNoticeCreateDate.setText(notice.getCreateTime().toString());
                                textNoticeContent.setText(notice.getContent());
                            } else {
                                Log.d("SettingPostFragment", "글을 찾을 수 없습니다.");
                            }
                        } else {
                            Log.e("SettingPostFragment", "error", task.getException());
                        }
                    }
                });

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            noticeDocumentId = getArguments().getString(NOTICE_DOCUMENT_ID);
        }
    }
}
