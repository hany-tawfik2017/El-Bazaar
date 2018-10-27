package com.hany.el_bazaar.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hany.el_bazaar.Model.User;
import com.hany.el_bazaar.R;

/**
 * Created by Hany on 10/14/2018.
 */

public class JoinActivity extends AppCompatActivity {

    final FirebaseDatabase databse = FirebaseDatabase.getInstance();
    Button join;
    TextView loginText, fullNameText, emailText, mobileText, passwordText, addressText;
    ImageView backImage;
    RadioButton organizerType, vendorType;
    RadioGroup radioGroup;
    String userId;
    String type;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_activity);
        join = (Button) findViewById(R.id.join_button);
        loginText = (TextView) findViewById(R.id.login_text);
        emailText = (TextView) findViewById(R.id.email);
        mobileText = (TextView) findViewById(R.id.mobile);
        fullNameText = (TextView) findViewById(R.id.name);
        passwordText = (TextView) findViewById(R.id.password);
        addressText = (TextView) findViewById(R.id.address);
        organizerType = (RadioButton) findViewById(R.id.organizer_radio);
        vendorType = (RadioButton) findViewById(R.id.vendor_radio);
        radioGroup = (RadioGroup) findViewById(R.id.type_group);
        backImage = (ImageView) findViewById(R.id.back_img);
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JoinActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        if (TextUtils.isEmpty(userId)) {
            Toast.makeText(this, "user Existed", Toast.LENGTH_LONG).show();
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.organizer_radio:
                        type = "Organizer";
                        break;
                    case R.id.vendor_radio:
                        type = "Vendor";
                        break;

                }
            }
        });
        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkFields()) {
                    setNewUser();
                }
            }
        });

    }

    private void setNewUser() {
        DatabaseReference reference = databse.getReference("users");
        userId = reference.push().getKey();

        User user = new User(fullNameText.getText().toString(), emailText.getText().toString(), passwordText.getText().toString(), addressText.getText().toString(), mobileText.getText().toString(), type);
        reference.child(userId).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(JoinActivity.this, "You are joined successfully", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });
    }

    private boolean checkFields() {
        if (fullNameText.getText().toString().isEmpty()) {
            fullNameText.setError("Provide your name first!");
            fullNameText.requestFocus();
            return false;
        } else if (emailText.getText().toString().isEmpty()) {
            emailText.setError("Provide your email first!");
            emailText.requestFocus();
            return false;
        } else if (passwordText.getText().toString().isEmpty() || passwordText.getText().toString().length() < 8) {
            if (passwordText.getText().toString().isEmpty())
                passwordText.setError("Provide your password first!");
            else
                passwordText.setError("your password must be more than 8 characters!");
            passwordText.requestFocus();
            return false;
        } else if (addressText.getText().toString().isEmpty()) {
            addressText.setError("Provide your address first");
            addressText.requestFocus();
            return false;
        } else if (mobileText.getText().toString().isEmpty()) {
            mobileText.setError("Provide your mobile first!");
            mobileText.requestFocus();
            return false;
        } else if (type == null || radioGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Please select your type", Toast.LENGTH_LONG).show();
            return false;
        } else
            return true;
    }
}
