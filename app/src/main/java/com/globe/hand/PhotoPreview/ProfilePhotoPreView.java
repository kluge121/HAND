package com.globe.hand.PhotoPreview;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.globe.hand.R;
import com.globe.hand.common.BaseActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by baeminsu on 2018. 1. 6..
 */

public class ProfilePhotoPreView extends BaseActivity {

    String imagePath;
    Bitmap bitmap;
    ImageView imageView;
    PhotoView photoView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_photo_preview);

        PhotoView photoView = (PhotoView) findViewById(R.id.photoview);

        setToolbar(R.id.photo_actvitiy_toolbar, true);


//        imageView = findViewById(R.id.image);
//        photoViewAttacher = new PhotoViewAttacher(imageView);
//        photoViewAttacher.setScaleType(ScaleType.FIT_XY);
        imagePath = getIntent().getStringExtra("url");
        Log.e("체크", imagePath + "회잇");
//
//        Thread thread = new Thread(new GetIamgeRunnable());
//
//        thread.start();
//        try {
//            thread.join();
//            photoView.setImageBitmap(bitmap);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        Glide.with(getApplicationContext()).load(Uri.parse(imagePath)).into(photoView);

    }

    class GetIamgeRunnable implements Runnable {
        @Override
        public void run() {
            try {
                URL url = new URL(imagePath);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();

                InputStream is = connection.getInputStream();
                bitmap = BitmapFactory.decodeStream(is);


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
