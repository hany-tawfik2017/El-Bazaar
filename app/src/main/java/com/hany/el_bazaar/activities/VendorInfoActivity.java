package com.hany.el_bazaar.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hany.el_bazaar.Adapters.BazaarsTabAdapter;
import com.hany.el_bazaar.Adapters.ProductVendorAdapter;
import com.hany.el_bazaar.Adapters.ReviewsAdapter;
import com.hany.el_bazaar.Defaults;
import com.hany.el_bazaar.Model.Bazaar;
import com.hany.el_bazaar.Model.Product;
import com.hany.el_bazaar.Model.Review;
import com.hany.el_bazaar.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Hany on 12/27/2018.
 */

public class VendorInfoActivity extends AppCompatActivity {

    DatabaseReference reference;
    RecyclerView organizerBazaars;
    BazaarsTabAdapter adapter;
    ImageView facebookImage, googleImage, instagramImage, backImage;
    RecyclerView reviewsList, productVendorList;
    ReviewsAdapter reviewsAdapter;
    ArrayList<Product> products;
    ProductVendorAdapter vendorAdapter;
    ArrayList<Review> reviews;
    ArrayList<Bazaar> bazaars;
    EditText writeReview;
    RatingBar reviewRate, vendorRate;
    SpinKitView loading, loading2, loading3;
    TextView vendorName, brandName, postReview, aboutContent;
    long rate;
    RelativeLayout reviewsLayout;
    PackageManager packageManager;
    String facebookUrl, googleUrl, instagramUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_info);
        organizerBazaars = findViewById(R.id.bazaars);
        vendorName = findViewById(R.id.vendor_name);
        brandName = findViewById(R.id.brand_name);
        vendorRate = findViewById(R.id.vendor_rate);
        aboutContent = findViewById(R.id.about_content);
        facebookImage = findViewById(R.id.facebook_contact);
        googleImage = findViewById(R.id.google_contact);
        instagramImage = findViewById(R.id.instagram_contact);
        backImage = findViewById(R.id.back_img);
        reviewsList = findViewById(R.id.reviews);
        writeReview = findViewById(R.id.write_review);
        reviewRate = findViewById(R.id.review_rate);
        postReview = findViewById(R.id.post_review);
        productVendorList = findViewById(R.id.products);
        products = new ArrayList<>();

        reviewsLayout = findViewById(R.id.reviews_layout);
        loading = findViewById(R.id.loading);
        loading2 = findViewById(R.id.loading2);
        loading3 = findViewById(R.id.loading3);
        bazaars = new ArrayList<>();
        packageManager = getPackageManager();
        getVendorContacts();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        organizerBazaars.setLayoutManager(layoutManager);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        reviewsList.setLayoutManager(layoutManager2);
        LinearLayoutManager layoutManager3 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        productVendorList.setLayoutManager(layoutManager3);
        vendorAdapter = new ProductVendorAdapter(this, products);
        productVendorList.setAdapter(vendorAdapter);
        getProducts();
        vendorName.setText(getIntent().getExtras().getString("vendorName"));
        if (getIntent().getExtras().getString("brandName") != null)
            brandName.setText(getIntent().getExtras().getString("brandName"));
        adapter = new BazaarsTabAdapter(this, bazaars);
        adapter.setProfileBazaar(true);
        adapter.notClicked = true;
        organizerBazaars.setAdapter(adapter);
        setupBazaars();
        reviews = new ArrayList<>();
        getReviews();
        reviewsAdapter = new ReviewsAdapter(this, reviews);
        if (reviews.size() == 0)
            reviewsLayout.setVisibility(View.GONE);
        reviewsList.setAdapter(reviewsAdapter);
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        writeReview.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    reviewRate.setVisibility(View.VISIBLE);
                else
                    reviewRate.setVisibility(View.GONE);
            }
        });
        writeReview.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() != 0)
                    postReview.setVisibility(View.VISIBLE);
                else
                    postReview.setVisibility(View.GONE);
            }
        });
        reviewRate.setIsIndicator(false);
        reviewRate.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                rate = (long) rating;
            }
        });
        postReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setReview();
            }
        });
    }

    private void getVendorContacts() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query query = reference.orderByChild("name").equalTo((String) getIntent().getExtras().get("vendorName"));
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    setAboutContent(map);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(VendorInfoActivity.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setAboutContent(Map<String, Object> map) {
        if (map != null) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {

                if (entry.getValue() instanceof Map) {
                    Map<String, Object> mapObj = (Map<String, Object>) entry.getValue();

                    if (mapObj.get("brandName") != null)
                        brandName.setText((CharSequence) mapObj.get("brandName"));
                    if (mapObj.get("aboutMap") != null) {
                        Map<String, String> aboutMap = (Map<String, String>) mapObj.get("aboutMap");
                        if (aboutMap.get("aboutText") != null)
                            aboutContent.setText(aboutMap.get("aboutText"));
                        if (aboutMap.get("facebookUrl") != null)
                            facebookUrl = aboutMap.get("facebookUrl");
                        if (aboutMap.get("googleUrl") != null)
                            googleUrl = aboutMap.get("googleUrl");
                        if (aboutMap.get("instagramUrl") != null)
                            instagramUrl = aboutMap.get("instagramUrl");
                    }
                    if (mapObj.get("userRate") != null)
                        vendorRate.setRating((long) mapObj.get("userRate"));
                }
            }
        }
        setSocialMedia();
    }

    private void setSocialMedia() {
        facebookImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String facebookPage = "";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                if (facebookUrl != null) {
                    facebookPage = facebookUrl.split("/")[facebookUrl.split("/").length - 1];
                }

                String uri = "";
                try {
                    int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
                    if (versionCode >= 3002850)
                        uri = facebookUrl != null ? "fb://facewebmodal/f?href=" + facebookUrl : "fb://facewebmodal/f?href=";
                    else
                        uri = "fb://page/" + facebookPage;
                    intent.setPackage("com.facebook.katana");
                } catch (PackageManager.NameNotFoundException e) {
                    uri = facebookUrl != null ? facebookUrl : "http://www.facebook.com";
                }


                intent.setData(Uri.parse(uri));
                startActivity(intent);
            }
        });
        instagramImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = instagramUrl != null ? instagramUrl : "https://www.instagram.com";
                String profileUser = instagramUrl != null ? instagramUrl.split("/")[instagramUrl.split("/").length - 1] : "";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                try {
                    if (packageManager.getPackageInfo("com.instagram.android", 0) != null) {
                        uri = "https://www.instagram.com/_u/" + profileUser;
                        intent.setPackage("com.instagram.android");
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }

                intent.setData(Uri.parse(uri));
                startActivity(intent);
            }
        });
        googleImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = googleUrl != null ? googleUrl : "https://plus.google.com/";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                try {
                    if (packageManager.getPackageInfo("com.google.android.apps.plus", 0) != null) {
                        intent.setPackage("com.google.android.apps.plus");
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }

                intent.setData(Uri.parse(uri));
                startActivity(intent);
            }
        });
    }

    private void getProducts() {
        reference = FirebaseDatabase.getInstance().getReference("products");
        loading3.setVisibility(View.VISIBLE);
        Query query = reference.orderByChild("vendor/vendorName").equalTo(getIntent().getExtras().getString("vendorName"));
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                loading3.setVisibility(View.GONE);
                if (dataSnapshot.exists()) {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    setProductArray(map);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                loading2.setVisibility(View.GONE);
                Toast.makeText(VendorInfoActivity.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setProductArray(Map<String, Object> map) {
        if (map != null) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                Product product = new Product();
                product.setProductId(entry.getKey());
                if (entry.getValue() instanceof Map) {
                    Map<String, Object> mapObj = (Map<String, Object>) entry.getValue();
                    product.setProductName((String) mapObj.get("productName"));
                    product.setImages((List<String>) mapObj.get("images"));
                    products.add(product);
                }
            }
            vendorAdapter.notifyDataSetChanged();
        }
    }

    private void setReview() {
        if (Defaults.getDefaults("userId", this) == null) {
            writeReview.setText("");
            writeReview.clearFocus();
            reviewRate.setVisibility(View.GONE);
            reviewRate.setRating(0);
            Toast.makeText(this, "You should be logged in to post the review", Toast.LENGTH_LONG).show();
        } else {
            DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("reviews");
            String reviewId = reference1.push().getKey();
            Date date = new Date();
            String format = new SimpleDateFormat("dd-MMM-yyyy").format(date);
            Review review = new Review(Defaults.getDefaults("userName", this), writeReview.getText().toString(), format, rate, null, null, null, getIntent().getExtras().getString("vendorName"));
            reference1.child(reviewId).setValue(review).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    writeReview.setText("");
                    writeReview.clearFocus();
                    reviewRate.setVisibility(View.GONE);
                    reviewRate.setRating(0);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(VendorInfoActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }

    }

    private void setupBazaars() {
        reference = FirebaseDatabase.getInstance().getReference("bazaars");
        loading2.setVisibility(View.VISIBLE);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                loading2.setVisibility(View.GONE);
                if (dataSnapshot.exists()) {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    setBazaarArray(map);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                loading2.setVisibility(View.GONE);
                Toast.makeText(VendorInfoActivity.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setBazaarArray(Map<String, Object> map) {
        if (map != null) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                Bazaar bazaar = new Bazaar();
                bazaar.setBazaarId(entry.getKey());
                if (entry.getValue() instanceof Map) {
                    Map<String, Object> mapObj = (Map<String, Object>) entry.getValue();
                    bazaar.setBazaarName((String) mapObj.get("bazaarName"));
                    bazaar.setBazaarPlace((String) mapObj.get("bazaarPlace"));
                    bazaar.setBazaarDesc((String) mapObj.get("bazaarDesc"));
                    bazaar.setOrganizerName((String) mapObj.get("organizerName"));
                    bazaar.setVendorNumbers((String) mapObj.get("vendorNumbers"));
                    bazaar.setImages((List<String>) mapObj.get("images"));
                    bazaar.setVendors((List<Map<String, String>>) mapObj.get("vendors"));
                    boolean existed = false;
                    if (bazaar.getVendors() != null) {
                        for (Map<String, String> vendor : bazaar.getVendors()) {
                            if (vendor.get("vendorName").equals(getIntent().getExtras().getString("vendorName")))
                                existed = true;
                        }
                    }
                    if (existed)
                        bazaars.add(bazaar);
                }
            }
            adapter.notifyDataSetChanged();
        }
    }

    private void getReviews() {
        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("reviews");
        Query query = reference1.orderByChild("vendorName").equalTo(getIntent().getExtras().getString("vendorName"));
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (loading.getVisibility() == View.VISIBLE)
                    loading.setVisibility(View.GONE);
                reviews.clear();
                if (dataSnapshot.exists()) {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    setupReviews(map);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                loading.setVisibility(View.GONE);
                Toast.makeText(VendorInfoActivity.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setupReviews(Map<String, Object> map) {
        if (map != null) {
            long sum = 0;
            long average = 0;
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                Review review = new Review();

                if (entry.getValue() instanceof Map) {
                    Map<String, Object> mapObj = (Map<String, Object>) entry.getValue();
                    review.setReviewDate((String) mapObj.get("reviewDate"));
                    review.setReviewText((String) mapObj.get("reviewText"));
                    review.setUserName((String) mapObj.get("userName"));
                    review.setUserRate((Long) mapObj.get("userRate"));
                    sum += (long) mapObj.get("userRate");
                    reviews.add(review);
                }
            }
            average = sum / reviews.size();
            while (average / 5 != 0) {
                average = average / 5;
            }
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
            final long finalAverage = average;
            databaseReference.child(getIntent().getExtras().getString("vendorId")).child("userRate").setValue(average)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            vendorRate.setRating(finalAverage);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(VendorInfoActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
            if (reviewsLayout.getVisibility() != View.VISIBLE)
                reviewsLayout.setVisibility(View.VISIBLE);
            reviewsAdapter.notifyDataSetChanged();
        }

    }
}
