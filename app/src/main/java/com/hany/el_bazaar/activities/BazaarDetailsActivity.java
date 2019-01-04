package com.hany.el_bazaar.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.hany.el_bazaar.Adapters.ImagesPagerAdapter;
import com.hany.el_bazaar.Adapters.ReviewsAdapter;
import com.hany.el_bazaar.Adapters.VendorsTabAdapter;
import com.hany.el_bazaar.Defaults;
import com.hany.el_bazaar.Model.Review;
import com.hany.el_bazaar.Model.Vendor;
import com.hany.el_bazaar.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

/**
 * Created by Hany on 12/18/2018.
 */

public class BazaarDetailsActivity extends AppCompatActivity {

    final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("reviews");
    ImageView backimg, facebookImage, googleImage, instagramImage, organizerImage;
    TextView actionTitle, organizerName, bazaarName, vendorsNumbers, descText, vendorName, brandName, bazaarPlace, postReview;
    RecyclerView reviewsList;
    RatingBar bazaarRate;
    ArrayList<Review> reviews;
    ReviewsAdapter reviewsAdapter;
    EditText writeReview;
    RatingBar reviewRate;
    long rate;
    RelativeLayout reviewsLayout;
    SpinKitView loading;
    ViewPager viewPager;
    LinearLayout sliderDotspanel;
    PackageManager packageManager;
    RecyclerView vendorsList;
    VendorsTabAdapter adapter;
    ArrayList<Vendor> vendors;
    String facebookUrl, googleUrl, instagramUrl;
    private int dotscount;
    private ImageView[] dots;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bazaar_details);
        backimg = findViewById(R.id.back_img);
        facebookImage = findViewById(R.id.facebook_contact);
        googleImage = findViewById(R.id.google_contact);
        instagramImage = findViewById(R.id.instagram_contact);
        actionTitle = findViewById(R.id.action_bar_title);
        organizerName = findViewById(R.id.organizer_name);
        organizerImage = findViewById(R.id.organizer_image);
        bazaarName = findViewById(R.id.bazaar_name);
        vendorsNumbers = findViewById(R.id.vendors_numbers);
        descText = findViewById(R.id.desc_text);
        vendorName = findViewById(R.id.vendor_name);
        brandName = findViewById(R.id.brand_name);
        bazaarRate = findViewById(R.id.bazaar_rate);
        reviewsList = findViewById(R.id.reviews);
        bazaarPlace = findViewById(R.id.bazaar_address);
        writeReview = findViewById(R.id.write_review);
        reviewRate = findViewById(R.id.review_rate);
        postReview = findViewById(R.id.post_review);
        reviewsLayout = findViewById(R.id.reviews_layout);
        vendorsList = findViewById(R.id.vendors);
        vendors = new ArrayList<>();
        vendorsList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new VendorsTabAdapter(this, vendors);
        adapter.setBazaarVendor(true);
        vendorsList.setAdapter(adapter);
        setVendorList();
        loading = findViewById(R.id.loading);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        packageManager = getPackageManager();
        getOrganizerContacts();
        sliderDotspanel = (LinearLayout) findViewById(R.id.slider_dots);
        ArrayList<String> imagesList = (ArrayList<String>) getIntent().getSerializableExtra("bazaarImages");

        if (imagesList != null) {
            if (imagesList.size() < 3) {
                if (imagesList.size() > 0)
                    for (int i = imagesList.size(); i < 3; i++)
                        imagesList.add("");
                else
                    for (int i = 0; i < 3; i++)
                        imagesList.add("");
            }
        } else {
            imagesList = new ArrayList<>();
            for (int i = 0; i < 3; i++)
                imagesList.add("");
        }

        ImagesPagerAdapter viewPagerAdapter = new ImagesPagerAdapter(this, false, imagesList);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(viewPagerAdapter);

        dotscount = viewPagerAdapter.getCount();
        dots = new ImageView[dotscount];

        for (int i = 0; i < dotscount; i++) {

            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(8, 0, 8, 0);

            sliderDotspanel.addView(dots[i], params);

        }
        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < dotscount; i++) {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));
                }
                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        backimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        reviews = new ArrayList<>();
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
        actionTitle.setText((CharSequence) getIntent().getExtras().get("bazaarName"));
        bazaarName.setText((CharSequence) getIntent().getExtras().get("bazaarName"));
        organizerName.setText((CharSequence) getIntent().getExtras().get("organizerName"));
        vendorsNumbers.setText((CharSequence) getIntent().getExtras().get("vendorsNumbers"));
        bazaarPlace.setText((CharSequence) getIntent().getExtras().get("bazaarPlace"));
        descText.setText((CharSequence) getIntent().getExtras().get("bazaarDesc"));
        organizerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BazaarDetailsActivity.this, OrganizerInfoActivity.class);
                intent.putExtra("organizerName", organizerName.getText().toString());
                startActivity(intent);
            }
        });
        reviewsList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        getReviews();
        reviewsAdapter = new ReviewsAdapter(this, reviews);
        if (reviews.size() == 0)
            reviewsLayout.setVisibility(View.GONE);
        reviewsList.setAdapter(reviewsAdapter);
    }

    private void setVendorList() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("bazaars");
        Query query = reference.orderByChild("bazaarName").equalTo(getIntent().getExtras().getString("bazaarName"));
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    setupVendors(map);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(BazaarDetailsActivity.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setupVendors(Map<String, Object> map) {
        if (map != null) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {

                if (entry.getValue() instanceof Map) {
                    Map<String, Object> mapObj = (Map<String, Object>) entry.getValue();
                    if (mapObj.get("bazaarRate") != null)
                        bazaarRate.setRating((long) mapObj.get("bazaarRate"));
                    ArrayList<Map<String, String>> bazaarVendors = (ArrayList<Map<String, String>>) mapObj.get("vendors");
                    if (bazaarVendors != null)
                        for (Map<String, String> map1 : bazaarVendors) {
                            Vendor vendor = new Vendor();
                            vendor.setVendorName(map1.get("vendorName"));
                            if (map1.get("brandName") != null)
                                vendor.setBrandName(map1.get("brandName"));
                            vendors.add(vendor);
                        }
                }
            }
            adapter.notifyDataSetChanged();
        }
    }

    private void getOrganizerContacts() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query query = reference.orderByChild("name").equalTo((String) getIntent().getExtras().get("organizerName"));
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
                Toast.makeText(BazaarDetailsActivity.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setAboutContent(Map<String, Object> map) {
        if (map != null) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {

                if (entry.getValue() instanceof Map) {
                    Map<String, Object> mapObj = (Map<String, Object>) entry.getValue();
                    if (mapObj.get("aboutMap") != null) {
                        Map<String, String> aboutMap = (Map<String, String>) mapObj.get("aboutMap");
                        if (aboutMap.get("facebookUrl") != null)
                            facebookUrl = aboutMap.get("facebookUrl");
                        if (aboutMap.get("googleUrl") != null)
                            googleUrl = aboutMap.get("googleUrl");
                        if (aboutMap.get("instagramUrl") != null)
                            instagramUrl = aboutMap.get("instagramUrl");
                    }
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

    private void getReviews() {
        Query query = reference.orderByChild("bazaarName").equalTo(getIntent().getExtras().getString("bazaarName"));
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
                Toast.makeText(BazaarDetailsActivity.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setReview() {
        if (Defaults.getDefaults("userId", this) == null) {
            writeReview.setText("");
            writeReview.clearFocus();
            reviewRate.setVisibility(View.GONE);
            reviewRate.setRating(0);
            Toast.makeText(this, "You should be logged in to post the review", Toast.LENGTH_LONG).show();
        } else {
            String reviewId = reference.push().getKey();
            Date date = new Date();
            String format = new SimpleDateFormat("dd-MMM-yyyy").format(date);
            Review review = new Review(Defaults.getDefaults("userName", this), writeReview.getText().toString(), format, rate, null, getIntent().getExtras().getString("bazaarName"), null, null);
            reference.child(reviewId).setValue(review).addOnSuccessListener(new OnSuccessListener<Void>() {
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
                    Toast.makeText(BazaarDetailsActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }

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
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("bazaars");
            final long finalAverage = average;
            databaseReference.child(getIntent().getExtras().getString("bazaarId")).child("bazaarRate").setValue(average)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            bazaarRate.setRating(finalAverage);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(BazaarDetailsActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
            if (reviewsLayout.getVisibility() != View.VISIBLE)
                reviewsLayout.setVisibility(View.VISIBLE);
            reviewsAdapter.notifyDataSetChanged();
        }
    }
}
