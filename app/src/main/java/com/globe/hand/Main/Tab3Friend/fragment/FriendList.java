package com.globe.hand.Main.Tab3Friend.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.globe.hand.Main.Tab1Map.controllers.adapters.MapRoomFirebaseRecyclerViewAdapter;
import com.globe.hand.Main.Tab3Friend.fragment.controllers.FriendAdapter;
import com.globe.hand.Main.Tab3Friend.fragment.controllers.RequestViewHolder;
import com.globe.hand.PhotoPreview.ProfilePhotoPreView;
import com.globe.hand.R;
import com.globe.hand.common.RecyclerViewDecoration;
import com.globe.hand.models.User;
import com.globe.hand.temp.AdapterTempStorage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class FriendList extends Fragment {


    private final int PERMISSION_REQUEST_CODE = 100;
    private final int PICK_FROM_ALBUM = 90;
    String imgPath;
    String absolutePath;
    static String imgUpLoadPath = "";

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReference();

    public static FriendList newInstance() {
        return new FriendList();
    }


    private RecyclerView recyclerView;
    private CircleImageView myUserProfileImage;
    private TextView myUserName;
    private TextView myUserEmail;
    private final String TAG = "FriendListActivity";
    private FriendAdapter adapter;
    private FirebaseUser loginUser;
    private ListenerRegistration registration;
    private AlertDialog.Builder dialogBuilder;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_friend_list, container, false);


        final CharSequence[] items = {"기본이미지로 변경", "앨범에서 사진선택", "프로필사진 보기"};

        dialogBuilder = new AlertDialog.Builder(getContext());


        dialogBuilder.setTitle("프로필사진 변경");
        dialogBuilder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (which == 2) {
                    db.collection("user").document(loginUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                User tmp = task.getResult().toObject(User.class);
                                if (tmp.getProfile_url() != null) {
                                    Intent intent = new Intent(getContext(), ProfilePhotoPreView.class);
                                    intent.putExtra("url", tmp.getProfile_url());
                                    startActivity(intent);
                                } else {

                                    Toast.makeText(getContext(), "프로필을 설정해 주세요!", Toast.LENGTH_SHORT).show();
                                }

                            }


                        }
                    });

                } else if (which == 0) { // 프로필 기본이미지로

                    loginUser.updateProfile(new UserProfileChangeRequest.Builder()
                            .setPhotoUri(null).build()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {

                                db.collection("user").document(loginUser.getUid()).update("profile_url", null).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {


                                            StorageReference profileImgRef = storageRef.child("profile_image/" + loginUser.getUid());
                                            profileImgRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    myUserProfileImage.setImageResource(R.drawable.com_facebook_profile_picture_blank_portrait);
                                                    Toast.makeText(getContext(), "프로필 사진이 변경되었습니다", Toast.LENGTH_SHORT).show();
                                                }
                                            });


                                        }
                                    }
                                });

                            }
                        }
                    });


                } else if (which == 1) { // 앨범에서 선택한 사진으
                    checkPermission();
                }
            }
        });


        recyclerView = v.findViewById(R.id.friend_recyclerview);


        AdapterTempStorage.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new RecyclerViewDecoration(0, 30));
        recyclerView.setNestedScrollingEnabled(false);

        db = FirebaseFirestore.getInstance();
        loginUser = FirebaseAuth.getInstance().getCurrentUser();

        myUserProfileImage = v.findViewById(R.id.my_user_profile);
        myUserName = v.findViewById(R.id.my_user_name);
        myUserEmail = v.findViewById(R.id.my_user_email);


        if (loginUser.getPhotoUrl() != null)
            Glide.with(getContext()).load(loginUser.getPhotoUrl()).into(myUserProfileImage);
        myUserName.setText(loginUser.getDisplayName());
        myUserEmail.setText(loginUser.getEmail());

        myUserProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder.show();


            }
        });


        new AyncGetFriend().execute();

        return v;
    }


    private class AyncGetFriend extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... strings) {

            registration =
                    db.collection("user").document(loginUser.getUid()).
                            collection("friend").addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                            if (e != null) {
                                Log.e("map_room_snapshot", e.getMessage());
                                return;
                            }
                            recyclerView.setAdapter(new FriendAdapter(
                                    getContext(), documentSnapshots.getDocuments()));

                        }
                    });
            return null;
        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (registration != null) {
            registration.remove();
        }
    }

    private void doTakeAlbumAction() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    public String getImageNameToUri(Uri data) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().managedQuery(data, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        imgPath = cursor.getString(column_index);
        String imgName = imgPath.substring(imgPath.lastIndexOf("/") + 1);

        return imgName;
    }

    public static void saveBitmaptoJpeg(Bitmap bitmap, String folder, String name) {
        String ex_storage = Environment.getExternalStorageDirectory().getAbsolutePath();
        // Get Absolute Path in External Sdcard
        String foler_name = "/" + folder + "/";
        String file_name = name + ".jpg";
        String string_path = ex_storage + foler_name;
        imgUpLoadPath = string_path + file_name;


        File file_path;
        try {
            file_path = new File(string_path);
            if (!file_path.isDirectory()) {
                file_path.mkdirs();
            }
            FileOutputStream out = new FileOutputStream(string_path + file_name);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();

        } catch (FileNotFoundException exception) {
            Log.e("FileNotFoundException", exception.getMessage());
        } catch (IOException exception) {
            Log.e("IOException", exception.getMessage());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case PICK_FROM_ALBUM:
                try {
                    //이미지 경로, 이름
                    if (data == null) break;
                    String imaName = getImageNameToUri(data.getData());
                    Uri selectImageUri = data.getData();

                    Cursor c = getContext().getContentResolver().query(Uri.parse(selectImageUri.toString()), null, null, null, null);
                    c.moveToNext();
                    absolutePath = c.getString(c.getColumnIndex(MediaStore.MediaColumns.DATA));

                    Bitmap image_bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), data.getData());

                    //이미지 리사이징
                    int height = image_bitmap.getHeight();
                    int width = image_bitmap.getWidth();

                    Log.e("체크", absolutePath);
                    Bitmap src = BitmapFactory.decodeFile(absolutePath);
                    Bitmap resized = Bitmap.createScaledBitmap(src, width / 2, height / 2, true);

                    saveBitmaptoJpeg(resized, "resizeTmp", imaName);

                    if (imgUpLoadPath != null) {
                        Uri file = Uri.fromFile(new File(imgUpLoadPath));
                        StorageReference profileImgRef = storageRef.child("profile_image/" + user.getUid());
                        UploadTask uploadTask = profileImgRef.putFile(file);
                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Uri profile_URI = taskSnapshot.getDownloadUrl();

                                user.updateProfile(new UserProfileChangeRequest.Builder()
                                        .setPhotoUri(profile_URI).build());

                                db.collection("user").document(user.getUid())
                                        .update("profile_url", profile_URI.toString());
                            }
                        });
                    }

                    myUserProfileImage.setImageBitmap(resized);
                    Toast.makeText(getContext(), "프로필사진이 변경되었습니다.", Toast.LENGTH_SHORT).show();


                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }


    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                }
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);

            } else {
                // 사용자 언제나 허락시
                doTakeAlbumAction();
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    // 퍼미션 모두 허용일 시
                } else {

                }
                break;
        }
    }


}
