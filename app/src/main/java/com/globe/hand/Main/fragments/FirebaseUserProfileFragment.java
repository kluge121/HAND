package com.globe.hand.Main.fragments;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.globe.hand.R;
import com.globe.hand.Setting.SettingActivity;
import com.globe.hand.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class FirebaseUserProfileFragment extends Fragment {

    private final int PERMISSION_REQUEST_CODE = 100;
    private final int PICK_FROM_ALBUM = 90;
    String imgPath;
    String absolutePath;
    static String imgUpLoadPath = "";

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReference();

    public FirebaseUserProfileFragment() {
        // Required empty public constructor
    }

    public static FirebaseUserProfileFragment newInstance() {
        return new FirebaseUserProfileFragment();
    }

    private TextView userInfoText;
    private CircleImageView userImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(
                R.layout.fragment_user_profile, container, false);

        userImage = view.findViewById(R.id.image_user_profile);
        userInfoText = view.findViewById(R.id.text_user_name);


        assert user != null;
        if (user.getPhotoUrl() != null) {
            Glide.with(this)
                    .load(user.getPhotoUrl())
                    .apply(RequestOptions.circleCropTransform())
                    .into(userImage);
        }

        if (user.getDisplayName() != null) {
            userInfoText.setText(String.format(
                    getString(R.string.user_profile_name_format), user.getDisplayName()));
        } else {
            userInfoText.setText(getString(R.string.user_profile_name_empty));
        }

        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getContext())
                        .setMessage("프로필 이미지를 바꾸시겠습니까?")
                        .setPositiveButton(R.string.change_photo, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //TODO : 사진 권한 받아서 갤러리 띄우기
                                checkPermission();
                            }
                        }).setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
            }
        });

        userInfoText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText editUserNickname = new EditText(getContext());
                AlertDialog.Builder alertBuilder =
                        new AlertDialog.Builder(getContext())
                                .setTitle("닉네임 변경")
                                .setMessage("변경할 닉네임을 입력해주세요.")
                                .setPositiveButton(R.string.change_photo, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        String nickname = editUserNickname.getText().toString();
                                        if (!nickname.isEmpty()) {
                                            final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                                            UserProfileChangeRequest profileRequest =
                                                    new UserProfileChangeRequest.Builder()
                                                            .setDisplayName(nickname).build();

                                            firebaseUser.updateProfile(profileRequest)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                String changedNickname = firebaseUser.getDisplayName();
                                                                Log.e("닉네임 적용", changedNickname + "헤헤");

                                                                FirebaseFirestore.getInstance()
                                                                        .collection("user").document(firebaseUser.getUid())
                                                                        .update("name", changedNickname);

                                                                userInfoText.setText(changedNickname);
                                                            } else {
                                                                Log.e("닉네임 적용", "실패");
                                                            }
                                                        }
                                                    });
                                        }
                                        dialogInterface.dismiss();
                                    }
                                }).setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });

                String nickName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
                if (nickName != null || !nickName.isEmpty()) {
                    editUserNickname.setText(nickName);
                }
                alertBuilder.setView(editUserNickname);
                alertBuilder.show();
            }
        });

//        ImageView imageView = view.findViewById(R.id.image_setting);
//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(getContext(), SettingActivity.class));
//            }
//        });
        return view;
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

                    userImage.setImageBitmap(resized);
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
