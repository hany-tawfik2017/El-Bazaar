package com.hany.el_bazaar.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hany.el_bazaar.Defaults;
import com.hany.el_bazaar.Model.User;
import com.hany.el_bazaar.R;
import com.hany.el_bazaar.activities.ProfileActivity;

/**
 * Created by Hany on 12/24/2018.
 */

public class EditUserProfileFragment extends Fragment {

    EditText editName, editEmail, editMobile, editAddress, editPassowrd;
    Button saveBtn;
    DatabaseReference reference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_edit_user, container, false);
        reference = FirebaseDatabase.getInstance().getReference("users");
        editName = view.findViewById(R.id.name_edit);
        editEmail = view.findViewById(R.id.email_edit);
        editMobile = view.findViewById(R.id.edit_mobile);
        editAddress = view.findViewById(R.id.address_edit);
        editPassowrd = view.findViewById(R.id.password_edit);
        saveBtn = view.findViewById(R.id.save_button);

        Bundle bundle = getArguments();
        if (bundle != null) {
            editName.setText((CharSequence) bundle.get("userName"));
            editName.setEnabled(false);
            editEmail.setText((CharSequence) bundle.get("userEmail"));
            editEmail.setEnabled(false);
            editMobile.setText((CharSequence) bundle.get("userMob"));
            editPassowrd.setText((CharSequence) bundle.get("userPass"));
            editPassowrd.setEnabled(false);
            editAddress.setText((CharSequence) bundle.get("userAddress"));
        }

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Animation alphaAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.button_alpha_anim);
                v.startAnimation(alphaAnimation);
                User user = new User();
                user.setEmail(editEmail.getText().toString());
                user.setPassword(editPassowrd.getText().toString());
                user.setName(editName.getText().toString());
                if (editAddress.getText().toString().isEmpty()) {
                    editAddress.setError("provide a valid address");
                    editAddress.requestFocus();
                    return;
                } else
                    user.setAddress(editAddress.getText().toString());
                if (editMobile.getText().toString().isEmpty()) {
                    editMobile.setError("provide a valid Mobile number");
                    editMobile.requestFocus();
                    return;
                } else
                    user.setMobile(editMobile.getText().toString());
                user.setUserType(Defaults.getDefaults("userType", getActivity()));
                reference.child(Defaults.getDefaults("userId", getActivity())).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        startActivity(new Intent(getActivity(), ProfileActivity.class));
                        getActivity().finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        return view;
    }


}
