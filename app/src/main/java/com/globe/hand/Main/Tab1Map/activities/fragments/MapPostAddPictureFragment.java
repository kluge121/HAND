package com.globe.hand.Main.Tab1Map.activities.fragments;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.globe.hand.R;
import com.globe.hand.common.HandPermission;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

import static com.globe.hand.common.HandPermission.PICK_FROM_ALBUM;

public class MapPostAddPictureFragment extends Fragment {

    private HandPermission handPermission;

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReference();

    private LinearLayout emptyContainer;
    private ImageView mapPostPicture;

    private OnUploadMapPostPictureListener listener;

    public MapPostAddPictureFragment() {
        // Required empty public constructor
    }

    public static MapPostAddPictureFragment newInstance() {
        return new MapPostAddPictureFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map_post_add_picture,
                container, false);

        emptyContainer = view.findViewById(R.id.map_post_picture_empty_container);
        mapPostPicture = view.findViewById(R.id.image_map_post_picture);

        emptyContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handPermission.checkPhotoPermission();
            }
        });

        mapPostPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 사진 수정
            }
        });

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handPermission = new HandPermission(getContext(), this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PICK_FROM_ALBUM:
                //이미지 경로, 이름
                if (data == null) break;
                Uri selectImageUri = data.getData();

                String imageName = handPermission.getImageNameToUri(data.getData());
                Bitmap resized = handPermission.makeBitmap(selectImageUri);
                String imgUpLoadPath = handPermission.saveBitmapToJpeg(
                        resized, "resizeTmp", imageName);

                if (imgUpLoadPath != null) {
                    Uri file = Uri.fromFile(new File(imgUpLoadPath));
                    StorageReference mapPhotoImgRef = storageRef.child("map_post_image/" + user.getUid());
                    mapPhotoImgRef.putFile(file)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Uri profile_URI = taskSnapshot.getDownloadUrl();
                            if (profile_URI != null) {
                                Log.e("포스트사진", profile_URI.toString());
                                listener.onUploadMapPostPicture(profile_URI.toString());
                            }
                        }
                    });
                }

                emptyContainer.setVisibility(View.GONE);
                mapPostPicture.setVisibility(View.VISIBLE);
                mapPostPicture.setImageBitmap(resized);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnUploadMapPostPictureListener) {
            this.listener = (OnUploadMapPostPictureListener) context;
        }
    }

    public interface OnUploadMapPostPictureListener {
        void onUploadMapPostPicture(String imageUrl);
    }
}
