package com.hany.el_bazaar.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.hany.el_bazaar.MainActivity;
import com.hany.el_bazaar.R;
import com.rengwuxian.materialedittext.MaterialEditText;

/**
 * Created by Hany on 10/16/2018.
 */

public class LoginActivity extends AppCompatActivity {

    ImageView backImage;
    TextView registerText;
    Button loginBtn;
    ImageView facebookBtn;
    MaterialEditText emailText;
    MaterialEditText passText;
    FirebaseAuth auth;
    CallbackManager facebookCallBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        backImage = (ImageView) findViewById(R.id.back_img);
        registerText = (TextView) findViewById(R.id.register_text);
        loginBtn = (Button) findViewById(R.id.login_button);
        facebookBtn = (ImageView) findViewById(R.id.facebook_login);
        emailText = (MaterialEditText) findViewById(R.id.email);
        passText = (MaterialEditText) findViewById(R.id.password);
        auth = FirebaseAuth.getInstance();
        facebookCallBack = CallbackManager.Factory.create();
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
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Animation alphaAnimation = AnimationUtils.loadAnimation(LoginActivity.this,R.anim.button_alpha_anim);
                v.startAnimation(alphaAnimation);
                if (checkFields())
                    auth.signInWithEmailAndPassword(emailText.getText().toString(), passText.getText().toString())
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (!task.isSuccessful())
                                        Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_LONG).show();
                                    else {
                                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                        finish();
                                    }
                                }
                            });
            }
        });
        facebookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().registerCallback(facebookCallBack, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        Log.i("Facebook Cancel", "registration Cancelled");
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.e("Facebook register error", error.getMessage());
                    }
                });
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        facebookCallBack.onActivityResult(requestCode, resultCode, data);
    }

    private boolean checkFields() {

        if (emailText.getText().toString().isEmpty()) {
            emailText.setError("Provide your valid email!");
            emailText.requestFocus();
            return false;
        } else if (passText.getText().toString().isEmpty()) {

            passText.setError("Provide your valid password!");

            passText.requestFocus();
            return false;
        } else
            return true;
    }

    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        auth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful())
                    Toast.makeText(LoginActivity.this, "Login with facebook is failed!", Toast.LENGTH_LONG).show();
                else {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                }

            }
        });
    }

}
