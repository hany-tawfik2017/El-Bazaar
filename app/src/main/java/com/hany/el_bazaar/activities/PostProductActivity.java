package com.hany.el_bazaar.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hany.el_bazaar.GlideApp;
import com.hany.el_bazaar.MainActivity;
import com.hany.el_bazaar.Model.Product;
import com.hany.el_bazaar.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import in.gauriinfotech.commons.Commons;

/**
 * Created by Hany on 12/22/2018.
 */

public class PostProductActivity extends AppCompatActivity {

    final FirebaseDatabase databse = FirebaseDatabase.getInstance();
    final FirebaseStorage storage = FirebaseStorage.getInstance();
    ImageView backImg, firstImage, secondImage, thirdImage;
    EditText productTitle, productPrice, productDesc;
    Button postSave, addFirstImage, addSecondImage, addThirdImage;
    Spinner currencySpinner;
    String spinnerSelection;
    TextView imagesTitle;
    StorageReference reference;
    String imageNo;
    List<String> images = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_product);
        backImg = findViewById(R.id.back_img);
        productTitle = findViewById(R.id.post_title);
        productPrice = findViewById(R.id.post_price);
        productDesc = findViewById(R.id.post_desc);
        imagesTitle = findViewById(R.id.image_title);
        postSave = findViewById(R.id.post_button);
        currencySpinner = findViewById(R.id.currency_spinner);
        addFirstImage = findViewById(R.id.add_first_image);
        firstImage = findViewById(R.id.first_image);
        secondImage = findViewById(R.id.second_image);
        addSecondImage = findViewById(R.id.add_second_image);
        thirdImage = findViewById(R.id.third_image);
        addThirdImage = findViewById(R.id.add_third_image);
        reference = storage.getReference();
        addFirstImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(PostProductActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
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

        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        postSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Animation alphaAnimation = AnimationUtils.loadAnimation(PostProductActivity.this, R.anim.button_alpha_anim);
                view.startAnimation(alphaAnimation);
                if (checkProductFields())
                    postProduct();
            }
        });

    }

    private void startResults(String imageNumber) {
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.putExtra("imageNo",imageNumber);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), 152);
    }

    private void postProduct() {
        DatabaseReference reference = databse.getReference("products");
        String productId = reference.push().getKey();
        spinnerSelection = (String) currencySpinner.getSelectedItem();
        Product product = new Product(productTitle.getText().toString(), productPrice.getText().toString(), spinnerSelection != null ? spinnerSelection : "EGP", productDesc.getText().toString(), images,false);
        reference.child(productId).setValue(product).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                startActivity(new Intent(PostProductActivity.this, MainActivity.class));
                finish();
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
        } else
            return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            if (uri != null) {
                String filePath = Commons.getPath(uri, this);
                File file = new File(filePath);
                final StorageReference productReference = reference.child("product/" + file.getName());
                UploadTask uploadTask = productReference.putFile(Uri.fromFile(file));
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        StorageMetadata metadata = taskSnapshot.getMetadata();
                        String name = metadata != null ? metadata.getName() : null;
                        setImage(productReference,name,imageNo);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(PostProductActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }

        }
    }

    private void setImage(StorageReference productReference, String name, String imageNo) {
        if(name!=null){
            ImageView image = null;
            images.add(name);
            switch (imageNo){
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
