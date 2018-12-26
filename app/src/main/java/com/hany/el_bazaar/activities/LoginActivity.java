package com.hany.el_bazaar.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
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
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hany.el_bazaar.Defaults;
import com.hany.el_bazaar.MainActivity;
import com.hany.el_bazaar.Model.User;
import com.hany.el_bazaar.R;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONObject;

import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Map;

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
    DatabaseReference reference;
    ProgressDialog progressDialog;

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
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Signing in...");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("users");
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
                final Animation alphaAnimation = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.button_alpha_anim);
                v.startAnimation(alphaAnimation);
                progressDialog.show();
                if (checkFields())
                    auth.signInWithEmailAndPassword(emailText.getText().toString(), passText.getText().toString())
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (!task.isSuccessful()) {
                                        progressDialog.dismiss();
                                        Toast.makeText(LoginActivity.this, task.getException() != null ? task.getException().getMessage() : "loging failed", Toast.LENGTH_LONG).show();
                                    } else {
                                        getUserId();

                                    }
                                }
                            });
            }
        });

        facebookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("email", "public_profile"));
                LoginManager.getInstance().registerCallback(facebookCallBack, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(final LoginResult loginResult) {

                        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Bundle bFacebookdata = getFacebookData(object);
                                handleFacebookAccessToken(loginResult.getAccessToken(), bFacebookdata);
                            }
                        });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id, first_name, last_name, email,gender, birthday, location");
                        request.setParameters(parameters);
                        request.executeAsync();

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

    private Bundle getFacebookData(JSONObject object) {
        Bundle bundle = new Bundle();
        try {
            URL url = new URL("https://graph.facebook.com/" + object.getString("id") + "/picture?width=300&height=200");
            bundle.putString("profile_pic", url.toString());
            if (object.has("id"))
                bundle.putString("idFacebook", object.getString("id"));
            if (object.has("first_name"))
                bundle.putString("first_name", object.getString("first_name"));
            if (object.has("last_name"))
                bundle.putString("last_name", object.getString("last_name"));
            if (object.has("email"))
                bundle.putString("email", object.getString("email"));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return bundle;
    }

    private void getUserId() {
        if (Defaults.getDefaults("userId", this) == null) {
            Query query = reference.orderByChild("email").equalTo(emailText.getText().toString());
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    progressDialog.dismiss();
                    if (dataSnapshot.exists()) {
                        Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                        for (Map.Entry<String, Object> entry : map.entrySet()) {
                            Defaults.setDefaults("userId", entry.getKey(), LoginActivity.this);
                            if (entry.getValue() instanceof Map)
                                Defaults.setDefaults("userName", (String) ((Map) entry.getValue()).get("name"), LoginActivity.this);
                        }
                        Defaults.setDefaults("auth", "verified", LoginActivity.this);
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Defaults.setDefaults("auth", "verified", LoginActivity.this);
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

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

    private void handleFacebookAccessToken(AccessToken token, final Bundle bFacebookdata) {
        final AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        User user = new User(bFacebookdata.getString("first_name") + " " + bFacebookdata.getString("last_name"), bFacebookdata.getString("email"));
        final String userId = bFacebookdata.getString("idFacebook") != null ? bFacebookdata.getString("idFacebook") : reference.push().getKey();
        reference.child(userId).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Defaults.setDefaults("userId", userId, LoginActivity.this);
                Defaults.setDefaults("userName", bFacebookdata.getString("first_name") + " " + bFacebookdata.getString("last_name"), LoginActivity.this);
                loginWithFacebook(credential);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loginWithFacebook(AuthCredential credential) {
        auth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                } else {
                    Defaults.setDefaults("auth", "verified", LoginActivity.this);
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                }

            }
        });
    }

    public void getFaceBookHashKey(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    "com.hany.el_bazaar",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException ignored) {
            ignored.printStackTrace();
        }
    }

}
