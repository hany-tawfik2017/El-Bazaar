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

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Hany on 12/24/2018.
 */

public class EditAboutContactsFragment extends Fragment {

    EditText editAbout, editFacebook, editGoogle, editInstagram, editBrandName;
    Button saveBtn;
    DatabaseReference reference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.from(getActivity()).inflate(R.layout.fragment_edit_contacts_about, container, false);
        reference = FirebaseDatabase.getInstance().getReference("users");
        saveBtn = view.findViewById(R.id.save_button);
        editAbout = view.findViewById(R.id.about_edit);
        editFacebook = view.findViewById(R.id.facebook_edit);
        editGoogle = view.findViewById(R.id.google_edit);
        editInstagram = view.findViewById(R.id.instagram_edit);
        editBrandName = view.findViewById(R.id.brand_edit);
        final Bundle bundle = getArguments();

        if (Defaults.getDefaults("userType", getActivity()).equals("Vendor")) {
            editBrandName.setVisibility(View.VISIBLE);
            editBrandName.setText(bundle.getString("brandName"));
        }
        if (bundle.getSerializable("about") != null) {
            Map<String, String> about = (Map<String, String>) bundle.getSerializable("about");
            if (about != null) {
                editAbout.setText(about.get("aboutText"));
                editFacebook.setText(about.get("facebookUrl"));
                editInstagram.setText(about.get("instagramUrl"));
                editGoogle.setText(about.get("googleUrl"));
            }
        }

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Animation alphaAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.button_alpha_anim);
                v.startAnimation(alphaAnimation);
                User user = new User();
                user.userType = Defaults.getDefaults("userType", getActivity());
                user.name = (String) bundle.get("userName");
                user.email = (String) bundle.get("userEmail");
                user.mobile = (String) bundle.get("userMob");
                user.password = (String) bundle.get("userPass");
                user.address = (String) bundle.get("userAddress");
                Map<String, String> aboutMap = new HashMap<>();
                if (!editAbout.getText().toString().isEmpty())
                    aboutMap.put("aboutText", editAbout.getText().toString());
                if (Defaults.getDefaults("userType", getActivity()).equals("Vendor")) {
                    if (editBrandName.getText().toString().isEmpty()) {
                        editBrandName.setError("provide a valide Brand name");
                        editBrandName.requestFocus();
                        return;
                    } else
                        user.brandName = editBrandName.getText().toString();
                }
                if (!editFacebook.getText().toString().isEmpty())
                    aboutMap.put("facebookUrl", editFacebook.getText().toString());
                if (!editGoogle.getText().toString().isEmpty())
                    aboutMap.put("googleUrl", editGoogle.getText().toString());
                if (!editInstagram.getText().toString().isEmpty())
                    aboutMap.put("instagramUrl", editInstagram.getText().toString());
                user.aboutMap = aboutMap;
                reference.child(Defaults.getDefaults("userId", getActivity())).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        startActivity(new Intent(getActivity(), ProfileActivity.class));
                        getActivity().finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        return view;
    }
}
