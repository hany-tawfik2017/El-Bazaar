package com.hany.el_bazaar.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.hany.el_bazaar.Fragments.PostBazaarFragment;
import com.hany.el_bazaar.Fragments.PostProductFragment;
import com.hany.el_bazaar.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hany on 12/22/2018.
 */

public class PostProductActivity extends AppCompatActivity {


    private Fragment fragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    ImageView backImg;
    TextView actionBarTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_product);
        backImg = findViewById(R.id.back_img);
        actionBarTitle = findViewById(R.id.action_bar_title);
        if ((Boolean) getIntent().getExtras().get("isOrganizer")) {
            fragment = new PostBazaarFragment();
            actionBarTitle.setText("Post Your Bazaar");
        }
        else
            fragment = new PostProductFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();


    }

//    private void startResults(String imageNumber) {
//        Intent intent = new Intent();
//        intent.setType("*/*");
//        intent.putExtra("imageNo",imageNumber);
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent, "Select File"), 152);
//    }
//
//    private void postProduct() {
//        DatabaseReference reference = databse.getReference("products");
//        String productId = reference.push().getKey();
//        spinnerSelection = (String) currencySpinner.getSelectedItem();
//        Product product = new Product(productTitle.getText().toString(), productPrice.getText().toString(), spinnerSelection != null ? spinnerSelection : "EGP", productDesc.getText().toString(), images,false);
//        reference.child(productId).setValue(product).addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//                startActivity(new Intent(PostProductActivity.this, MainActivity.class));
//                finish();
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                e.printStackTrace();
//            }
//        });
//    }
//
//
//    private boolean checkProductFields() {
//        if (productTitle.getText().toString().isEmpty()) {
//            productTitle.setError("Provide your product name first!");
//            productTitle.requestFocus();
//            return false;
//        } else if (productPrice.getText().toString().isEmpty()) {
//            productPrice.setError("Provide your product price first!");
//            productPrice.requestFocus();
//            return false;
//        } else if (productDesc.getText().toString().isEmpty()) {
//
//            productDesc.setError("Provide your product description first!");
//            productDesc.requestFocus();
//            return false;
//        } else
//            return true;
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode != RESULT_CANCELED && resultCode == Activity.RESULT_OK) {
//            Uri uri = data.getData();
//            if (uri != null) {
//                String filePath = Commons.getPath(uri, this);
//                File file = new File(filePath);
//                final StorageReference productReference = reference.child("product/" + file.getName());
//                UploadTask uploadTask = productReference.putFile(Uri.fromFile(file));
//                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        StorageMetadata metadata = taskSnapshot.getMetadata();
//                        String name = metadata != null ? metadata.getName() : null;
//                        setImage(productReference,name,imageNo);
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(PostProductActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
//                    }
//                });
//            }
//
//        }
//    }
//
//    private void setImage(StorageReference productReference, String name, String imageNo) {
//        if(name!=null){
//            ImageView image = null;
//            images.add(name);
//            switch (imageNo){
//                case "first":
//                    addFirstImage.setVisibility(View.INVISIBLE);
//                    addSecondImage.setVisibility(View.VISIBLE);
//                    image = firstImage;
//                    break;
//                case "second":
//                    addSecondImage.setVisibility(View.INVISIBLE);
//                    addThirdImage.setVisibility(View.VISIBLE);
//                    image = secondImage;
//                    break;
//                case "third":
//                    addThirdImage.setVisibility(View.INVISIBLE);
//                    image = thirdImage;
//                    break;
//            }
//            GlideApp.with(this).load(productReference).into(image);
//        }
//    }
}
