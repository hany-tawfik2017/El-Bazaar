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
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hany.el_bazaar.Adapters.PostItemAdapter;
import com.hany.el_bazaar.Defaults;
import com.hany.el_bazaar.GlideApp;
import com.hany.el_bazaar.MainActivity;
import com.hany.el_bazaar.Model.Product;
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

public class PostProductFragment extends Fragment {

    final FirebaseDatabase databse = FirebaseDatabase.getInstance();
    final FirebaseStorage storage = FirebaseStorage.getInstance();
    ImageView firstImage, secondImage, thirdImage;
    EditText productTitle, productPrice, productDesc;
    Button postSave, addFirstImage, addSecondImage, addThirdImage;
    Spinner currencySpinner;
    String spinnerSelection;
    TextView imagesTitle;
    StorageReference reference;
    String imageNo;
    ArrayList<Object> bazaarsLocations;
    RecyclerView bazaarsVendorsList;
    PostItemAdapter adapter;
    ArrayList<Map<String, String>> callbackSpinner;
    List<String> images = new ArrayList<>();
    ProgressBar loadingBar1,loadingBar2,loadingBar3;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_post_product, container, false);
        productTitle = view.findViewById(R.id.post_title);
        productPrice = view.findViewById(R.id.post_price);
        productDesc = view.findViewById(R.id.post_desc);
        imagesTitle = view.findViewById(R.id.image_title);
        postSave = view.findViewById(R.id.post_button);
        currencySpinner = view.findViewById(R.id.currency_spinner);
        addFirstImage = view.findViewById(R.id.add_first_image);
        firstImage = view.findViewById(R.id.first_image);
        loadingBar1 = view.findViewById(R.id.loading_bar);
        loadingBar2 = view.findViewById(R.id.loading_bar2);
        loadingBar3 = view.findViewById(R.id.loading_bar3);
        bazaarsVendorsList = view.findViewById(R.id.bazaars_vendors);
        bazaarsLocations = new ArrayList<>();
        callbackSpinner = new ArrayList<>();
        bazaarsVendorsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        setBazaarsList();
        secondImage = view.findViewById(R.id.second_image);
        addSecondImage = view.findViewById(R.id.add_second_image);
        thirdImage = view.findViewById(R.id.third_image);
        addThirdImage = view.findViewById(R.id.add_third_image);
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
            public void onClick(View view) {
                final Animation alphaAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.button_alpha_anim);
                view.startAnimation(alphaAnimation);
                if (checkProductFields())
                    postProduct();
            }
        });
        return view;
    }

    private void setBazaarsList() {
        DatabaseReference reference = databse.getReference("bazaars");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    setBazaars(map);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setBazaars(Map<String, Object> map) {
        if (map != null) {
            final ArrayList<Map<String, String>> bazaars = new ArrayList<>();
            final ArrayList<String> spinnerList = new ArrayList<>();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                if (entry.getValue() instanceof Map) {
                    spinnerList.add(((Map) entry.getValue()).get("bazaarName") + " - " + ((Map) entry.getValue()).get("bazaarPlace"));
                    bazaarsLocations.add((String) ((Map) entry.getValue()).get("bazaarName"));
                    bazaars.add((Map<String, String>) entry.getValue());
                }
            }
            SpinnerCallback callback = new SpinnerCallback() {
                @Override
                public void callback(String itemSelected) {
                    Map<String, String> bazaarMap = new HashMap<>();
                    bazaarMap.put("bazaarName", itemSelected.split("-")[0].trim());
                    for (Map<String, String> map1 : bazaars) {
                        if (map1.get("bazaarName").equals(itemSelected.split("-")[0].trim())){
                            bazaarMap.put("bazaarPlace", itemSelected.split("-")[1].trim());
                            break;
                        }
                    }
                    boolean flag = false;
                    for (Map<String, String> map1 : callbackSpinner) {
                        if (map1.get("bazaarName").equals(bazaarMap.get("bazaarName")))
                            flag = true;
                    }
                    if (!flag)
                        callbackSpinner.add(bazaarMap);
                }
            };
            adapter = new PostItemAdapter(getActivity(), bazaarsLocations, callback);
            adapter.setSpinnerItems(spinnerList);
            bazaarsVendorsList.setAdapter(adapter);
        }
    }

    private void startResults(String imageNumber) {
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.putExtra("imageNo", imageNumber);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), 152);
    }

    private void postProduct() {
        DatabaseReference reference = databse.getReference("products");
        String productId = reference.push().getKey();
        spinnerSelection = (String) currencySpinner.getSelectedItem();
        Map<String, String> vendorMap = new HashMap<>();
        vendorMap.put("vendorName", Defaults.getDefaults("userName", getActivity()));
        vendorMap.put("vendorEmail", FirebaseAuth.getInstance().getCurrentUser().getEmail());
        Product product = new Product(productTitle.getText().toString(), productPrice.getText().toString(), spinnerSelection != null ? spinnerSelection : "EGP", productDesc.getText().toString(), images, vendorMap, false,callbackSpinner);
        reference.child(productId).setValue(product).addOnSuccessListener(new OnSuccessListener<Void>() {
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


    private boolean checkProductFields() {
        if (productTitle.getText().toString().isEmpty()) {
            productTitle.setError("Provide your product name first!");
            productTitle.requestFocus();
            return false;
        } else if (productPrice.getText().toString().isEmpty()) {
            productPrice.setError("Provide your product price first!");
            productPrice.requestFocus();
            return false;
        } else if (productDesc.getText().toString().isEmpty()) {

            productDesc.setError("Provide your product description first!");
            productDesc.requestFocus();
            return false;
        } else if (callbackSpinner.isEmpty()) {
            Toast.makeText(getActivity(), "Please Select at least one bazaar", Toast.LENGTH_LONG).show();
            return false;
        } else
            return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != getActivity().RESULT_CANCELED && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            if (uri != null) {
                switch (imageNo){
                    case "first":
                        addFirstImage.setVisibility(View.INVISIBLE);
                        loadingBar1.setVisibility(View.VISIBLE);
                        break;
                    case "second":
                        addSecondImage.setVisibility(View.INVISIBLE);
                        loadingBar2.setVisibility(View.VISIBLE);
                        break;
                    case "third":
                        addThirdImage.setVisibility(View.INVISIBLE);
                        loadingBar3.setVisibility(View.VISIBLE);
                        break;
                }
                String filePath = Commons.getPath(uri, getActivity());
                File file = new File(filePath);
                final StorageReference productReference = reference.child("product/" + file.getName());
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
                    loadingBar1.setVisibility(View.GONE);
                    addSecondImage.setVisibility(View.VISIBLE);
                    image = firstImage;
                    break;
                case "second":
                    loadingBar2.setVisibility(View.GONE);
                    addThirdImage.setVisibility(View.VISIBLE);
                    image = secondImage;
                    break;
                case "third":
                    loadingBar3.setVisibility(View.GONE);
                    image = thirdImage;
                    break;
            }
            GlideApp.with(this).load(productReference).into(image);
        }
    }
}
