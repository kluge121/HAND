package com.globe.hand.Join;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.globe.hand.BaseActivity;
import com.globe.hand.R;

/**
 * Created by baeminsu on 2017. 12. 20..
 */

public class JoinActivity extends BaseActivity implements View.OnClickListener {


    EditText editId;
    EditText editPass;
    EditText editPhone;
    EditText editEmail;

    Button btnJoin;
    Button btnCancle;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        setWidget();
    }

    void setWidget() {
        editId = findViewById(R.id.join_edit_id);
        editPass = findViewById(R.id.join_edit_pass);
        editPhone = findViewById(R.id.join_edit_phone);
        editEmail = findViewById(R.id.join_edit_email);
        btnJoin = findViewById(R.id.join_btn_join);
        btnCancle = findViewById(R.id.join_btn_cancle);

        btnJoin.setOnClickListener(this);
        btnCancle.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.join_btn_join:
                break;
            case R.id.join_btn_cancle:
                finish();
                break;
        }
    }
}
