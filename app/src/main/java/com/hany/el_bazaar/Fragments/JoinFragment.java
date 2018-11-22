package com.hany.el_bazaar.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hany.el_bazaar.Model.User;
import com.hany.el_bazaar.R;
import com.hany.el_bazaar.activities.LoginActivity;

/**
 * Created by Hany on 11/17/2018.
 */

public class JoinFragment extends Fragment {

    final FirebaseDatabase databse = FirebaseDatabase.getInstance();
    Button join;
    TextView loginText, fullNameText, emailText, mobileText, passwordText, addressText;
    ImageView backImage;
    RadioButton organizerType, vendorType;
    RadioGroup radioGroup;
    String userId;
    String type;
    FirebaseAuth auth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = null;
        view = inflater.inflate(R.layout.join_fragment, container, false);

        join = (Button) view.findViewById(R.id.join_button);
        loginText = (TextView) view.findViewById(R.id.login_text);
        emailText = (TextView) view.findViewById(R.id.email);
        mobileText = (TextView) view.findViewById(R.id.mobile);
        fullNameText = (TextView) view.findViewById(R.id.name);
        passwordText = (TextView) view.findViewById(R.id.password);
        addressText = (TextView) view.findViewById(R.id.address);
        organizerType = (RadioButton) view.findViewById(R.id.organizer_radio);
        vendorType = (RadioButton) view.findViewById(R.id.vendor_radio);
        radioGroup = (RadioGroup) view.findViewById(R.id.type_group);
        auth = FirebaseAuth.getInstance();

        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        if (!TextUtils.isEmpty(userId)) {
            Toast.makeText(getActivity(), "user Existed", Toast.LENGTH_LONG).show();
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
                    setNewUser(v);
                }
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private void setNewUser(View view) {
        final Animation alphaAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.button_alpha_anim);
        view.startAnimation(alphaAnimation);
        DatabaseReference reference = databse.getReference("users");
        userId = reference.push().getKey();

        User user = new User(fullNameText.getText().toString(), emailText.getText().toString(), passwordText.getText().toString(), addressText.getText().toString(), mobileText.getText().toString(), type);

//        auth.createUserWithEmailAndPassword(emailText.getText().toString().trim(), passwordText.getText().toString().trim())
//                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (!task.isSuccessful())
//                            Toast.makeText(getActivity(), "joining is failing", Toast.LENGTH_LONG).show();
//                        else {
//                            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
//                            fragmentTransaction.replace(R.id.fragment_container, new FinishJoinFragment());
//                            fragmentTransaction.commit();
//                        }
//                    }
//                });
        reference.child(userId).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        }).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                    joiningUser();
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
            Toast.makeText(getActivity(), "Please select your type", Toast.LENGTH_LONG).show();
            return false;
        } else
            return true;
    }

    private void joiningUser() {
        auth.createUserWithEmailAndPassword(emailText.getText().toString().trim(), passwordText.getText().toString().trim())
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful())
                            Toast.makeText(getActivity(), "joining is failing", Toast.LENGTH_LONG).show();
                        else {
                            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.fragment_container, new FinishJoinFragment());
                            fragmentTransaction.commit();
                        }
                    }
                });
    }
}
