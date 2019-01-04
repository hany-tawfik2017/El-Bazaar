package com.hany.el_bazaar.Fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hany.el_bazaar.Adapters.PostItemAdapter;
import com.hany.el_bazaar.Defaults;
import com.hany.el_bazaar.GlideApp;
import com.hany.el_bazaar.MainActivity;
import com.hany.el_bazaar.Model.Bazaar;
import com.hany.el_bazaar.R;
import com.hany.el_bazaar.SpinnerCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.gauriinfotech.commons.Commons;

/**
 * Created by Hany on 12/25/2018.
 */

public class PostBazaarFragment extends Fragment {

    final FirebaseDatabase databse = FirebaseDatabase.getInstance();
    final FirebaseStorage storage = FirebaseStorage.getInstance();
    ImageView firstImage, secondImage, thirdImage;
    Button postSave, addFirstImage, addSecondImage, addThirdImage;
    EditText bazaarName, bazaarLocation, bazaarDesc;
    Spinner capacitySpinner;
    String spinnerSelection;
    ArrayList<Object> users;
    RecyclerView bazaarsVendorsList;
    PostItemAdapter adapter;
    TextView imagesTitle, addVendor;
    StorageReference reference;
    String imageNo;
    List<String> images = new ArrayList<>();
    ArrayList<Map<String, String>> callbackspinner;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_post_bazaar, container, false);

        postSave = view.findViewById(R.id.post_button);
        capacitySpinner = view.findViewById(R.id.capacity_spinner);
        addFirstImage = view.findViewById(R.id.add_first_image);
        firstImage = view.findViewById(R.id.first_image);
        secondImage = view.findViewById(R.id.second_image);
        addSecondImage = view.findViewById(R.id.add_second_image);
        thirdImage = view.findViewById(R.id.third_image);
        addThirdImage = view.findViewById(R.id.add_third_image);
        bazaarName = view.findViewById(R.id.bazaar_name_edit);
        bazaarLocation = view.findViewById(R.id.bazaar_location_edit);
        bazaarDesc = view.findViewById(R.id.post_desc);
        bazaarsVendorsList = view.findViewById(R.id.bazaars_vendors);
        addVendor = view.findViewById(R.id.add_bazaar_vendor);
        bazaarsVendorsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        users = new ArrayList<>();
        callbackspinner = new ArrayList<>();
        setVendorList();
        reference = storage.getReference();
        addFirstImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    imageNo = "first";
                    startResults("first");

                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 152);
                }

            }
        });
        addSecondImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageNo = "second";
                startResults("second");
            }
        });
        addThirdImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageNo = "third";
                startResults("third");
            }
        });
        postSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Animation alphaAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.button_alpha_anim);
                v.startAnimation(alphaAnimation);
                if (checkBazaarFields())
                    postBazaar();

            }
        });
        return view;
    }

    private void setVendorList() {
        DatabaseReference reference = databse.getReference("users");
        Query query = reference.orderByChild("userType").equalTo("Vendor");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    setVendors(map);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setVendors(final Map<String, Object> map) {
        if (map != null) {
            final ArrayList<Map<String, String>> vendors = new ArrayList<>();
            final ArrayList<String> spinnerList = new ArrayList<>();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                if (entry.getValue() instanceof Map) {
                    spinnerList.add((String) ((Map) entry.getValue()).get("name"));
                    users.add((String) ((Map) entry.getValue()).get("name"));
                    vendors.add((Map<String, String>) entry.getValue());
                }
            }
            SpinnerCallback callback = new SpinnerCallback() {
                @Override
                public void callback(String itemSelected) {
                    Map<String, String> vendorMap = new HashMap<>();
                    vendorMap.put("vendorName", itemSelected);
                    for (Map<String, String> map1 : vendors) {
                        if (map1.get("name").equals(itemSelected)) {
                            if (map1.get("brandName") != null)
                                vendorMap.put("brandName", map1.get("brandName"));
                            break;
                        }
                    }
                    boolean flag = false;
                    for (Map<String, String> map1 : callbackspinner) {
                        if (map1.get("bazaarName").equals(vendorMap.get("bazaarName")))
                            flag = true;
                    }
                    if (!flag)
                    callbackspinner.add(vendorMap);
                }
            };
            adapter = new PostItemAdapter(getActivity(), users,callback);
            adapter.setSpinnerItems(spinnerList);
            bazaarsVendorsList.setAdapter(adapter);
        }
    }

    private void postBazaar() {
        DatabaseReference reference = databse.getReference("bazaars");
        String bazaarId = reference.push().getKey();
        spinnerSelection = (String) capacitySpinner.getSelectedItem();
        Bazaar bazaar = new Bazaar(bazaarName.getText().toString(), Defaults.getDefaults("userName", getActivity()), bazaarLocation.getText().toString(), bazaarDesc.getText().toString(), spinnerSelection != null ? spinnerSelection : "4 Vendors", images, callbackspinner);
        reference.child(bazaarId).setValue(bazaar).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });
    }

    private boolean checkBazaarFields() {
        if (bazaarName.getText().toString().isEmpty()) {
            bazaarName.setError("Provide your bazaar name first!");
            bazaarName.requestFocus();
            return false;
        } else if (bazaarLocation.getText().toString().isEmpty()) {
            bazaarLocation.setError("Provide your bazaar location first!");
            bazaarLocation.requestFocus();
            return false;
        } else if (bazaarDesc.getText().toString().isEmpty()) {

            bazaarDesc.setError("Provide your bazaar description first!");
            bazaarDesc.requestFocus();
            return false;
        } else if (callbackspinner.isEmpty()) {
            Toast.makeText(getActivity(), "Please Select at least one vendor", Toast.LENGTH_LONG).show();
            return false;
        } else
            return true;
    }

    private void startResults(String imageNumber) {
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.putExtra("imageNo", imageNumber);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), 152);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != getActivity().RESULT_CANCELED && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            if (uri != null) {
                String filePath = Commons.getPath(uri, getActivity());
                File file = new File(filePath);
                final StorageReference productReference = reference.child("bazaar/" + file.getName());
                UploadTask uploadTask = productReference.putFile(Uri.fromFile(file));
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        StorageMetadata metadata = taskSnapshot.getMetadata();
                        String name = metadata != null ? metadata.getName() : null;
                        setImage(productReference, name, imageNo);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }

        }
    }

    private void setImage(StorageReference productReference, String name, String imageNo) {
        if (name != null) {
            ImageView image = null;
            images.add(name);
            switch (imageNo) {
                case "first":
                    addFirstImage.setVisibility(View.INVISIBLE);
                    addSecondImage.setVisibility(View.VISIBLE);
                    image = firstImage;
                    break;
                case "second":
                    addSecondImage.setVisibility(View.INVISIBLE);
                    addThirdImage.setVisibility(View.VISIBLE);
                    image = secondImage;
                    break;
                case "third":
                    addThirdImage.setVisibility(View.INVISIBLE);
                    image = thirdImage;
                    break;
            }
            GlideApp.with(this).load(productReference).into(image);
        }
    }
}
