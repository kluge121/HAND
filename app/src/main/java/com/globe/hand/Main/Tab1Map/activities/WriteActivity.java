package com.globe.hand.Main.Tab1Map.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.globe.hand.R;
import com.globe.hand.common.BaseActivity;

public class WriteActivity extends BaseActivity {

    private TextView textPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        setToolbar(R.id.write_toolbar, true);

        textPlace = findViewById(R.id.text_write_place);

        if(getIntent().hasExtra("place_name")) {
            textPlace.setText(getIntent().getStringExtra("place_name"));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_write_done, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_write_done) {
            setResult(RESULT_OK);
            startActivity(new Intent(this, InMapRoomActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("경고")
                .setMessage("작성한 내용이 모두 사라집니다! 정말 지도 화면으로 가시겠습니까?")
                .setPositiveButton(R.string.dialog_back, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        WriteActivity.super.onBackPressed();
                        dialogInterface.dismiss();
                    }
                }).setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
        }).show();
    }
}
