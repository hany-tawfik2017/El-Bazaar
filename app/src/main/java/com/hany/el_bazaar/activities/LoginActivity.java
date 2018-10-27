package com.hany.el_bazaar.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hany.el_bazaar.R;

/**
 * Created by Hany on 10/16/2018.
 */

public class LoginActivity extends AppCompatActivity {

    ImageView backImage;
    TextView registerText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        backImage = (ImageView) findViewById(R.id.back_img);
        registerText = (TextView) findViewById(R.id.register_text);
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        registerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, JoinActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
