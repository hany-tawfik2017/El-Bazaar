package com.hany.el_bazaar.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hany.el_bazaar.Defaults;
import com.hany.el_bazaar.R;

import java.util.HashMap;
import java.util.Map;

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
            editEmail.setText((CharSequence) bundle.get("userEmail"));
            editMobile.setText((CharSequence) bundle.get("userMob"));
            editPassowrd.setText((CharSequence) bundle.get("userPass"));
            editPassowrd.setEnabled(false);
            editAddress.setText((CharSequence) bundle.get("userAddress"));
        }

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,String> map = new HashMap<>();
                map.put("address",editAddress.getText().toString());
                map.put("mobile",editMobile.getText().toString());
                reference.child(Defaults.getDefaults("userId",getActivity())).child("info").setValue(map);
            }
        });


        return view;
    }


}
